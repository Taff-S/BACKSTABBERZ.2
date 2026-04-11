package me.taff_s.game.world;
import me.taff_s.game.player.Player;

public class VQueenEncounter implements Encounter{
    //Premise: Randomly, one player is taken hostage by a vampire.
    //The other player has the option to either save their friend by paying, or let them have their health drained
    
    public void displayIntro(Player hostage, Player other){
        hostage.getHandler().send("As you enter the next room, the doors slam shut behind you leaving you stuck in the dim, half-lit chamber. A chilling laugh echoes through the chamber.");
        hostage.getHandler().sendMulti("You edge your way forward but you trip over the uneven flooring.",
        "Something cold but soft stops you from clattering to the floor. The brief relief you feel turns to dread once you see what - or who - has caught you.",
        "\"Ureeheeheehee\", the Vampire Queen laughs as she wraps a leathery bat-like wing around you, \"A willing hostage, that\'s lovely. You!\"",
        "She addresses your partner as she lifts your neck closer to her mouth.",
        "\"Your money or this poor soul\'s blood. Your choice\"",
        "You gulp as you await your partner's response, the vampire queen squeezing you tighter as if to emphasize the point."
        ); 
        other.getHandler().send("As you enter the next room, the doors slam shut behind youleaving you stuck in the dim, half-lit chamber. A chilling laugh echoes through the chamber.");
        other.getHandler().sendMulti("You begin walking cautiously ahead, your partner just in front of you when all of a sudden, it happens.",
        "\"Ureeheeheehee.\" Before you can even react, your companion is trapped in the clutches of a tall, pale vamipre, whose wings take up half the room by themselves.",
        "\"A willing hostage, that\'s lovely. You!\", she barks at you, with an undead grin, \"Your money or this poor soul\'s blood.\"",
        "Well at least they're a succinct vampire. You finger 40 gold in your pocket and consider your next move..."
        );
    }

    
    public void interact(Player player1, Player player2){
        //get random player to be taken hostage
        Player hostage = Math.random() < 0.5 ? player1 : player2;
        Player other = hostage == player1 ? player2 : player1;
        displayIntro(hostage, other);

        boolean validChoice = false;
        String choice = "";
        while (validChoice == false) {
            String choice1 = other.userInput("Pay 40 coins to save your friend? (yes) or (no)?").trim().toLowerCase();
            if (choice1.equals("yes")) {
                choice = choice1;
                validChoice = true;
            } else if (choice1.equals("no")) {
                choice = choice1;
                validChoice = true;
            } else {
                other.sendMessage("Invalid input. Please type 'yes' or 'no'.");
            }
        }


        if (choice == "yes") {
            other.getHandler().sendMulti("You hand over 40 gold coins to the vampire queen. She cackles with glee as she releases your friend, who collapses to the floor, trembling but unharmed.",
            "\"Aww, what a lovely kinship,\" she says snatching the coins from your hands. \"A word of advice though. Kinship only gets you so far. Now, out of my sight.\"",
            "You both quickly exit the room, eager to put as much distance between you and the vampire queen as possible.");
            hostage.getHandler().sendMulti("Relief washes over you as your friend holds their money out to the queen. You are released from her grasp, trembling but physically no worse for wear.",
            "\"Aww, what a lovely kinship,\" she says snatching the coins from your hands. \"A word of advice though. Kinship only gets you so far. Now, out of my sight.\"",
            "You both quickly exit the room, eager to put as much distance between you and the vampire queen as possible."
            );
            other.coinChange(-40);
        } else {
            other.getHandler().sendMulti("You shamelessly turn your head away from your friend\'s plight and hold onto your money.",
                "\"Ureeheeheehee!\", The vampire queen looks to your companion with a vicious, euphoric grin. \"Well, you heard them, morsel!\"",
                "She wastes no time sinking her teeth into your companion. The next minute is long and awkward for you, as you can\'t help but listen to the wet, squelching sounds of your friend\'s blood being drained.",
                "Finally, the vampire queen pulls away, licking her lips with satisfaction.",
                "\"Ooh, so rich.\", she says lethargically, brushing a runaway rivulet of blood off her cheek. The winged woman lets the limp body of your friend drop to the floor",
                "\"Now, out of my sight, before I get a free sample of you too.\"",
                "You hastily help your companion off the ground and you both exit the room, eager to put as much distance between you and the vampire queen as possible.");
            hostage.getHandler().sendMulti("Your companion shamelessly turns their head away from your plight and holds onto their money.",
                "\"Ureeheeheehee!\", The vampire queen looks to you with a grin. You think for a brief moment you see something else in her eyes. Regret? Resignation?. \"Well, you heard them, morsel.\"",
                "She wastes no time sinking her teeth into you. Pain and heat blossom in your neck as she digs in on your flesh. You feek your life force ebb out of you and into the dark undead.",
                "Finally, the vampire queen pulls away, licking her lips with satisfaction.",
                "\"Ooh, so rich.\", she says lethargically, brushing a runaway rivulet of blood off her cheek. She drops you unceremoniously on the ground, your vision swimming as you struggle to stay conscious.",
                "\"Now, out of my sight, before I get a free sample of you too.\"",
                "Your \'companion\' helps you to your feet, avoiding any eye contact as you both wordlessly exit the room.",
                "You lose 3 health"
            );
            hostage.antiHeal(3);
        } 

    }

    @Override
    public void execute(Player player1, Player player2) {
        interact(player1, player2);
    }

}