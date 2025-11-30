import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BackStabEncounter {
    private Player player1;
    private Player player2;
    ExecutorService inputExecutor = Executors.newFixedThreadPool(2);

    public BackStabEncounter(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
    }

    public List<CombatResult> start(PlayerHandler ph1, PlayerHandler ph2)
            throws IOException, InterruptedException, ExecutionException {
        List<CombatResult> combatLog = new ArrayList<>();
        player1.dStanceCounter = 2;
        player2.dStanceCounter = 2;
        player1.setPartner(player2);
        player2.setPartner(player1);

        while (player1.living() && player2.living()) {
            player1.onTurnStart();
            player2.onTurnStart();

            ph1.getMessenger().send("Your HP: " + player1.getHealth());
            ph2.getMessenger().send("Your HP: " + player2.getHealth());

            Future<String> p1Future = inputExecutor.submit(() -> ph1.getMessenger().prompt("Choose action (1: Attack, 2: Defend)"));
            Future<String> p2Future = inputExecutor.submit(() -> ph2.getMessenger().prompt("Choose action (1: Attack, 2: Defend)"));

            String p1Action = p1Future.get();
            String p2Action = p2Future.get();

            if (p1Action == null || p2Action == null) {
                ph1.getMessenger().send("One of the players disconnected.");
                ph2.getMessenger().send("One of the players disconnected.");
                break;
            }

            combatLog.addAll(resolveCombatRound(p1Action, p2Action));

            if (!player1.living() || !player2.living()) break;
            ph1.getMessenger().send("Your HP: " + player1.getHealth());
            ph2.getMessenger().send("Your HP: " + player2.getHealth());
        }

        inputExecutor.shutdown();

        // Handle coin transfer on death
        if (!player1.living()) {
            int temp = player1.getCoins() / 2;
            player1.coinChange(-temp);
            player2.coinChange(temp);
            CombatResult result = new CombatResult();
            result.addEvent(player1.getName() + " has died and lost half their coins to " + player2.getName() + "!");
            combatLog.add(result);
        } else if (!player2.living()) {
            int temp = player2.getCoins() / 2;
            player2.coinChange(-temp);
            player1.coinChange(temp);
            CombatResult result = new CombatResult();
            result.addEvent(player2.getName() + " has died and lost half their coins to " + player1.getName() + "!");
            combatLog.add(result);
        }

        ph1.getMessenger().send("Combat has ended.");
        ph2.getMessenger().send("Combat has ended.");

        return combatLog;
    }

    private List<CombatResult> resolveCombatRound(String p1Action, String p2Action) {
        List<CombatResult> roundLog = new ArrayList<>();
        boolean p1Def = "2".equals(p1Action);
        boolean p2Def = "2".equals(p2Action);

        if ("1".equals(p1Action)) {
            roundLog.add(CombatSystem.playerAttack(player1, player2, player1.getEquipment().getEquippedWeapon(), p2Def));
        } else if (p1Def) {
            CombatResult result = new CombatResult();
            result.addEvent(player1.getName() + " defends.");
            roundLog.add(result);
            player1.defend();
        }

        if ("1".equals(p2Action)) {
            roundLog.add(CombatSystem.playerAttack(player2, player1, player2.getEquipment().getEquippedWeapon(), p1Def));
        } else if (p2Def) {
            CombatResult result = new CombatResult();
            result.addEvent(player2.getName() + " defends.");
            roundLog.add(result);
            player2.defend();
        }

        return roundLog;
    }
}