package me.taff_s.game.world;

import me.taff_s.game.player.Player;

public class BloodMachineEcounter implements Encounter{
    //Premise: Players enter a room with a machine.
    //They have the option to either give some health to gain money or give some money to gain health#
    
    public void displayIntro(Player player){
        player.sendMessage(
        "You continue to the next room and are relieved to see nothing but a machine inside of it. \n"
        + "A mouldy wooden sign hangs on the wall above it, reading out its purpose. \n"
        + "\"GIVE BLOOD, GET MONEY\"\n"
        + "\"GIVE MONEY, GET BLOOD\"\n"
        + "Well this seems rather self explanatory.\n"
        );

    }

    public void interact(Player player1, Player player2) {
        displayIntro(player1);
        displayIntro(player2);

        String player1choice = "";
        boolean validChoice1 = false;
        String player2choice = "";
        boolean validChoice2 = false;

        // Get player 1's choice
        while (validChoice1 == false) {
            String choice1 = player1.userInput("Player 1, What do you do?\n(1)Give Money \n(2)Give Blood \n(3)Do Nothing").trim().toLowerCase();
            if (choice1.equals("1") || choice1.equals("2") || choice1.equals("3")) {
                player1choice = choice1;
                validChoice1 = true;
            } else {
                player1.sendMessage("Invalid input. Please type '1', '2', or '3'.");
            }
        }

        // Get player 2's choice
        while (validChoice2 == false) {
            String choice2 = player2.userInput("Player 2, What do you do?\n (1)Give Money \n(2)Give Blood \n(3)Do Nothing").trim().toLowerCase();
            if (choice2.equals("1") || choice2.equals("2") || choice2.equals("3")) {
                player2choice = choice2;
                validChoice2 = true;
            } else {
                player2.sendMessage("Invalid input. Please type '1', '2', or '3'.\n");
            }
        }

        if (player1choice.equals("1")) {
            player1.coinChange(-30);
            player1.heal(3);
            player1.sendMessage("You go to the right side of the machine. A slot with a number above it opens itself "
            + "as if sensing your presence. You don't know what that number is, but you guess 3 is "
            + "probably the right amount.\n As soon as you deposit the last coin, a syringe darts out "
            + "from beneath the slot and jabs itself into your side.\n You feel shock, then relief as "
            + "your wounds begin to mend.\n\n"
            + "You lose 30 coins and gain 3 hearts\n"

            );
        } else if (player1choice.equals("2")) {
            player1.coinChange(45);
            player1.antiHeal(2);
            player1.sendMessage("You go to the left of the machine, where a syringe connected to the mechanical box "
            + "by a tube dangles in the air. You pull the syringe and the tube extends with it.\n "
            + "You waste no time, jabbing it into your arm and pulling up the plunger.\n\n"
            + "You lose 2 hearts and gain 45 coins\n"
            );
        } else {
            player1.sendMessage("You decide to do nothing and walk out of the room, waiting for your companion to do the same.\n");
        }

        if (player2choice.equals("1")) {
            player2.coinChange(-30);
            player2.heal(3);
            player2.sendMessage("You go to the right side of the machine. A slot with a number above it opens itself "
            + "as if sensing your presence. You don't know what that number is, but you guess 3 is "
            + "probably the right amount.\n As soon as you deposit the last coin, a syringe darts out "
            + "from beneath the slot and jabs itself into your side.\n You feel shock, then relief as "
            + "your wounds begin to mend.\n\n"
            + "You lose 30 coins and gain 3 hearts\n"

            );
        } else if (player2choice.equals("2")) {
            player2.coinChange(45);
            player2.antiHeal(2);
            player2.sendMessage("You go to the left of the machine, where a syringe connected to the mechanical box "
            + "by a tube dangles in the air. You pull the syringe and the tube extends with it.\n "
            + "You waste no time, jabbing it into your arm and pulling up the plunger.\n\n"
            + "You lose 2 hearts and gain 45 coins\n"
            );
        } else {
            player2.sendMessage("You decide to do nothing and walk out of the room, waiting for your companion to do the same.\n");
        }
    }  
    
    @Override
    public void execute(Player player1, Player player2) {
        interact(player1, player2);
    }
    
}