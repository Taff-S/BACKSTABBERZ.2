package me.taff_s.game.world;
import me.taff_s.game.player.Player;

public class MimicEncounter {
    //Premise: Player 1 and 2 enter a room. A chest greets them and offers them conditional rewards
    //Works on game theory/prisoners dilemna. Players have the option to either reach for the chest or stay still
    //If both players reach for the chest, they both get a small reward but also take damage
    //If one player reaches for the chest and the other doesn't, the player who reached gets rewarded with money
    //If neither player reach they both get medium rewards

    public void displayIntro(Player player){
        player.sendMessage("You continue to the next room. There's a treasure chest in front of you. Before you two can argue about who saw it first, it slides open by itself, revealing rows upon rows of glistening white broken in two by a thick pink tongue. Before you can draw your weapons, the mimic speaks.\n\"Veeheehee! Gold for the two tempered? Or gold for the one greedy?\"\nThe mimic brokenly explains its game. The chest demands you both close your eyes and gives you the option to either keep your arm to your side or to reach forward into its gaping maw.");
    }

    public void interact(Player player1, Player player2) {
        displayIntro(player1);
        displayIntro(player2);

        boolean player1Reaches = false;
        boolean player2Reaches = false;

        // Get player 1's choice
        while (true) {
            String choice1 = player1.userInput("Player 1, do you reach for the chest? (yes/no) > ").trim().toLowerCase();
            if (choice1.equals("yes")) {
                player1Reaches = true;
                break;
            } else if (choice1.equals("no")) {
                break;
            } else {
                player1.sendMessage("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        // Get player 2's choice
        while (true) {
            String choice2 = player2.userInput("Player 2, do you reach for the chest? (yes/no) > ").trim().toLowerCase();
            if (choice2.equals("yes")) {
                player2Reaches = true;
                break;
            } else if (choice2.equals("no")) {
                break;
            } else {
                player2.sendMessage("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        // Determine outcomes based on choices
        if (player1Reaches && player2Reaches) {
            // Both players reach: small reward but take damage
            player1.sendMessage("\"GREEDY GREEDY GREEDY PAIR!\" \r\n" + //
                                "\r\n" + //
                                "The mimic bites down on you and your companion's hands, hard enough to break skin but soft enough to leave them hand intact. \r\n" + //
                                "\r\n" + //
                                "He spits out some coins, which you both hastily scoop off the ground. As you both walk out, the sound of laughing echoes all the way to the next room. ");
            player2.sendMessage("\"GREEDY GREEDY GREEDY PAIR!\" \r\n" + //
                                "\r\n" + //
                                "The mimic bites down on you and your companion's hands, hard enough to break skin but soft enough to leave them hand intact. \r\n" + //
                                "\r\n" + //
                                "He spits out some coins, which you both hastily scoop off the ground. As you both walk out, the sound of laughing echoes all the way to the next room. ");
            // Implement reward and damage logic here
            player1.antiHeal(2);
            player2.antiHeal(2);
            player1.coinChange(5);
            player2.coinChange(5);
        } else if (player1Reaches && !player2Reaches) {
            // Player 1 reaches, Player 2 doesn't: Player 1 gets rewarded with money
            player1.sendMessage("\"GREEDY WINS\" \r\n" + //
                "\r\n" + //
                "The mimic coughs 70 coins from his throat onto you. The glob of coins sticks to you with glistening, viscous phlegm. \r\n" + //
                "\r\n" + //
                "You exit the chamber, feeling as smug as you are sticky");
            player2.sendMessage("\"GREEDY WINS\" \r\n" + //
                "\r\n" + //
                "The mimic coughs 70 coins from his throat onto your companion. The glob of coins sticks to them with glistening, viscous phlegm. \r\n" + //
                "\r\n" + //
                "You exit the chamber, resentful but at least not covered in mimic drool");
            player1.coinChange(70);
        } else if (!player1Reaches && player2Reaches) {
            // Player 2 reaches, Player 1 doesn't: Player 2 gets rewarded with money
            player1.sendMessage("\"GREEDY WINS\" \r\n" + //
                "\r\n" + //
                "The mimic coughs 70 coins from his throat onto your companion. The glob of coins sticks to them with glistening, viscous phlegm. \r\n" + //
                "\r\n" + //
                "You exit the chamber, resentful but at least not covered in mimic drool");
            player2.sendMessage("\"GREEDY WINS\" \r\n" + //
                "\r\n" + //
                "The mimic coughs 70 coins from his throat onto you. The glob of coins sticks to you with glistening, viscous phlegm. \r\n" + //
                "\r\n" + //
                "You exit the chamber, feeling as smug as you are sticky");
            player2.coinChange(70);
        } else {
            // Neither player reaches: both get medium rewards
            player1.sendMessage("\"GREEDY- oh. A pair of temperate? How rare in this conniving world. Go forth and prosper, friends, and may your restraint bring you much joy.\" \r\n" + //
                                "\r\n" + //
                                "The mimic retracts its tongue into its maw and closes up, making a noise you could only describe as wooden chewing. After a second, its tongue darts out again, a prehensile tentacle that places 30 coins you and your companions hands. ");
            player2.sendMessage("\"GREEDY- oh. A pair of temperate? How rare in this conniving world. Go forth and prosper, friends, and may your restraint bring you much joy.\" \r\n" + //
                                "\r\n" + //
                                "The mimic retracts its tongue into its maw and closes up, making a noise you could only describe as wooden chewing. After a second, its tongue darts out again, a prehensile tentacle that places 30 coins you and your companions hands. ");
            player1.coinChange(30);
            player2.coinChange(30);
        }
    }



}