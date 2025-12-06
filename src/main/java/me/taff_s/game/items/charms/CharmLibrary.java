package me.taff_s.game.items.charms;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.items.charms.NeedleCharm;
import me.taff_s.game.items.charms.RockCharm;
import me.taff_s.game.items.charms.BackStabberCharm;
import me.taff_s.game.items.charms.FangCharm;
import me.taff_s.game.items.charms.ShieldCharm;

public class CharmLibrary {
    private static final Random rand = new Random();
    
    public static final Charm NEEDLE = new NeedleCharm();
    public static final Charm ROCK = new RockCharm();
    public static final Charm BACKSTABBER = new BackStabberCharm();
    public static final Charm FANG = new FangCharm();
    public static final Charm SHIELD = new ShieldCharm();
    

    private static final List<Charm> charms = Arrays.asList(
        NEEDLE, ROCK, BACKSTABBER, FANG, SHIELD
    );

    public static Charm getRandomCharm() {
        return charms.get(rand.nextInt(charms.size()));
    }

    public static List<Charm> getAllCharms() {
        return charms;
    }

    private CharmLibrary() {
    }

}