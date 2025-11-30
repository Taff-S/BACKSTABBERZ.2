public class DragonNarrator{
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static void startMonologue(Player player1, Player player2) {
        String monologue = YELLOW + "You shuffle forth into the main chamber of the Golden Dragon's den. It's hard to move as every inch of the floor is covered in gold coins, jewellery and trinkets. Piles and piles, like desert sand dunes reach high up, nearly meeting the stalactites hanging from the ceiling. You watch your step — a slip in mud might dirty your clothes, but here? You'd be lucky to walk away with just a concussion. You and your partner walk up the steepest hill of riches and prepare to meet your boss.\r\n" + //
        "\r\n" + //
        "\"Ah, Worm.\" The voice echoes within your head. Literally. The dragon uses magic to speak in your head, for a mere sigh or cough could leave both of you dead and charred. It's deep, brassy, and all too sonorous. \"And Worm Two. You know why I called you here.\"\r\n" + //
        "\r\n" + //
        "Before you lies the golden dragon, lounging on a plateau of melted silver coins. His eyes, half your height. His talons, freshly sharpened. His teeth, glinting menacingly.\r\n" + //
        "\r\n" + //
        "\"You've served me well enough insofar. But... Three is a crowd, don't you think? I only need one supervisor, you see,\" he mentally says, as you and your pal look at each other, wondering who's going to be dragon-supper. “So I've devised a little test for you. To see who's most fit for serving a being such as myself. I'm going to teleport you to the entrance of my dungeon. You will work your way back to me, collecting as much GOLD as possible.\" The word 'GOLD' bounces around your skull, echoing louder and louder, as if he's telepathically engraving it into your brain.\r\n" + //
        "\r\n" + //
        "\"I will teleport you to me if either of you get close enough to death, but I'd have to remove half the defeated party's coins as a separate tribute. Anyway, of the coins you bring back to, I'll count 75% toward your final score and take them for... Safekeeping. We shall do this gauntlet three times. Whoever brings me the most GOLD becomes my most treasured minion. Questions?\"\r\n" + //
        "\r\n" + //
        "You both can't bring yourselves to speak in the dragon's presence, but he looks at you, or rather through you. Evidently, the shining drake is peering into your minds and it's only a moment before he grins.\r\n" + //
        "\r\n" + //
        "\"What's this? You don't want to be a minion? How... delightfully insurrectionist of you... But I like that. Fine, I'll sweeten the pot. When you both make it back, I'll allow you to challenge me. How does that sound?\"\r\n" + //
        "\r\n" + //
        "The dragon reads either your mind or your face and chuckles. Even though he mercifully turned away his head, you feel the room's temperature rise.\r\n" + //
        "\r\n" + //
        "\"Enough. I wish you both the best. GOLD. Bring me GOLD.\"\r\n" + //
        "\r\n" + //
        "You clutch your weapons, ready your armour and before you can blink, you appear at the beginning of the dungeon.\r\n" + //
        "" + RESET;
        player1.sendMessage(monologue);
        player2.sendMessage(monologue);
    }

    public static void finalFightIntro(Player player1, Player player2) {
        StringBuilder intro = new StringBuilder();
    
        intro.append("The chamber rumbles as you step into the hoard once more.\n");
        intro.append("This time, there's no mistaking it — the piles of gold part like waves,\n");
        intro.append("revealing the Golden Dragon in full, lounging atop his throne of melted crowns and coin-paved pillows.\n\n");
    
        intro.append("He doesn't rise. He doesn't need to.\n");
        intro.append("Instead, his voice slips into your minds like silk over steel.\n\n");
    
        intro.append("\"Twelve rooms, two survivors, and a treasure trail that would make even my accountant weep.\"\n");
        intro.append("\"I must say - I'm impressed.\"\n\n");
    
        intro.append("The dragon stretches, lazily batting a goblet across the room with a talon.\n\n");
    
        intro.append("\"Most adventurers die somewhere around... room six. Room seven if they're annoyingly hopeful.\"\n");
        intro.append("\"But you - you stubborn little worms - actually made it.\"\n\n");
    
        intro.append("He leans closer. The air grows hotter, heavier. Not hostile — just expectant.\n\n");
    
        intro.append("\"Now, as promised, I'll allow you to challenge me. I'm *trembling* in anticipation. Really.\"\n\n");
    
        intro.append("A smirk coils into your thoughts like a serpent.\n\n");
    
        intro.append("\"Try not to embarrass yourselves. I polished the lair for this.\"\n\n");
    
        intro.append("The dragon's wings flare, gold fluttering like leaves in a storm.\n");
        intro.append("His grin glistens with casual cruelty.\n\n");
    
        intro.append("\"Come, champions of greed and grit... ");
        intro.append("**Have at thee!**\"\n");
    
        player1.sendMessage(YELLOW + intro.toString() + RESET);
        player2.sendMessage(YELLOW + intro.toString() + RESET);
    }


    public static void midScene(Player player1, Player player2, boolean isFinalRun) {
        int p1Total = DragonGameManager.getBankedGold(player1);
        int p2Total = DragonGameManager.getBankedGold(player2);
        
        Player highGold = p1Total >= p2Total ? player1 : player2;
        Player lowGold = highGold == player1 ? player2 : player1;
        
        // PlayerHandler highPH = highGold == player1 ? ph1 : ph2;
        // PlayerHandler lowPH = highGold == player1 ? ph2 : ph1;
    
        // Intro
        String intro1 = "You blink, and suddenly, you're no longer in the dungeon.\n"
            + "You're standing in the heart of the Golden Dragon's lair.\n"
            + "Still woozy from the teleportation, the Golden Dragon seems less a figure and more of a looming mountain of scales in front of you.\n";
            if (!player1.living() || !player2.living()){ 
            intro1 = intro1 +"\"Ah, you've returned,\" he says, \"Prematurely too. I wonder how that happened.\"\n"
            + "Your mind's eye depicts an upturned smile of razor blade teeth.\n"
            + "\"Oh well, I don't mind the specifics. Let us not waste any time.\"\n\n";
            }
            intro1 = intro1 + "\"Gold.\" You shuffle forward offering up the agreed 75% of your horde.\n"
            + "You daren't even think of cheating him. He can see right through you.\n";
    
        player1.sendMessage(YELLOW + intro1 + RESET);
        player2.sendMessage(YELLOW + intro1 + RESET);

        if (!isFinalRun && !DragonGameManager.isRunBanked()) {
            DragonGameManager.bankCoins(player1, player2);
            DragonGameManager.markRunBanked();
        }
        // Higher gold response
        String highGoldMsg = "\"Now that's lovely.\"\n"
            + "You feel the dragon's satisfaction on an unconscious level, like he's scratching an itch in your brain you could never reach.\n"
            + "\"That's my darling servant. Keep up at this rate, and you'll end up my closest and only advisor...\"\n";
        highGold.sendMessage(YELLOW + highGoldMsg + RESET);
    
        // Lower gold response
        String lowGoldMsg = "\"Hmmm.\"\n"
            + "It's cold. Hair raisingly, teeth chattering-ly, heart stoppingly cold.\n"
            + "\"Adequate. But 'Adequate' doesn't become a servant. 'Adequate' more so fits my snacks.\"\n";
        lowGold.sendMessage(YELLOW + lowGoldMsg + RESET);
    
        // Mid-run boon
        if (!isFinalRun) {
            String boon = "\"You both have satisfied me. One more than the other...\"\n"
                + "The dragon inhales slowly.\n"
                + "\"In my boundless grace and charity, I have decided to bestow a boon unto both of you.\"\n"
                + "The dragon turns around, revealing the spines that run down its back.\n"
                + "Although 'reveal' may not be the right word.\n"
                + "All of the dragon's spikes are absolutely engulfed in jewellery. It's hard to see where the scales begin and the regalia ends.\n"
                + "Necklaces, bracelets, gold chains — you swear there's even a crown or two.\n\n"
                + "Before you can say anything, two pieces slowly pull themselves off the wyrm's body and into your hands.\n"
                + "Compared to everything else, they seem rather inexpensive — just trinkets threaded with common string.\n"
                + "But you feel that there's something magical about them. (EQUIP CHARMS IN YOUR INVENTORY)\n";
    
            player1.sendMessage(YELLOW + boon + RESET);
            player2.sendMessage(YELLOW + boon + RESET);
            player1.getInventory().addItem(CharmLibrary.getRandomCharm());
            player2.getInventory().addItem(CharmLibrary.getRandomCharm());
    
        // Ending
        String ending = "\"Now,\" the dragon says, sitting back down without bothering to face you, "
            + "\"in my name, I allow you to continue your valiant crusade. I'll be waiting.\"\n";
    
        player1.sendMessage(YELLOW + ending + RESET);
        player2.sendMessage(YELLOW + ending + RESET);
    
        // Personalized threat
        String name = (lowGold == player1) ? "Worm One" : "Worm Two";
        String threat = "\"And " + name + "... Please don't disappoint me again... "
            + "And if you do, bring some salt back with you... I hear that worm goes down better seasoned...\"\n";
    
        lowGold.sendMessage(YELLOW + threat + RESET);
        }
    }

    public static void dragonVictory(Player player1, Player player2, boolean finalRun) {
        StringBuilder victory = new StringBuilder();
    
        victory.append("As you lie crumpled amidst the gold, wheezing and bruised, ");
        victory.append("the dragon casually strolls forward, each step shaking the chamber with idle power.\n\n");
    
        victory.append("\"Ah, balance has been restored,\" he muses, tilting his head at you like mildly disappointing pets.\n\n");
    
        victory.append("\"Don't feel too bad. You made it much further than most. And if it's any comfort...\"\n\n");
    
        victory.append("A smile blooms in your mind — sharp and radiant.\n\n");
    
        victory.append("\"You'll have plenty of time to reflect while doing inventory for the next century.\"\n\n");
    
        victory.append("\"Now... Coins. Rules are rules.\"\n");
    
        player1.sendMessage(YELLOW + victory.toString() + RESET);
        player2.sendMessage(YELLOW + victory.toString() + RESET);
    }
    
    public static void dragonDefeat(Player player1, Player player2) {
        StringBuilder defeat = new StringBuilder();
    
        defeat.append("The dragon stumbles.\n");
        defeat.append("Yes, stumbles. The ground doesn't shake but you do. ");
        defeat.append("From disbelief. From the adrenaline. From the moment finally catching up to you.\n\n");
    
        defeat.append("The Golden Dragon steadies himself, eyes wide. For a heartbeat, the chamber is silent. Then:\n\n");
    
        defeat.append("\"Well... huh.\"\n\n");
    
        defeat.append("A rumble rolls from his chest. Not a growl. A laugh.\n\n");
    
        defeat.append("\"I genuinely didn't see that coming. Worms biting back — how novel.\"\n\n");
    
        defeat.append("His wings fold slowly. He lowers himself to the ground, regal even in defeat.\n\n");
    
        defeat.append("\"Then I suppose... you win.\"\n\n");
    
        defeat.append("A beat. A gleam in his eye.\n\n");
    
        defeat.append("\"I'll need a vacation. And perhaps... a rematch.\"\n\n");
    
        defeat.append("The air begins to shimmer. The gold stirs around your feet. The dragon vanishes into a pulse of magic.\n\n");
    
        defeat.append("A single word echoes in your head —\n\n");
        defeat.append("\"Respect.\"\n\n");
    
        defeat.append("**YOU WIN.**\n");
    
        player1.sendMessage(YELLOW + defeat.toString() + RESET);
        player2.sendMessage(YELLOW + defeat.toString() + RESET);
    }

    public static void finalTally(Player winner, Player loser) {
        StringBuilder finalDialogue = new StringBuilder();
    
        finalDialogue.append("The dragon lounges atop his glimmering throne of molten coins and crushed crowns, ");
        finalDialogue.append("one claw lazily stirring a pile of treasure like soup.\n\n");
    
        finalDialogue.append("\"Well then,\" he purrs, \"the gauntlet comes to its end\"\n\n");
    
        finalDialogue.append("He raises a single talon. A column of gold rises from the ground in front of each of you, ");
        finalDialogue.append("shaped by unseen magic — two shimmering towers of your collective tribute.\n\n");
    
        finalDialogue.append("\"Let's see who kissed the coin harder...\"\n\n");
    
        finalDialogue.append("The towers begin to weigh themselves in mid-air, shifting and adjusting as if alive. ");
        finalDialogue.append("One side rises. The other dips.\n\n");
    
        finalDialogue.append("\"Ah. There it is.\"\n\n");
    
        finalDialogue.append("He turns his eyes to " + winner.getName() + " — glowing, golden, and far too large to be comforting.\n\n");
    
        finalDialogue.append("\"You, my radiant little worm, have proven your worth. ");
        finalDialogue.append("A coin-connoisseur. A hoarder of high regard. My new... favorite.\"\n\n");
    
        finalDialogue.append("Then, with a dismissive glance at " + loser.getName() + ":\n\n");
    
        finalDialogue.append("\"And you… Well. Every gold pile needs someone at the bottom. Better luck next century.\"\n\n");
    
        finalDialogue.append("He stretches, sending a few smaller gemstones tumbling down his haunches.\n\n");
    
        finalDialogue.append("\"As promised, the winner becomes my most treasured advisor. ");
        finalDialogue.append("The loser? Hmm. I'll find something... decorative. And my finest silverware.\"\n\n");
    
        finalDialogue.append("He grins.\n\n");
    
        finalDialogue.append("\"So, " + winner.getName() + "... Shall we begin your onboarding?\"\n\n");
    
        winner.sendMessage(YELLOW + finalDialogue.toString() + RESET);
        loser.sendMessage(YELLOW + finalDialogue.toString() + RESET);
        winner.sendMessage(BLUE + "**YOU WIN**" + RESET);
        loser.sendMessage(BLUE + "**YOU LOSE**" + RESET);
    }

}