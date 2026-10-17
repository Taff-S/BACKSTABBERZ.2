package me.taff_s.game.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.armour.ArmourLibrary;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponLibrary;
import me.taff_s.game.player.Player;

public class BlacksmithEncounter implements Encounter {

    private static final int BID_INCREMENT = 10;
    private static final List<Item> HIGH_VALUE_POOL = List.of(
        WeaponLibrary.executionerSword,
        WeaponLibrary.greatAxe,
        WeaponLibrary.twinBladeAxe,
        WeaponLibrary.crushingMace,
        WeaponLibrary.siegeHammer,
        WeaponLibrary.oldRoyalHalberd,
        WeaponLibrary.longBow,
        WeaponLibrary.rapier,
        WeaponLibrary.pike,
        ArmourLibrary.ironArmour,
        ArmourLibrary.chainmailArmour
    );

    private final Item item1;
    private final Item item2;

    public BlacksmithEncounter() {
        List<Item> pool = new ArrayList<>(HIGH_VALUE_POOL);
        Collections.shuffle(pool);
        item1 = copyEquipmentItem(pool.get(0));
        item2 = copyEquipmentItem(pool.get(1));
    }

    BlacksmithEncounter(Item item1, Item item2) {
        this.item1 = copyEquipmentItem(item1);
        this.item2 = copyEquipmentItem(item2);
    }

    private static Item copyEquipmentItem(Item item) {
        if (item instanceof Weapon) {
            return ((Weapon) item).copy();
        }
        if (item instanceof Armour) {
            return ((Armour) item).copy();
        }
        throw new IllegalArgumentException(
                "Unsupported blacksmith stock item: " + item.getClass().getName());
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
            Weapon weapon = (Weapon) item;
            durabilityInfo = " | Dmg: " + weapon.getDamage()
                    + " | Class: " + weapon.getWeaponClass()
                    + " | Uses: " + weapon.getMaxDurability();
        } else if (item instanceof Armour) {
            Armour armour = (Armour) item;
            durabilityInfo = " | Armour Val: "
                    + String.format("%.0f%%", (1 - armour.getArmourVal()) * 100)
                    + " reduction";
        }
        return "[" + number + "] " + item.getItemName()
            + " - " + item.getDescription()
            + durabilityInfo
            + " | Price: " + price + " gold";
    }

    private int getPrice(Item item) {
        return (int) (item.getPrice() * 2.5);
    }

    private Choice getChoice(Player player) {
        while (true) {
            String input = player.userInput("Which item do you want? (1 / 2 / 3: Leave)");
            if (input == null) {
                return null;
            }
            switch (input.trim()) {
                case "1":
                    return Choice.ITEM_ONE;
                case "2":
                    return Choice.ITEM_TWO;
                case "3":
                    return Choice.LEAVE;
                default:
                    player.sendMessage("Please enter 1, 2, or 3.");
            }
        }
    }

    public void interact(Player player1, Player player2) {
        displayIntro(player1);
        displayIntro(player2);
        player1.sendMessage(formatItem(1, item1));
        player1.sendMessage(formatItem(2, item2));
        player2.sendMessage(formatItem(1, item1));
        player2.sendMessage(formatItem(2, item2));

        Choice[] choices = collectChoices(player1, player2);
        if (choices == null) {
            return;
        }

        Choice choice1 = choices[0];
        Choice choice2 = choices[1];
        if (choice1 == Choice.LEAVE && choice2 == Choice.LEAVE) {
            player1.sendMessage("\"Suit yourselves. Come back when you've got gold and sense.\"");
            player2.sendMessage("\"Suit yourselves. Come back when you've got gold and sense.\"");
            return;
        }

        if (choice1 == choice2 && choice1 != Choice.LEAVE) {
            Item contested = itemFor(choice1);
            Item remaining = otherItem(choice1);
            player1.sendMessage("You both want the " + contested.getItemName() + ".");
            player2.sendMessage("You both want the " + contested.getItemName() + ".");

            AuctionResult result = conductBiddingWar(player1, player2, contested);
            if (result.aborted) {
                return;
            }
            if (result.winner != null) {
                offerRemainingItem(result.winner == player1 ? player2 : player1, remaining);
            }
        } else {
            purchaseChoice(player1, choice1);
            purchaseChoice(player2, choice2);
        }

        player1.sendMessage("\"Good doing business with you. Now get out of my forge.\"");
        player2.sendMessage("\"Good doing business with you. Now get out of my forge.\"");
    }

    private Choice[] collectChoices(Player player1, Player player2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<Choice> choice1 = executor.submit(() -> getChoice(player1));
            Future<Choice> choice2 = executor.submit(() -> getChoice(player2));
            Choice first = choice1.get();
            Choice second = choice2.get();
            if (first == null || second == null) {
                player1.sendMessage("The blacksmith encounter ends because a player disconnected.");
                player2.sendMessage("The blacksmith encounter ends because a player disconnected.");
                return null;
            }
            return new Choice[] {first, second};
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            player1.sendMessage("The blacksmith encounter was interrupted.");
            player2.sendMessage("The blacksmith encounter was interrupted.");
            return null;
        } catch (ExecutionException e) {
            player1.sendMessage("The blacksmith could not collect both choices: "
                    + e.getCause().getMessage());
            player2.sendMessage("The blacksmith could not collect both choices: "
                    + e.getCause().getMessage());
            return null;
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Player one opens at the asking price, which they already selected.
     * Player two can raise by one increment or fold; after a raise, the current
     * leader gets the same choice until one player folds or cannot afford a raise.
     */
    private AuctionResult conductBiddingWar(Player player1, Player player2, Item item) {
        int currentBid = getPrice(item);
        Player leader;
        Player challenger;

        if (player1.getCoins() < currentBid) {
            if (player2.getCoins() >= currentBid) {
                player1.sendMessage("You cannot afford the asking price. Your partner wins at "
                        + currentBid + " gold.");
                return awardAuctionItem(player2, item, currentBid);
            }
            player1.sendMessage("Neither of you can afford the asking price. The blacksmith keeps the item.");
            player2.sendMessage("Neither of you can afford the asking price. The blacksmith keeps the item.");
            return AuctionResult.completed(null);
        }

        if (player2.getCoins() < currentBid) {
            player2.sendMessage("You cannot afford the asking price. Your partner wins at "
                    + currentBid + " gold.");
            return awardAuctionItem(player1, item, currentBid);
        }

        leader = player1;
        challenger = player2;
        player1.sendMessage("You open at the asking price of " + currentBid + " gold.");
        player2.sendMessage("Starting bid: " + currentBid
                + " gold. Raise by " + BID_INCREMENT + " or fold.");

        while (true) {
            int raisedBid = currentBid + BID_INCREMENT;
            if (challenger.getCoins() < raisedBid) {
                challenger.sendMessage("You cannot afford the next bid of " + raisedBid
                        + " gold. Your partner wins at " + currentBid + " gold.");
                leader.sendMessage(challenger.getName()
                        + " cannot afford the next bid. You win at " + currentBid + " gold.");
                return awardAuctionItem(leader, item, currentBid);
            }

            String input = getBidChoice(challenger, leader, currentBid, raisedBid);
            if (input == null) {
                player1.sendMessage("The bidding war ends because a player disconnected.");
                player2.sendMessage("The bidding war ends because a player disconnected.");
                return AuctionResult.aborted();
            }
            if (input.equals("fold")) {
                challenger.sendMessage("You fold. " + leader.getName() + " wins for "
                        + currentBid + " gold.");
                leader.sendMessage(challenger.getName() + " folds. You win for "
                        + currentBid + " gold.");
                return awardAuctionItem(leader, item, currentBid);
            }

            currentBid = raisedBid;
            Player previousLeader = leader;
            leader = challenger;
            challenger = previousLeader;
            player1.sendMessage(leader.getName() + " raises the bid to " + currentBid + " gold.");
            player2.sendMessage(leader.getName() + " raises the bid to " + currentBid + " gold.");
        }
    }

    private String getBidChoice(Player bidder, Player leader, int currentBid, int raisedBid) {
        while (true) {
            String input = bidder.userInput("Current bid: " + currentBid + " gold. "
                    + "Your coins: " + bidder.getCoins() + ". Type 'bid' to raise to "
                    + raisedBid + " or 'fold' to withdraw.");
            if (input == null) {
                return null;
            }
            String normalized = input.trim().toLowerCase();
            if (normalized.equals("bid") || normalized.equals("fold")) {
                return normalized;
            }
            bidder.sendMessage("Invalid bid action. Type 'bid' or 'fold'.");
            leader.sendMessage(bidder.getName() + " is deciding whether to raise or fold.");
        }
    }

    private AuctionResult awardAuctionItem(Player winner, Item item, int price) {
        winner.coinChange(-price);
        winner.getInventory().addItem(item);
        winner.sendMessage("You win the " + item.getItemName() + " for " + price + " gold.");
        return AuctionResult.completed(winner);
    }

    private void offerRemainingItem(Player player, Item remaining) {
        player.sendMessage("The blacksmith offers the remaining item at its asking price.");
        player.sendMessage(formatItem(1, remaining));
        String input = player.userInput("Buy it for " + getPrice(remaining) + " gold? (yes / no)");
        if (input == null) {
            player.sendMessage("You leave the remaining item on the counter.");
            return;
        }
        if (input.trim().equalsIgnoreCase("yes")) {
            purchase(player, remaining);
        } else {
            player.sendMessage("You leave it on the counter.");
        }
    }

    private void purchaseChoice(Player player, Choice choice) {
        if (choice == Choice.LEAVE) {
            player.sendMessage("You decide to keep your coins.");
            return;
        }
        purchase(player, itemFor(choice));
    }

    private Item itemFor(Choice choice) {
        return choice == Choice.ITEM_ONE ? item1 : item2;
    }

    private Item otherItem(Choice choice) {
        return choice == Choice.ITEM_ONE ? item2 : item1;
    }

    private void purchase(Player player, Item item) {
        int price = getPrice(item);
        if (player.getCoins() < price) {
            player.sendMessage("\"" + price + " gold. You're short. Come back when you're worth it.\"");
            return;
        }
        player.coinChange(-price);
        player.getInventory().addItem(item);
        player.sendMessage("The blacksmith hands over the " + item.getItemName()
                + ". \"Fine choice.\"");
    }

    @Override
    public void execute(Player player1, Player player2) {
        interact(player1, player2);
    }

    private enum Choice {
        ITEM_ONE,
        ITEM_TWO,
        LEAVE
    }

    private static final class AuctionResult {
        private final Player winner;
        private final boolean aborted;

        private AuctionResult(Player winner, boolean aborted) {
            this.winner = winner;
            this.aborted = aborted;
        }

        private static AuctionResult completed(Player winner) {
            return new AuctionResult(winner, false);
        }

        private static AuctionResult aborted() {
            return new AuctionResult(null, true);
        }
    }
}
