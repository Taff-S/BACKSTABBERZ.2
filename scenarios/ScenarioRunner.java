package scenarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import me.taff_s.game.combat.CombatEncounter;
import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.enemies.EnemyFactory;
import me.taff_s.game.items.weapons.WeaponLibrary;
import me.taff_s.game.player.Player;
import me.taff_s.game.player.PlayerHandler;
import me.taff_s.game.world.BlacksmithEncounter;
import me.taff_s.game.world.BloodMachineEcounter;
import me.taff_s.game.world.MimicEncounter;
import me.taff_s.game.world.RestRoom;
import me.taff_s.game.world.Shop;
import me.taff_s.game.world.TreasureEncounter;
import me.taff_s.game.world.VQueenEncounter;

public final class ScenarioRunner {

    private static final BufferedReader CONSOLE_INPUT =
            new BufferedReader(new InputStreamReader(System.in));

    private ScenarioRunner() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: ScenarioRunner <encounter|rest|combat>");
            System.exit(2);
        }

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            Socket client1 = new Socket("127.0.0.1", port);
            Socket server1 = serverSocket.accept();
            Socket client2 = new Socket("127.0.0.1", port);
            Socket server2 = serverSocket.accept();

            ConcurrentHashMap<Integer, Boolean> readyPlayers = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer, Player> playerObjects = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer, BufferedReader> inputReaders = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer, PrintWriter> players = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer, Socket> playerSockets = new ConcurrentHashMap<>();
            PlayerHandler handler1 = new PlayerHandler(server1, 1, readyPlayers,
                    playerObjects, inputReaders, players, playerSockets);
            PlayerHandler handler2 = new PlayerHandler(server2, 2, readyPlayers,
                    playerObjects, inputReaders, players, playerSockets);

            ExecutorService workers = Executors.newFixedThreadPool(4);
            try {
                workers.submit(handler1);
                workers.submit(handler2);
                workers.submit(() -> runConsoleClient(1, client1));
                workers.submit(() -> runConsoleClient(2, client2));

                if (!handler1.awaitReady(10, TimeUnit.MINUTES)
                        || !handler2.awaitReady(10, TimeUnit.MINUTES)) {
                    throw new IllegalStateException("Both scenario players must become ready.");
                }

                Player player1 = handler1.getPlayer();
                Player player2 = handler2.getPlayer();
                configurePlayers(player1, player2);
                player1.setPartner(player2);
                player2.setPartner(player1);

                runScenario(args[0], player1, player2, handler1, handler2);
            } finally {
                closeQuietly(client1);
                closeQuietly(client2);
                closeQuietly(server1);
                closeQuietly(server2);
                workers.shutdownNow();
            }
        }
    }

    private static void configurePlayers(Player player1, Player player2) {
        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth() / 2);
        player1.coinChange(100 - player1.getCoins());
        player2.coinChange(1000 - player2.getCoins());
        player1.getEquipment().setWeapon(WeaponLibrary.executionerSword.copy());
        player2.getEquipment().setWeapon(WeaponLibrary.practiceSword.copy());

        player1.sendMessage("Scenario setup: full health, 100 coins, Executioner's Sword.");
        player2.sendMessage("Scenario setup: half health, 1000 coins, Practice Sword.");
    }

    private static void runScenario(String scenario, Player player1, Player player2,
                                    PlayerHandler handler1, PlayerHandler handler2)
            throws Exception {
        switch (scenario.trim().toLowerCase()) {
            case "treasure":
                new TreasureEncounter().interact(player1, player2);
                break;
            case "mimic":
                new MimicEncounter().interact(player1, player2);
                break;
            case "blood-machine":
                new BloodMachineEcounter().interact(player1, player2);
                break;
            case "vampire-queen":
                new VQueenEncounter().interact(player1, player2);
                break;
            case "blacksmith":
                new BlacksmithEncounter().interact(player1, player2);
                break;
            case "shop":
                Shop.createRandomShop().interact(player1);
                Shop.createRandomShop().interact(player2);
                break;
            case "vampire-shop":
                player1.sendMessage("The vampire shop encounter is not implemented yet.");
                player2.sendMessage("The vampire shop encounter is not implemented yet.");
                break;
            case "rest":
                new RestRoom(player1, player2).enter(handler1, handler2);
                break;
            case "combat":
                Enemy enemy = EnemyFactory.create("slime", "standard");
                enemy.display(player1);
                enemy.display(player2);
                new CombatEncounter(player1, player2, enemy).start(handler1, handler2);
                break;
            default:
                throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
    }

    private static void runConsoleClient(int playerNumber, Socket socket) {
        try (BufferedReader serverInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {
            int startupPrompt = 0;
            String line;
            while ((line = serverInput.readLine()) != null) {
                if (line.equals(">")) {
                    startupPrompt++;
                    if (startupPrompt == 1) {
                        serverOutput.println("Scenario Player " + playerNumber);
                    } else if (startupPrompt == 2) {
                        serverOutput.println("1");
                    } else {
                        serverOutput.println(readConsoleInput(playerNumber));
                    }
                } else {
                    synchronized (System.out) {
                        System.out.println("[Player " + playerNumber + "] " + line);
                    }
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                synchronized (System.err) {
                    System.err.println("Player " + playerNumber + " scenario connection failed: "
                            + e.getMessage());
                }
            }
        }
    }

    private static String readConsoleInput(int playerNumber) throws IOException {
        synchronized (ScenarioRunner.class) {
            System.out.print("Player " + playerNumber + " input> ");
            System.out.flush();
            return CONSOLE_INPUT.readLine();
        }
    }

    private static void closeQuietly(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
            // Closing a local scenario socket is best-effort cleanup.
        }
    }
}
