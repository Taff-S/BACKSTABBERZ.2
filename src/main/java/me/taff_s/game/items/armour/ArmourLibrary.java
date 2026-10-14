package me.taff_s.game.items.armour;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.taff_s.game.items.armour.Armour;

public class ArmourLibrary {

    //format: Name, Description, Price, Armour Value, Durability
    public static final Armour clothShirt = new Armour("Cloth Shirt", "Offers basically no protection", 5, 1, 10);
    public static final Armour leatherArmour = new Armour("Leather Armour", "A sturdy and lightweight bit of armour", 15, 2, 15);
    public static final Armour chainmailArmour = new Armour("Chainmail Armour", "A well crafted set comprised of a chain shirt and greaves", 35, 3, 20);
    public static final Armour ironArmour = new Armour("Iron Armour", "A durable set of iron armour", 70, 4, 25);
 
    // enemy armours (removed duplicate names)
    public static final Armour enemyClothShirt = new Armour("Slipshod Armour", "Bits and pieces of cobbled together into a fascimile of armour", 5, 2, 10);
    public static final Armour enemyLeatherArmour = new Armour("Basic Armour", "A practical set of dented but usable armour", 15, 3, 15);
    public static final Armour enemyChainmailArmour = new Armour("Full Armour", "A helm, breastplate and greaves that look too good to be scavenged", 35, 4, 20);

    //list of armours
    private static final List<Armour> armours = Arrays.asList(
        clothShirt, leatherArmour, chainmailArmour, ironArmour
    );

    public static Armour getRandomArmour() {
        return armours.get(rand.nextInt(armours.size())).copy();
    }

    public static List<Armour> getAllArmours() {
        return armours.stream().map(Armour::copy).toList();
    }

    private ArmourLibrary() {}
}