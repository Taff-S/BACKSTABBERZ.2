package me.taff_s.game.items.potions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.taff_s.game.items.potions.Potion;
import me.taff_s.game.items.potions.HealingEffect;
import me.taff_s.game.items.potions.BasePotionEffect;
import me.taff_s.game.items.potions.StrengthEffect;

public class PotionLibrary {
    private static final Random rand = new Random();
    
    public static final Potion LESSER_HEAL = new Potion(
        "Lesser Healing Potion", "Heals the player a bit",  15,
        () -> new HealingEffect(new BasePotionEffect(), 3)
    );

    public static final Potion HEAL = new Potion(
        "Healing Potion", "Heals the player",  25,
        () -> new HealingEffect(new BasePotionEffect(), 7)
    );

    public static final Potion GREATER_HEAL = new Potion(
        "Greater Healing Potion", "Heals the player a lot",  45,
        () -> new HealingEffect(new BasePotionEffect(), 12)
    );

    public static final Potion LESSER_STRENGTH = new Potion(
        "Lesser Strength Potion", "Strengthens the player a bit", 10,
        () -> new StrengthEffect(new BasePotionEffect(), 1, 5)
    );

    public static final Potion STRENGTH = new Potion(
        "Strength Potion", "Strengthens the player", 45,
        () -> new StrengthEffect(new BasePotionEffect(), 2, 5)
    );

    public static final Potion GREATER_STRENGTH = new Potion(
        "Greater Strength Potion", "Strengthens the player a lot", 65,
        () -> new StrengthEffect(new BasePotionEffect(), 3, 5)
    );    

        
    private static final List<Potion> healingPotions = Arrays.asList(
        LESSER_HEAL, HEAL, GREATER_HEAL
    );

    private static final List<Potion> strengthPotions = Arrays.asList(
        LESSER_STRENGTH, STRENGTH, GREATER_STRENGTH
    );

    public static Potion getRandomHealingPotion() {
        return healingPotions.get(rand.nextInt(healingPotions.size()));
    }

    public static Potion getRandomStrengthPotion() {
        return strengthPotions.get(rand.nextInt(strengthPotions.size()));
    }

    public static List<Potion> getAllHealingPotions() {
        return healingPotions;
    }

    public static List<Potion> getAllStrengthPotions() {
        return strengthPotions;
    }

}
