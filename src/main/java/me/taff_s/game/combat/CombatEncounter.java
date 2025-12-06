package me.taff_s.game.combat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import me.taff_s.game.player.Player;
import me.taff_s.game.player.PlayerHandler;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.enemies.types.Dragon;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.charms.ShieldCharm;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.combat.CombatResult;
import me.taff_s.game.combat.CombatSystem;

public class CombatEncounter {
    private Player player1;
    private Player player2;
    private Enemy enemy;
    private ExecutorService inputExecutor = Executors.newFixedThreadPool(2);

    public CombatEncounter(Player p1, Player p2, Enemy enemy) {
        this.player1 = p1;
        this.player2 = p2;
        this.enemy = enemy;
    }

    public List<CombatResult> start(PlayerHandler ph1, PlayerHandler ph2) throws InterruptedException, ExecutionException {
        List<CombatResult> combatLog = new ArrayList<>();
        player1.dStanceCounter = 2;
        player2.dStanceCounter = 2;

        for (Player player : List.of(player1, player2)) {
            for (Charm charm : player.getEquippedCharms()) {
                if (charm instanceof ShieldCharm) {
                    player.dStanceCounter += 1;
                    break; // Only one bonus per player
                }
            }
        }

        while (player1.living() && player2.living() && enemy.living()) {
            // Prompt both players for actions
            //ph1.getMessenger().send("ts so mustard");
            //ph2.getMessenger().send("erm what the sigma");
            ph1.getMessenger().send("Your HP: " + player1.getHealth());
            ph2.getMessenger().send("Your HP: " + player2.getHealth());

            Future<String> p1Future = inputExecutor.submit(() -> ph1.getMessenger().prompt("Choose action (1: Attack, 2: Defend)"));
            Future<String> p2Future = inputExecutor.submit(() -> ph2.getMessenger().prompt("Choose action (1: Attack, 2: Defend)"));
            
            String p1Action = p1Future.get(); // Wait until player 1 responds
            String p2Action = p2Future.get(); // Wait until player 2 responds
            
            // Player 1's turn
            CombatResult p1Result = handlePlayerTurn(player1, p1Action);
            for (String event : p1Result.getEvents()) {
                ph1.getMessenger().send(event);
                ph2.getMessenger().send(event);
            }

            // Player 2's turn
            CombatResult p2Result = handlePlayerTurn(player2, p2Action);
            for (String event : p2Result.getEvents()) {
                ph1.getMessenger().send(event);
                ph2.getMessenger().send(event);
            }

            // Check if enemy is dead before enemy attacks
            if (!enemy.living()) break;

            // Enemy attacks random player
            Player target = Math.random() < 0.5 ? player1 : player2;
            CombatResult enemyResult = CombatSystem.enemyAttack(enemy, target);
            combatLog.add(enemyResult);
            for (String event : enemyResult.getEvents()) {
                ph1.getMessenger().send(event);
                ph2.getMessenger().send(event);
            }
        }

        inputExecutor.shutdown();

        // Halve coins if a player died (and enemy is not a Dragon)
        if (!(enemy instanceof Dragon)) {
            if (!player1.living()) {
                int lost = player1.getCoins() / 2;
                player1.coinChange(-lost);
                combatLog.add(new CombatResult() {{
                    addEvent(player1.getName() + " has died and lost half their coins!");
                }});
                String msg = player1.getName() + " has died and lost half their coins!";
                ph1.getMessenger().send(msg);
                ph2.getMessenger().send(msg);
            }
            if (!player2.living()) {
                int lost = player2.getCoins() / 2;
                player2.coinChange(-lost);
                combatLog.add(new CombatResult() {{
                    addEvent(player2.getName() + " has died and lost half their coins!");
                }});
                String msg = player2.getName() + " has died and lost half their coins!";
                ph1.getMessenger().send(msg);
                ph2.getMessenger().send(msg);
            }
        }

        return combatLog;
    }

    private CombatResult handlePlayerTurn(Player actingPlayer, String action) {
        switch (action) {
            case "1":
                return CombatSystem.performAttack(actingPlayer, enemy);
            case "2":
                return CombatSystem.performDefend(actingPlayer);
            default:
                CombatResult result = new CombatResult();
                result.addEvent(actingPlayer.getName() + " panics and loses the turn!");
                return result;
        }
    }
}
