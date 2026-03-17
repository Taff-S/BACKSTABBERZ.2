package me.taff_s.game.net;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;

import me.taff_s.game.player.Player;
import me.taff_s.game.player.PlayerHandler;
import me.taff_s.game.core.GameLogger;
import me.taff_s.game.core.CoinDisplayObserver;
import me.taff_s.game.core.DragonGameManager;
import me.taff_s.game.core.DragonNarrator;
import me.taff_s.game.items.potions.PotionLibrary;
import me.taff_s.game.world.EncounterType;
import me.taff_s.game.world.RoomManager;
import me.taff_s.game.world.RoomType;
import me.taff_s.game.enemies.EnemyFactory;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.enemies.types.Dragon;
import me.taff_s.game.combat.CombatEncounter;
import me.taff_s.game.combat.CombatResult;
import me.taff_s.game.world.Shop;
import me.taff_s.game.world.RestRoom;
import me.taff_s.game.world.MimicEncounter;
import me.taff_s.game.world.BloodMachineEcounter;

public class GameServer {
    private static final int PORT = 5000;
    private static final ExecutorService playerThreads = Executors.newFixedThreadPool(2);
    private static final ConcurrentHashMap<Integer, PrintWriter> players = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Boolean> readyPlayers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Player> playerObjects = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, BufferedReader> inputReaders = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Socket> playerSockets = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, PlayerHandler> playerHandlers = new ConcurrentHashMap<>();

    private static void runSimultaneously(Runnable r1, Runnable r2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.submit(r1);
            executor.submit(r2);
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static boolean handleDeathAndResetIfNeeded(Player player1, Player player2, int run) {
        if (player1.living() && player2.living()) {
            return false;
        }

        DragonGameManager.handleRunCompletion(player1, player2);
        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());

        if (run == 2) {
            System.out.println("Game Over. Server shutting down.");
            System.exit(0);
        }

        return true;
    }

    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException{     
        GameLogger logger = new GameLogger();
        DragonGameManager.eventManager.addListener(logger);

        CoinDisplayObserver coinObserver = new CoinDisplayObserver();
        DragonGameManager.eventManager.addListener(coinObserver);

        try (ServerSocket serverSocket = new ServerSocket(PORT))  {

            System.out.println("Game server started... Waiting for players.");

            int playerCounter = 1;
            while (playerCounter <= 2) {
                Socket playerSocket = serverSocket.accept();
                int currentPlayerId = playerCounter;

                PrintWriter writer = new PrintWriter(playerSocket.getOutputStream(), true);
                // players.put(currentPlayerId, writer);
                readyPlayers.put(currentPlayerId, false); 

                PlayerHandler handler = new PlayerHandler(playerSocket, currentPlayerId, readyPlayers, playerObjects, inputReaders, players, playerSockets);
                playerHandlers.put(currentPlayerId, handler);

                playerThreads.submit(handler);
                
                System.out.println("Player " + currentPlayerId + " connected.");

                playerCounter++;
            }
            
            //waitForPlayers(); // Ensures both players are ready before game starts
            playerHandlers.get(1).awaitReady(10, TimeUnit.MINUTES);
            playerHandlers.get(2).awaitReady(10, TimeUnit.MINUTES);
            players.values().forEach(p -> p.println("Both players are ready! The dungeon begins..."));

            Player player1 = playerObjects.get(1);
            Player player2 = playerObjects.get(2);

            BufferedReader in1 = inputReaders.get(1);
            BufferedReader in2 = inputReaders.get(2);

            PrintWriter out1 = players.get(1);
            PrintWriter out2 = players.get(2);
            
            player1.getInventory().addItem(PotionLibrary.GREATER_HEAL);
            player2.getInventory().addItem(PotionLibrary.GREATER_HEAL);

            DragonNarrator.startMonologue(player1, player2);
            
            runLoop:
            for (int run = 0; run < 3; run++) {
                DragonGameManager.runCount = run;
                RoomManager generator = new RoomManager();

                while (generator.hasMoreRooms()) {
                    RoomType room = generator.getNextRoomType();
                    switch (room) {
                    case COMBAT: {
                        player1.sendMessage("---------------------------------------------------------");
                        player2.sendMessage("---------------------------------------------------------");

                        //String enemyType = EnemyFactory.getRandomEnemyName();
                        //Enemy enemy1 = EnemyFactory.create(enemyType);
                        Enemy enemy1 = EnemyFactory.create("slime","standard");

                        enemy1.display(player2);
                        enemy1.display(player1);

                        CombatEncounter encounter = new CombatEncounter(player1, player2, enemy1);
                        List<CombatResult> results = encounter.start(playerHandlers.get(1), playerHandlers.get(2));

                        // Send results of each action to both players
                        for (CombatResult result : results) {
                            for (String event : result.getEvents()) {
                                playerHandlers.get(1).getMessenger().send(event);
                                playerHandlers.get(2).getMessenger().send(event);
                            }
                        }

                        // Handle win/loss
                        if (!enemy1.living()) {
                            playerHandlers.get(1).getMessenger().send("Enemy defeated!");
                            playerHandlers.get(2).getMessenger().send("Enemy defeated!");
                            player1.coinChange(enemy1.getCoinDrop());
                            player2.coinChange(enemy1.getCoinDrop());
                        } else if (!player1.living() || !player2.living()) {
                            playerHandlers.get(1).getMessenger().send("A player has died. Combat ends.");
                            playerHandlers.get(2).getMessenger().send("A player has died. Combat ends.");
                        }
                        break;
                    }
                        case ENCOUNTER: {
                            player1.sendMessage("---------------------------------------------------------");
                            player2.sendMessage("---------------------------------------------------------");

                            EncounterType encounterType = EncounterType.getRandomEncounter();
                            player1.sendMessage("You encounter: " + encounterType);
                            player2.sendMessage("You encounter: " + encounterType);

                            switch (encounterType) {
                                case SHOP:
                                    runSimultaneously(
                                        () -> Shop.createRandomShop().interact(player1),
                                        () -> Shop.createRandomShop().interact(player2)
                                    );
                                    break;
                                case MIMIC:
                                    new MimicEncounter().interact(player1, player2);
                                    break;
                                case BLOODMACHINE:
                                    new BloodMachineEcounter().interact(player1, player2);
                                    break;
                                case TREASURE:
                                    player1.sendMessage("You found a treasure room! (Not yet implemented.)");
                                    player2.sendMessage("You found a treasure room! (Not yet implemented.)");
                                    break;
                                case VSHOP:
                                    player1.sendMessage("You found a vampire shop! (Not yet implemented.)");
                                    player2.sendMessage("You found a vampire shop! (Not yet implemented.)");
                                    break;
                                case VQUEEN:
                                    player1.sendMessage("You have stumbled upon the vampire queen... (Not yet implemented.)");
                                    player2.sendMessage("You have stumbled upon the vampire queen... (Not yet implemented.)");
                                    break;
                                case BLACKSMITH:
                                    player1.sendMessage("You see a blacksmith's forge... (Not yet implemented.)");
                                    player2.sendMessage("You see a blacksmith's forge... (Not yet implemented.)");
                                    break;
                            }

                            break;
                        }
                        case REST: {
                            player1.sendMessage("---------------------------------------------------------");
                            player2.sendMessage("---------------------------------------------------------");
                            RestRoom restTime = new RestRoom(player1, player2);
                            restTime.enter(playerHandlers.get(1), playerHandlers.get(2));   
                            break;
                        }
                        case BOSS: {
                            player1.sendMessage("---------------------------------------------------------");
                            player2.sendMessage("---------------------------------------------------------");
                            DragonNarrator.finalFightIntro(player1, player2);
                            Enemy dragon1 = new Dragon();
                            CombatEncounter finalFight = new CombatEncounter(player1, player2, dragon1);
                            //boolean playersWin = finalFight.start(playerHandlers.get(1), playerHandlers.get(2));
                            List<CombatResult> finalFightResult = finalFight.start(playerHandlers.get(1), playerHandlers.get(2));

                            // Send results of each action to both players
                            for (CombatResult results : finalFightResult) {
                                for (String event : results.getEvents()) {
                                    playerHandlers.get(1).getMessenger().send(event);
                                    playerHandlers.get(2).getMessenger().send(event);
                                }
                            }

                            // Handle win/loss
                            if (!dragon1.living()) {
                                playerHandlers.get(1).getMessenger().send("Enemy defeated!");
                                playerHandlers.get(2).getMessenger().send("Enemy defeated!");
                                DragonNarrator.dragonDefeat(player1, player2);
                                System.exit(0);
                            } else if (!player1.living() || !player2.living()) {
                                playerHandlers.get(1).getMessenger().send("A player has died. Combat ends.");
                                playerHandlers.get(2).getMessenger().send("A player has died. Combat ends.");
                                DragonNarrator.dragonVictory(player1, player2, run == 2);
                            }

                            // if (playersWin) {
                            //     DragonNarrator.dragonDefeat(player1, player2);
                            //     System.exit(0);
                            // } else {
                            //     DragonNarrator.dragonVictory(player1, player2, run == 2);
                            // }
                            break;
                        }
                    }

                    if (handleDeathAndResetIfNeeded(player1, player2, run)) {
                        break;
                    }
                }

                if (!player1.living() || !player2.living()) {
                    continue;
                }

                DragonGameManager.handleRunCompletion(player1, player2); // Final run check
                player1.setHealth(player1.getMaxHealth());
                player2.setHealth(player2.getMaxHealth());

        }
        playerThreads.shutdown();
        playerThreads.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Game Over. Server shutting down.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // private static void waitForPlayers() {
    //     while (true) {
    //         if (readyPlayers.size() == 2 && readyPlayers.values().stream().allMatch(ready -> ready)) {
    //             break;
    //         }
    //         try {
    //             Thread.sleep(1000);
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    
    //     players.values().forEach(p -> p.println("Both players are ready! The dungeon begins..."));
    // }
}