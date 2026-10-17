package me.taff_s.game.world;

import java.util.ArrayList;
import java.util.List;
import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreasureEncounterTest {

    @Test
    void higherHpPlayerWinsTheWrestleAndTakesTheWholeChest() {
        ScriptedPlayer player1 = player("One", 150, "1");
        ScriptedPlayer player2 = player("Two", 100, "1");

        encounter(0, true).interact(player1, player2);

        assertEquals(200, player1.getCoins());
        assertEquals(0, player2.getCoins());
        assertTrue(player1.messages.stream().anyMatch(message -> message.contains("higher current HP")));
    }

    @Test
    void agreeingToShareSplitsTheEntireChestEvenly() {
        ScriptedPlayer player1 = player("One", 100, "2");
        ScriptedPlayer player2 = player("Two", 100, "2");

        encounter(1, true).interact(player1, player2);

        assertEquals(100, player1.getCoins());
        assertEquals(101, player2.getCoins());
        assertEquals(201, player1.getCoins() + player2.getCoins());
    }

    @Test
    void coinTossAwardsAllCoinsToTheWinner() {
        ScriptedPlayer player1 = player("One", 100, "3");
        ScriptedPlayer player2 = player("Two", 100, "3");

        encounter(300, true).interact(player1, player2);

        assertEquals(500, player1.getCoins());
        assertEquals(0, player2.getCoins());
    }

    @Test
    void playerOneChoiceIsShownToPlayerTwoAndTheyCanAgreeOnANextAttempt() {
        ScriptedPlayer player1 = player("One", 100, "1", "2");
        ScriptedPlayer player2 = player("Two", 100, "3", "2");

        encounter(0, false).interact(player1, player2);

        assertEquals(100, player1.getCoins());
        assertEquals(100, player2.getCoins());
        assertTrue(player2.messages.stream().anyMatch(message -> message.contains("One chose: Wrestle partner")));
        assertTrue(player1.messages.stream().anyMatch(message -> message.contains("disagreed 1 of 3 times")));
    }

    @Test
    void thiefTakesChestAfterThreeDisagreements() {
        ScriptedPlayer player1 = player("One", 100, "1", "2", "3");
        ScriptedPlayer player2 = player("Two", 100, "2", "3", "1");

        encounter(0, false).interact(player1, player2);

        assertEquals(0, player1.getCoins());
        assertEquals(0, player2.getCoins());
        assertTrue(player1.messages.stream().anyMatch(message -> message.contains("snatches the entire chest")));
        assertTrue(player2.messages.stream().anyMatch(message -> message.contains("snatches the entire chest")));
        assertEquals(3, player1.prompts.size());
        assertEquals(3, player2.prompts.size());
    }

    private static TreasureEncounter encounter(int chestRoll, boolean player1WinsCoinToss) {
        return new TreasureEncounter(new FixedRandom(chestRoll, player1WinsCoinToss));
    }

    private static ScriptedPlayer player(String name, int health, String... choices) {
        ScriptedPlayer player = new ScriptedPlayer(name, choices);
        player.setHealth(health);
        return player;
    }

    private static class ScriptedPlayer extends Player {
        private final List<String> choices;
        private final List<String> messages = new ArrayList<>();
        private final List<String> prompts = new ArrayList<>();
        private int nextChoice;

        ScriptedPlayer(String name, String... choices) {
            super(name, 200, 200, 0, new GameEventManager());
            this.choices = List.of(choices);
        }

        @Override
        public String userInput(String prompt) {
            prompts.add(prompt);
            return choices.get(nextChoice++);
        }

        @Override
        public void sendMessage(String message) {
            messages.add(message);
        }
    }

    private static class FixedRandom extends java.util.Random {
        private final int chestRoll;
        private final boolean player1WinsCoinToss;

        FixedRandom(int chestRoll, boolean player1WinsCoinToss) {
            this.chestRoll = chestRoll;
            this.player1WinsCoinToss = player1WinsCoinToss;
        }

        @Override
        public int nextInt(int bound) {
            return chestRoll;
        }

        @Override
        public boolean nextBoolean() {
            return player1WinsCoinToss;
        }
    }
}
