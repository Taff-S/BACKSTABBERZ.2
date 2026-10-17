package me.taff_s.game.world;

import java.util.ArrayList;
import java.util.List;
import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponClass;
import me.taff_s.game.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlacksmithEncounterTest {

    @Test
    void playerOneWinsAtOpeningPriceWhenPlayerTwoFoldsAndCanBuyRemainingItem() {
        ScriptedPlayer player1 = player("One", 100, "1");
        ScriptedPlayer player2 = player("Two", 100, "1", "fold", "yes");
        BlacksmithEncounter encounter = encounter();

        encounter.interact(player1, player2);

        assertEquals(75, player1.getCoins());
        assertEquals(50, player2.getCoins());
        assertHasItem(player1, "First weapon");
        assertHasItem(player2, "Second weapon");
    }

    @Test
    void laterBidWinsAndPreviousLeaderCanDeclineTheOtherItem() {
        ScriptedPlayer player1 = player("One", 100, "1", "fold", "no");
        ScriptedPlayer player2 = player("Two", 100, "1", "bid");

        encounter().interact(player1, player2);

        assertEquals(100, player1.getCoins());
        assertEquals(65, player2.getCoins());
        assertFalse(hasItem(player1, "First weapon"));
        assertHasItem(player2, "First weapon");
    }

    @Test
    void bidderWhoCannotAffordTheNextRaiseLosesAtTheCurrentBid() {
        ScriptedPlayer player1 = player("One", 40, "1");
        ScriptedPlayer player2 = player("Two", 30, "1", "no");

        encounter().interact(player1, player2);

        assertEquals(15, player1.getCoins());
        assertEquals(30, player2.getCoins());
        assertHasItem(player1, "First weapon");
        assertFalse(hasItem(player2, "First weapon"));
    }

    @Test
    void differentSelectionsAreSoldIndependentlyAtAskingPrice() {
        ScriptedPlayer player1 = player("One", 100, "1");
        ScriptedPlayer player2 = player("Two", 100, "2");

        encounter().interact(player1, player2);

        assertEquals(75, player1.getCoins());
        assertEquals(50, player2.getCoins());
        assertHasItem(player1, "First weapon");
        assertHasItem(player2, "Second weapon");
    }

    @Test
    void invalidAuctionInputDoesNotRaiseTheBidOrTransferTheItem() {
        ScriptedPlayer player1 = player("One", 100, "1", "fold", "no");
        ScriptedPlayer player2 = player("Two", 100, "1", "raise", "fold", "no");

        encounter().interact(player1, player2);

        assertEquals(75, player1.getCoins());
        assertEquals(100, player2.getCoins());
        assertHasItem(player1, "First weapon");
        assertFalse(hasItem(player2, "First weapon"));
        assertTrue(player2.messages.stream().anyMatch(message -> message.contains("Invalid bid action")));
    }

    @Test
    void encounterCopiesStockItemsBeforeSellingThem() {
        Weapon stockTemplate = weapon("Template weapon", 10);
        BlacksmithEncounter encounter = new BlacksmithEncounter(stockTemplate, weapon("Other", 20));
        ScriptedPlayer player1 = player("One", 100, "1");
        ScriptedPlayer player2 = player("Two", 100, "3");

        encounter.interact(player1, player2);

        Item purchased = player1.getInventory().getItems().get(0);
        assertFalse(purchased == stockTemplate);
        assertEquals("Template weapon", purchased.getItemName());
        assertEquals(10, stockTemplate.getPrice());
    }

    private static BlacksmithEncounter encounter() {
        return new BlacksmithEncounter(weapon("First weapon", 10), weapon("Second weapon", 20));
    }

    private static Weapon weapon(String name, int price) {
        return new Weapon(name, name + " description", price, 10,
                WeaponClass.SWORD, DamageType.SLASH, 5);
    }

    private static ScriptedPlayer player(String name, int coins, String... choices) {
        return new ScriptedPlayer(name, coins, choices);
    }

    private static boolean hasItem(Player player, String itemName) {
        return player.getInventory().getItems().stream()
                .anyMatch(item -> item.getItemName().equals(itemName));
    }

    private static void assertHasItem(Player player, String itemName) {
        assertTrue(hasItem(player, itemName), player.getName() + " should own " + itemName);
    }

    private static final class ScriptedPlayer extends Player {
        private final List<String> choices;
        private final List<String> messages = new ArrayList<>();
        private int nextChoice;

        private ScriptedPlayer(String name, int coins, String... choices) {
            super(name, 200, 200, coins, new GameEventManager());
            this.choices = List.of(choices);
        }

        @Override
        public String userInput(String prompt) {
            return choices.get(nextChoice++);
        }

        @Override
        public void sendMessage(String message) {
            messages.add(message);
        }
    }
}
