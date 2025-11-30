import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ArmourLibrary {
    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    //format: Name, Description, Price, Armour Value
    public static final Armour clothShirt = new Armour("Cloth Shirt", "Offers basically no protection", 5, 1);
    public static final Armour leatherArmour = new Armour("Leather Armour", "A sturdy and lightweight bit of armour", 15, 2);
    public static final Armour chainmailArmour = new Armour("Chainmail Armour", "A well crafted set comprised of a chain shirt and greaves", 35, 3);
    public static final Armour ironArmour = new Armour("Iron Armour", "A durable set of iron armour", 70, 4);
 
    //enemy armours
    public static final Armour clothShirt = new Armour("Slipshod Armour", "Bits and pieces of cobbled together into a fascimile of armour", 5, 2);
    public static final Armour leatherArmour = new Armour("Basic Armour", "A practical set of dented but usable armour", 15, 3);
    public static final Armour chainmailArmour = new Armour("Full Armour", "A helm, breastplate and greaves that look too good to be scavenged", 35, 4);

    //list of armours
    private static final List<Armour> armours = Arrays.asList(
        clothShirt, leatherArmour, chainmailArmour, ironArmour
    );

    public static Armour getRandomArmour() {
        return armours.get(rand.nextInt(armours.size()));
    }

    public static List<Armour> getAllArmours() {
        return armours;
    }

    private ArmourLibrary() {}
}