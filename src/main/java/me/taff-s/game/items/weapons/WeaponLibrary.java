import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WeaponLibrary {
    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    //format: Name, Description, Price, Damage, Class, DamageType, Durability
    
    public static final Weapon rustyKnife = new Weapon("Rusty Knife", "Barely better than your fists", 3, 1, WeaponClass.SWORD, DamageType.SLASH, 7);
    public static final Weapon butterKnife = new Weapon("Butter Knife", "A dull butterknife", 5, 1, WeaponClass.SWORD, DamageType.SLASH, 10);
    public static final Weapon practiceSword = new Weapon("Practice Sword", "A wooden sword used for training", 8, 2, WeaponClass.SWORD, DamageType.SLASH, 12);

    public static final Weapon ironSword = new Weapon("Iron Sword", "A sturdy blade made of iron. Nothing too fancy", 25, 3, WeaponClass.SWORD, DamageType.SLASH, 15);
    public static final Weapon curvedBlade = new Weapon("Curved Blade", "A finely honed curved sword", 30, 4, WeaponClass.SWORD, DamageType.SLASH, 18);
    public static final Weapon longSword = new Weapon("Longsword", "A heavier sword with greater reach", 35, 5, WeaponClass.SWORD, DamageType.SLASH, 20);
    
    public static final Weapon oldRoyalBlade = new Weapon("Old Royal Blade", "An ornate sword once wielded by a noble", 55, 6, WeaponClass.SWORD, DamageType.SLASH, 7);
    public static final Weapon executionerSword = new Weapon("Executioner's Sword", "Heavy and brutal, meant to end fights fast", 60, 7, WeaponClass.SWORD, DamageType.SLASH, 24);



    public static final Weapon stoneAxe = new Weapon("Stone Axe", "A shoddily crafted axe", 5, 1, WeaponClass.AXE, DamageType.SLASH,7);
    public static final Weapon hatchet = new Weapon("Hatchet", "A small axe better suited for trees than enemies", 7, 2, WeaponClass.AXE, DamageType.SLASH,10);
    public static final Weapon brokenCleaver = new Weapon("Broken Cleaver", "Still sharp enough to hurt", 10, 2, WeaponClass.AXE, DamageType.SLASH,5);

    public static final Weapon ironAxe = new Weapon("Iron Axe", "A sharpened and reliable iron axe", 25, 3, WeaponClass.AXE, DamageType.SLASH,15);
    public static final Weapon battleAxe = new Weapon("Battle Axe", "A weighty axe meant for war", 30, 4, WeaponClass.AXE, DamageType.SLASH,18);
    public static final Weapon boneSplitter = new Weapon("Bone Splitter", "Heavy enough to crack bone with ease", 35, 5, WeaponClass.AXE, DamageType.SLASH,20);
   
    public static final Weapon greatAxe = new Weapon("Great Axe", "An imposing axe with a huge, lethal head", 50, 6, WeaponClass.AXE, DamageType.SLASH,22);
    public static final Weapon twinBladeAxe = new Weapon("Twinblade Axe", "Double-edged and doubly dangerous", 55, 7, WeaponClass.AXE, DamageType.SLASH,24);
    


    public static final Weapon woodClub = new Weapon("Wooden Club", "Not much better than a stick", 5, 1, WeaponClass.BLUNT, DamageType.FORCE, 5);
    public static final Weapon plank = new Weapon("Wooden Plank", "Still has a few nails in it", 7, 1, WeaponClass.BLUNT, DamageType.FORCE, 7);
    public static final Weapon rustedPipe = new Weapon("Rusted Pipe", "Looks like it was part of some plumbing", 9, 2, WeaponClass.BLUNT, DamageType.FORCE, 5);
    
    public static final Weapon mace = new Weapon("Mace", "An old but robust mace", 25, 3, WeaponClass.BLUNT, DamageType.FORCE, 20);
    public static final Weapon ironFlail = new Weapon("Iron Flail", "A spiked ball chained to a stick", 30, 4, WeaponClass.BLUNT, DamageType.FORCE, 24);
    public static final Weapon spikedClub = new Weapon("Spiked Club", "Rudely embedded with metal shards", 33, 5, WeaponClass.BLUNT, DamageType.FORCE, 24);
    
    public static final Weapon morningStar = new Weapon("Morningstar", "A beautifully crafted iron sphere on a stick", 50, 6, WeaponClass.BLUNT, DamageType.FORCE, 26);
    public static final Weapon crushingMace = new Weapon("Crushing Mace", "Thick, dense, and hard to lift", 55, 7, WeaponClass.BLUNT, DamageType.FORCE,28);
    


    public static final Weapon rockHammer = new Weapon("Rock Hammer", "Quite literally a rock on a stick", 5, 1, WeaponClass.HAMMER, DamageType.FORCE,5);
    public static final Weapon tinkerHammer = new Weapon("Tinkerer's Hammer", "More tool than weapon", 7, 1, WeaponClass.HAMMER, DamageType.FORCE, 15);
    public static final Weapon hammer = new Weapon("Hammer", "A hammer with a heavy steel head", 25, 2, WeaponClass.HAMMER, DamageType.FORCE, 18);
    
    public static final Weapon smithHammer = new Weapon("Smith's Hammer", "Repurposed for war", 30, 3, WeaponClass.HAMMER, DamageType.FORCE, 20);
    public static final Weapon warHammer = new Weapon("War Hammer", "A large iron hammer made to crush armour and concave skulls", 50, 4, WeaponClass.HAMMER, DamageType.FORCE, 24);
    public static final Weapon siegeHammer = new Weapon("Siege Hammer", "A giant hammer used to breach defenses", 60, 6, WeaponClass.HAMMER, DamageType.FORCE, 24);



    public static final Weapon woodSpear = new Weapon("Sharp Stick", "Better than a blunt one", 5, 1, WeaponClass.SPEAR, DamageType.PIERCE, 5);
    public static final Weapon bambooSpear = new Weapon("Bamboo Spear", "Flexible and fast", 8, 2, WeaponClass.SPEAR, DamageType.PIERCE, 20);
    public static final Weapon ironSpear = new Weapon("Iron Tipped Spear", "A steel tipped spear with a long shaft", 25, 3, WeaponClass.SPEAR, DamageType.PIERCE, 16);
    
    public static final Weapon guardLance = new Weapon("Guard Lance", "A weapon once used by a dungeon patrol", 30, 4, WeaponClass.SPEAR, DamageType.PIERCE, 18);
    public static final Weapon oldRoyalHalberd = new Weapon("Old Royal Halberd", "A well crafted polearm, with a spear's range and an axe's bite", 50, 6, WeaponClass.SPEAR, DamageType.PIERCE, 7);
    public static final Weapon pike = new Weapon("Pike", "Extremely long and hard to wield, but deadly", 55, 6, WeaponClass.SPEAR, DamageType.PIERCE, 24);



    public static final Weapon makeshiftBow = new Weapon("Makeshift Bow", "Strung with twine and hope", 7, 1, WeaponClass.BOW, DamageType.PIERCE, 7);
    public static final Weapon shortBow = new Weapon("ShortBow", "A shoddy shortbow, alongside enough arrows to last till the dungeon ends", 5, 2, WeaponClass.BOW, DamageType.PIERCE, 9);
    public static final Weapon bow = new Weapon("Bow", "A sturdy bow with many bolts to fire", 25, 3, WeaponClass.BOW, DamageType.PIERCE, 18);
    
    public static final Weapon oldHuntingBow = new Weapon("Old Hunting Bow", "Used in forest hunts, many moons ago", 30, 4, WeaponClass.BOW, DamageType.PIERCE, 9);
    public static final Weapon warBow = new Weapon("War Bow", "A powerful bow that puts others to shame", 50, 5, WeaponClass.BOW, DamageType.PIERCE, 20);
    public static final Weapon longBow = new Weapon("Longbow", "Its range and power are unmatched", 55, 6, WeaponClass.BOW, DamageType.PIERCE, 26);



    public static final Weapon rapier = new Weapon("Rapier", "A shining, thin blade made of an alloy that gleams in darkest dungeon", 50, 4, WeaponClass.SWORD, DamageType.PIERCE,26);
    public static final Weapon crossBow = new Weapon("Repeating Crossbow", "A sturdy crossbow with many bolts to fire", 25, 2, WeaponClass.BOW, DamageType.PIERCE, 100);
    public static final Weapon scythe = new Weapon("Scythe", "A cresent moon blade on a gnarled wooden pole", 50, 3, WeaponClass.SCYTHE, DamageType.SLASH,26);



    //List of all weapons
    private static final List<Weapon> weapons = Arrays.asList(
        rustyKnife, butterKnife, practiceSword, ironSword, curvedBlade, longSword, oldRoyalBlade, executionerSword, 
        stoneAxe, hatchet, brokenCleaver, ironAxe, boneSplitter, greatAxe, twinBladeAxe,
         woodClub, plank, rustedPipe, mace, ironFlail, spikedClub, morningStar, crushingMace, 
         rockHammer, tinkerHammer, hammer, smithHammer, warHammer, siegeHammer, 
         woodSpear, bambooSpear, ironSpear, guardLance, oldRoyalHalberd, pike,
         makeshiftBow, shortBow, bow, oldHuntingBow, warBow, longBow
    );

    //Lists of weapons that can be found in shops
    private static final List<Weapon> shopWeapons = Arrays.asList(
        butterKnife, practiceSword, ironSword, executionerSword, 
        hatchet, ironAxe, 
        woodClub, mace, ironFlail, spikedClub, 
        tinkerHammer, hammer, smithHammer, warHammer,
        bambooSpear, ironSpear, guardLance,
        shortBow, bow, warBow
    );

    //Any random weapon
    public static Weapon getRandomWeapon() {
        return weapons.get(rand.nextInt(weapons.size()));
    }

    //Any random shop weapons
    public static Weapon getRandomShopWeapon() {
        return getRandomWeightedShopWeapon(shopWeapons, weights, rand);
    }

    public static Weapon getRandomWeightedShopWeapon(List<Weapon> weapons, List<Integer> weights, Random rand) {
        int totalWeight = 0;
        for (int w : weights) totalWeight += w;
    
        int r = rand.nextInt(totalWeight);
        int cumulative = 0;
    
        for (int i = 0; i < weights.size(); i++) {
            cumulative += weights.get(i);
            if (r < cumulative) {
                return weapons.get(i);
            }
        }
    
        return weapons.get(0);
    }

    //Weights for random shop items
    private static List<Integer> weights = Arrays.asList(
        8, 7, 5, 1,     // butterKnife, practiceSword, ironSword, executionerSword
        8, 5,           // hatchet, ironAxe
        8, 3, 3, 2,     // woodClub, mace, ironFlail, spikedClub
        8, 7, 2, 2,     // tinkerHammer, hammer, smithHammer, warHammer
        8, 5, 2,        // bambooSpear, ironSpear, guardLance
        8, 5, 3         // shortBow, bow, warBow
    );

    public static List<Weapon> getAllWeapons() {
        return weapons;
    }

    private WeaponLibrary() {}

}