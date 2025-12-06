package me.taff_s.game.world;

import java.io.IOException;
import java.util.concurrent.*;
import me.taff_s.game.player.Player;
import me.taff_s.game.player.PlayerHandler;
import me.taff_s.game.player.RestChoice;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.combat.BackStabEncounter;

public class RestRoom {
    private final Player player1;
    private final Player player2;

    public RestRoom(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
    }

    public void enter(PlayerHandler handle1, PlayerHandler handle2) {
        ExecutorService inputExecutor = Executors.newFixedThreadPool(2);

        handle1.sendMulti("You both enter a quiet room. You can finally rest... or not.",
                player1.getName() + ", choose your action:",
                "[1] Rest peacefully (+7 HP)",
                "[2] Rest warily (+3 HP, can defend)",
                "[3] Open inventory",
                "[4] Betray your partner");
        handle2.sendMulti("You both enter a quiet room. You can finally rest... or not.",
                player2.getName() + ", choose your action:",
                "[1] Rest peacefully (+7 HP)",
                "[2] Rest warily (+3 HP, can defend)",
                "[3] Open inventory",
                "[4] Betray your partner");

        try {
            Thread.sleep(100);
            Future<RestChoice> choice1Future = inputExecutor.submit(() -> getPlayerChoice(player1, handle1));
            Future<RestChoice> choice2Future = inputExecutor.submit(() -> getPlayerChoice(player2, handle2));

            RestChoice choice1 = choice1Future.get();
            RestChoice choice2 = choice2Future.get();

            resolveChoices(choice1, player1, handle1, choice2, player2, handle2);
            activateCharms(player1, player2);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            inputExecutor.shutdown();
        }
    }

    private RestChoice getPlayerChoice(Player player, PlayerHandler handler) throws IOException {
        while (true) {
            String input = handler.prompt("> ");

            if (input == null) continue;

            switch (input.trim()) {
                case "1":
                    player.setRestType(RestChoice.PEACEFUL);
                    return RestChoice.PEACEFUL;
                case "2":
                    player.setRestType(RestChoice.WARY);
                    return RestChoice.WARY;
                case "3":
                    player.getInventory().manageInventory(handler, player);  // Make sure this doesn’t block
                    break;
                case "4":
                    return RestChoice.BACKSTAB;
                default:
                    handler.send("Invalid input. Please enter 1, 2, 3, or 4.");
            }
        }
    }

    private void resolveChoices(RestChoice c1, Player p1, PlayerHandler ph1, RestChoice c2, Player p2, PlayerHandler ph2) {
        BackStabEncounter backStabEncounter = new BackStabEncounter(p1, p2);
        try {
            if (c1 == RestChoice.BACKSTAB && c2 == RestChoice.BACKSTAB) {
                ph1.send("You both stare each other down... then simultaneously lunge into battle!");
                ph2.send("You both stare each other down... then simultaneously lunge into battle!");
                backStabEncounter.start(ph1, ph2);
            
            } else if (c1 == RestChoice.BACKSTAB && c2 == RestChoice.PEACEFUL) {
                ph1.send("You backstab your companion! They lose half their health!");
                ph2.send("You were betrayed in your sleep! You lose half your health!");
                p2.antiHeal(-1*p2.getHealth() / 2);
                backStabEncounter.start(ph1, ph2);
            
            } else if (c2 == RestChoice.BACKSTAB && c1 == RestChoice.PEACEFUL) {
                ph2.send("You backstab your companion! They lose half their health!");
                ph1.send("You were betrayed in your sleep! You lose half your health!");
                p1.antiHeal(-1*p1.getHealth() / 2);
                backStabEncounter.start(ph1, ph2);
            
            } else if (c1 == RestChoice.BACKSTAB && c2 == RestChoice.WARY) {
                ph1.send("You attempt a backstab, but your wary companion was ready!");
                ph2.send("You were right to be wary. Your companion tried to betray you!");
                backStabEncounter.start(ph1, ph2);
            
            } else if (c2 == RestChoice.BACKSTAB && c1 == RestChoice.WARY) {
                ph2.send("You attempt a backstab, but your wary companion was ready!");
                ph1.send("You were right to be wary. Your companion tried to betray you!");
                backStabEncounter.start(ph1, ph2);
            
            } else {
                if (c1 == RestChoice.PEACEFUL) {
                    ph1.send("You sleep peacefully and recover.");
                    p1.heal(7);
                }
                if (c2 == RestChoice.PEACEFUL) {
                    ph2.send("You sleep peacefully and recover.");
                    p2.heal(7);
                }
                if (c1 == RestChoice.WARY) {
                    ph1.send("You rest warily, healing a bit while staying alert.");
                    p1.heal(3);
                }
                if (c2 == RestChoice.WARY) {
                    ph2.send("You rest warily, healing a bit while staying alert.");
                    p2.heal(3);
                }
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace(); 
        }
    }

    private void activateCharms(Player p1, Player p2) {
        for (Charm charm : p1.getEquippedCharms()) {
            charm.onRest(p1);
        }
        for (Charm charm : p2.getEquippedCharms()) {
            charm.onRest(p2);
        }
    }
}
