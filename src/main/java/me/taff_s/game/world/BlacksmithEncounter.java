package me.taff_s.game.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import me.taff_s.game.player.Player;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.armour.ArmourLibrary;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponLibrary;

public class BlacksmithEncounter implements Encounter {

    private static final int BID_INCREMENT = 10;

    // High-value items the blacksmith stocks
    private static final List<Item> HIGH_VALUE_POOL = List.of(
        WeaponLibrary.executionerSword,
        WeaponLibrary.greatAxe,
        WeaponLibrary.twinBladeAxe,
        WeaponLibrary.crushingMace,
        WeaponLibrary.siegeHammer,
        WeaponLibrary.oldRoyalHalberd,
        WeaponLibrary.longBow,
        WeaponLibrary.rapier,
        //WeaponLibrary.scythe,
        WeaponLibrary.pike,
        ArmourLibrary.ironArmour,
        ArmourLibrary.chainmailArmour
    );

    private Item item1;
    private Item item2;

    public BlacksmithEncounter() {
        List<Item> pool = new ArrayList<>(HIGH_VALUE_POOL);
        java.util.Collections.shuffle(pool);
        this.item1 = pool.get(0);
        this.item2 = pool.get(1);
    }

    private void displayIntro(Player player) {
        player.sendMessage(
            "You push open a heavy iron door and are hit with a wall of heat. A forge blazes in the corner, "
            + "illuminating the silhouette of a broad-shouldered figure hammering rhythmically at an anvil.\n"
            + "The blacksmith looks up, squinting through the smoke.\n"
            + "\"Adventurers? Hah. You've got that desperate look about you. Good — I've got just the thing.\"\n"
            + "He gestures to two items resting on a worn wooden counter, still warm from the forge.\n"
        );
    }

    private String formatItem(int number, Item item) {
        int price = getPrice(item);
        String durabilityInfo = "";
        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            durabilityInfo = " | Dmg: " + w.getDamage() + " | Class: " + w.getWeaponClass() + " | Uses: " + w.getMaxDurability();
        } else if (item instanceof Armour) {
            Armour a = (Armour) item;
            durabilityInfo = " | Armour Val: " + String.format("%.0f%%", (1 - a.getArmourVal()) * 100) + " reduction";
        }
        return "[" + number + "] " + item.getItemName()
            + " — " + item.getDescription()
            + durabilityInfo
            + " | Price: " + price + " gold";
    }

    private int getPrice(Item item) {
        // Mark up the base price significantly for the blacksmith
        return (int) (item.getPrice() * 2.5);
    }

    private String getChoice(Player player, String prompt) {
        while (true) {
            String input = player.userInput(prompt).trim();
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return input;
            }
            player.sendMessage("Please enter 1, 2, or 3.");
        }
    }

    public void interact(Player player1, Player player2) {
        displayIntro(player1);
        displayIntro(player2);

        player1.sendMessage(formatItem(1, item1));
        player1.sendMessage(formatItem(2, item2));
        player2.sendMessage(formatItem(1, item1));
        player2.sendMessage(formatItem(2, item2));

        // Collect choices simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> choice1Future = executor.submit(() ->
            getChoice(player1, "Which item do you want? (1 / 2 / 3: Leave)")
        );
        Future<String> choice2Future = executor.submit(() ->
            getChoice(player2, "Which item do you want? (1 / 2 / 3: Leave)")
        );

        String choice1, choice2;
        try {
            choice1 = choice1Future.get();
            choice2 = choice2Future.get();
        } catch (InterruptedException | ExecutionException e) {
            player1.sendMessage("Something went wrong at the blacksmith.");
            player2.sendMessage("Something went wrong at the blacksmith.");
            executor.shutdown();
            return;
        }
        executor.shutdown();

        // Both leave
        if (choice1.equals("3") && choice2.equals("3")) {
            player1.sendMessage("\"Suit yourselves. Come back when you've got gold and sense.\"");
            player2.sendMessage("\"Suit yourselves. Come back when you've got gold and sense.\"");
            return;
        }

        // Both want the same item
        if (choice1.equals(choice2) && !choice1.equals("3")) {
            Item contested = choice1.equals("1") ? item1 : item2;
            Item other = choice1.equals("1") ? item2 : item1;

            player1.sendMessage("You both want the " + contested.getItemName() + "...");
            player2.sendMessage("You both want the " + contested.getItemName() + "...");
            player1.sendMessage("\"Oh ho! A bidding war. I love it. Going up in " + BID_INCREMENT + "s from the asking price.\"");
            player2.sendMessage("\"Oh ho! A bidding war. I love it. Going up in " + BID_INCREMENT + "s from the asking price.\"");

            ////Item winner = conductBiddingWar(player1, player2, contested);

            // The loser can still buy the other item at standard price if they want
            ////Player loser = (winner == null) ? null :
            ////    (conductBiddingWar(player1, player2, contested) == null ? null : player1); // just get the non-winner
            // Simpler: track via bidding war return
            // (bidding war handles purchase internally, no extra logic needed here)

            // Offer the other item to both at normal price
            ////offerRemainingItem(player1, player2, other, winner == null ? null : winner);

        } else {
            // Different choices — clean transaction
            if (!choice1.equals("3")) {
                Item desired = choice1.equals("1") ? item1 : item2;
                purchase(player1, desired);
            } else {
                player1.sendMessage("You decide to keep your coins.");
            }

            if (!choice2.equals("3")) {
                Item desired = choice2.equals("1") ? item1 : item2;
                purchase(player2, desired);
            } else {
                player2.sendMessage("You decide to keep your coins.");
            }
        }

        player1.sendMessage("\"Good doing business with you. Now get out of my forge.\"");
        player2.sendMessage("\"Good doing business with you. Now get out of my forge.\"");
    }

    /**
     * Runs a bidding war between two players over a contested item.
     * Returns the winning player, or null if both withdraw.
     */
    private Player conductBiddingWar(Player player1, Player player2, Item item) {
        int currentBid = getPrice(item);
        Player[] players = {player1, player2};
        boolean[] stillBidding = {true, true};

        player1.sendMessage("Starting bid: " + currentBid + " gold. Type 'bid' to raise by " + BID_INCREMENT + ", or 'fold' to withdraw.");
        player2.sendMessage("Starting bid: " + currentBid + " gold. Type 'bid' to raise by " + BID_INCREMENT + ", or 'fold' to withdraw.");

        // Alternate turns until one player folds or can't afford
        int turn = 0;
        while (stillBidding[0] && stillBidding[1]) {
            int bidderIndex = turn % 2;
            int otherIndex = 1 - bidderIndex;
            Player bidder = players[bidderIndex];
            Player other = players[otherIndex];

            if (bidder.getCoins() < currentBid) {
                bidder.sendMessage("You can't afford " + currentBid + " gold. You're out.");
                other.sendMessage(bidder.getName() + " can't afford the bid. You win!");
                stillBidding[bidderIndex] = false;
                break;
            }

            bidder.sendMessage("Current bid: " + currentBid + " gold. Your gold: " + bidder.getCoins() + ". (bid / fold)");
            other.sendMessage("Waiting for " + bidder.getName() + " to bid...");

            String input = bidder.userInput("> ").trim().toLowerCase();

            if (input.equals("fold")) {
                bidder.sendMessage("You fold. " + other.getName() + " wins the bid!");
                other.sendMessage(bidder.getName() + " folded! The " + item.getItemName() + " is yours for " + currentBid + " gold.");
                stillBidding[bidderIndex] = false;
                if (other.getCoins() >= currentBid) {
                    other.getInventory().addItem(item);
                    other.coinChange(-currentBid);
                } else {
                    other.sendMessage("But you can't afford it either! The blacksmith shakes his head.");
                    return null;
                }
                return other;
            } else if (input.equals("bid")) {
                currentBid += BID_INCREMENT;
                bidder.sendMessage("You raise the bid to " + currentBid + "!");
                other.sendMessage(bidder.getName() + " raised the bid to " + currentBid + "!");
            } else {
                bidder.sendMessage("Invalid input — type 'bid' or 'fold'.");
                continue; // don't advance turn
            }

            turn++;
        }

        // If both dropped out somehow
        player1.sendMessage("\"Neither of you? Fine. I'll keep it.\"");
        player2.sendMessage("\"Neither of you? Fine. I'll keep it.\"");
        return null;
    }

    /**
     * Offers the leftover item to both players after a bidding war.
     * The bidding war winner is excluded (they already bought something).
     */
    private void offerRemainingItem(Player player1, Player player2, Item remaining, Player biddingWinner) {
        Player[] players = {player1, player2};
        for (Player p : players) {
            if (p == biddingWinner) continue;
            int price = getPrice(remaining);
            p.sendMessage("The blacksmith gestures to the " + remaining.getItemName() + " still on the counter.");
            p.sendMessage(formatItem(1, remaining));
            String input = p.userInput("Would you like to buy it? (yes / no)").trim().toLowerCase();
            if (input.equals("yes")) {
                purchase(p, remaining);
                return; // only one can buy it
            } else {
                p.sendMessage("You leave it on the counter.");
            }
        }
    }

    private void purchase(Player player, Item item) {
        int price = getPrice(item);
        if (player.getCoins() >= price) {
            player.getInventory().addItem(item);
            player.coinChange(-price);
            player.sendMessage("The blacksmith hands over the " + item.getItemName() + ". \"Fine choice.\"");
        } else {
            player.sendMessage("\"" + price + " gold. You're short. Come back when you're worth it.\"");
        }
    }

    @Override
    public void execute(Player player1, Player player2) {
        interact(player1, player2);
    }
}