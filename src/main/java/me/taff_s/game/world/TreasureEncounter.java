package me.taff_s.game.world;

import java.util.Random;
import me.taff_s.game.player.Player;

public class TreasureEncounter implements Encounter {

    private static final int MAX_DISAGREEMENTS = 3;
    private static final int MIN_CHEST_COINS = 200;
    private static final int MAX_CHEST_COINS = 500;

    private final Random random;

    public TreasureEncounter() {
        this(new Random());
    }

    TreasureEncounter(Random random) {
        this.random = random;
    }

    public void interact(Player player1, Player player2) {
        int chestCoins = random.nextInt(MAX_CHEST_COINS - MIN_CHEST_COINS + 1)
                + MIN_CHEST_COINS;
        player1.sendMessage("You find a chest containing " + chestCoins + " coins.");
        player2.sendMessage("You find a chest containing " + chestCoins + " coins.");

        for (int disagreement = 0; disagreement < MAX_DISAGREEMENTS; disagreement++) {
            Choice firstChoice = getChoice(player1, "Choose how to divide the chest:");
            if (firstChoice == null) {
                player2.sendMessage("The treasure-room negotiation ends as your partner leaves.");
                return;
            }

            player2.sendMessage(player1.getName() + " chose: " + firstChoice.getLabel() + ".");
            Choice secondChoice = getChoice(player2,
                    player1.getName() + " chose " + firstChoice.getLabel()
                            + ". Choose an option; matching their choice settles the chest:");
            if (secondChoice == null) {
                player1.sendMessage("The treasure-room negotiation ends as your partner leaves.");
                return;
            }

            if (firstChoice == secondChoice) {
                resolveChoice(firstChoice, chestCoins, player1, player2);
                return;
            }

            int disagreementsUsed = disagreement + 1;
            player1.sendMessage(player2.getName() + " chose " + secondChoice.getLabel()
                    + ". You have disagreed " + disagreementsUsed + " of "
                    + MAX_DISAGREEMENTS + " times.");
            player2.sendMessage("You and " + player1.getName() + " chose different options. "
                    + "You have disagreed " + disagreementsUsed + " of "
                    + MAX_DISAGREEMENTS + " times.");
        }

        player1.sendMessage("A thief bursts into the room, snatches the entire chest, and escapes!");
        player2.sendMessage("A thief bursts into the room, snatches the entire chest, and escapes!");
    }

    @Override
    public void execute(Player player1, Player player2) {
        interact(player1, player2);
    }

    private Choice getChoice(Player player, String prompt) {
        String choiceText = prompt + "\n"
                + "[1] Wrestle partner - higher current HP takes all\n"
                + "[2] Share the contents 50/50\n"
                + "[3] Coin toss - winner takes all";
        while (true) {
            String input = player.userInput(choiceText);
            if (input == null) {
                return null;
            }
            switch (input.trim()) {
                case "1":
                    return Choice.WRESTLE;
                case "2":
                    return Choice.SHARE;
                case "3":
                    return Choice.COIN_TOSS;
                default:
                    player.sendMessage("Invalid choice. Enter 1, 2, or 3.");
            }
        }
    }

    private void resolveChoice(Choice choice, int chestCoins, Player player1, Player player2) {
        switch (choice) {
            case WRESTLE:
                if (player1.getHealth() > player2.getHealth()) {
                    awardAll(player1, player2, chestCoins);
                } else if (player2.getHealth() > player1.getHealth()) {
                    awardAll(player2, player1, chestCoins);
                } else {
                    player1.sendMessage("Neither of you has more HP, so you split the chest.");
                    player2.sendMessage("Neither of you has more HP, so you split the chest.");
                    share(chestCoins, player1, player2);
                }
                break;
            case SHARE:
                player1.sendMessage("You agree to split the chest evenly.");
                player2.sendMessage("You agree to split the chest evenly.");
                share(chestCoins, player1, player2);
                break;
            case COIN_TOSS:
                Player winner = random.nextBoolean() ? player1 : player2;
                Player loser = winner == player1 ? player2 : player1;
                winner.sendMessage("The coin lands in your favour. You take the whole chest!");
                loser.sendMessage("The coin lands in your partner's favour. They take the whole chest.");
                winner.coinChange(chestCoins);
                break;
            default:
                throw new IllegalStateException("Unsupported treasure choice: " + choice);
        }
    }

    private void awardAll(Player winner, Player loser, int chestCoins) {
        winner.sendMessage("Your higher current HP wins the wrestle. You take all "
                + chestCoins + " coins!");
        loser.sendMessage("Your partner's higher current HP wins the wrestle. They take all "
                + chestCoins + " coins.");
        winner.coinChange(chestCoins);
    }

    private void share(int chestCoins, Player player1, Player player2) {
        int player1Share = chestCoins / 2;
        int player2Share = chestCoins - player1Share;
        player1.sendMessage("You receive " + player1Share + " coins from the chest.");
        player2.sendMessage("You receive " + player2Share + " coins from the chest.");
        player1.coinChange(player1Share);
        player2.coinChange(player2Share);
    }

    private enum Choice {
        WRESTLE("Wrestle partner"),
        SHARE("Share the contents 50/50"),
        COIN_TOSS("Coin toss");

        private final String label;

        Choice(String label) {
            this.label = label;
        }

        private String getLabel() {
            return label;
        }
    }
}
