package me.taff_s.game.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.taff_s.game.player.Player;
import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.core.GameEvent;

public class DragonGameManager {
    public static int runCount = 0;
    
    private static final int MAX_RUNS = 3;

    private static Map<Player, Integer> bankedGold = new HashMap<>();
    private static Set<Integer> bankedRuns = new HashSet<>();
    public static GameEventManager eventManager = new GameEventManager();

        public void resetDragonState() {
        runCount = 0;
        bankedGold.clear();
        bankedRuns.clear();
    }

    public static void bankCoins(Player player1, Player player2) {
        int p1Banked = (int)(player1.getCoins() * 0.75);
        int p2Banked = (int)(player2.getCoins() * 0.75);
        
        bankedGold.put(player1, bankedGold.getOrDefault(player1, 0) + p1Banked);
        bankedGold.put(player2, bankedGold.getOrDefault(player2, 0) + p2Banked);
    
        player1.coinChange(-p1Banked);
        player2.coinChange(-p2Banked);
    
        eventManager.notify(new GameEvent(GameEvent.EventType.COINS_BANKED, Map.of(
            "player1Banked", p1Banked,
            "player2Banked", p2Banked
        )));
    }

    public static boolean isFinalRun() {
        return runCount == MAX_RUNS - 1;
    }

    
    public static int getBankedGold(Player player) {
        return bankedGold.getOrDefault(player, 0);
    }

    public static boolean isRunBanked() {
        return bankedRuns.contains(runCount);
    }

    public static void markRunBanked() {
        bankedRuns.add(runCount);
    }

    public static void handleRunCompletion(Player player1, Player player2) {
        boolean finalRun = isFinalRun();
        DragonNarrator.midScene(player1, player2, finalRun);
        
        if (finalRun){
            int p1Total = getBankedGold(player1);
            int p2Total = getBankedGold(player2);
            
            if (p1Total > p2Total) {
                DragonNarrator.finalTally(player1, player2);
            } else if (p2Total > p1Total) {
                DragonNarrator.finalTally(player2, player1);
            } else {
                player1.sendMessage("The dragon laughs. \"A tie? How delightfully inconvenient. I suppose... both of you get second place.\"");
                player2.sendMessage("The dragon laughs. \"A tie? How delightfully inconvenient. I suppose... both of you get second place.\"");
            }
        }
    }     
}