package me.taff_s.game.items;

import java.util.List;
import java.util.Random;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.armour.ArmourLibrary;
import me.taff_s.game.items.armour.NoArmour;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.items.weapons.NoWeapon;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponLibrary;
import me.taff_s.game.items.weapons.WeaponClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class EquipmentCopyTest {

    @Test
    void weaponCopyPreservesPropertiesAndCurrentDurabilityIndependently() {
        Weapon original = new Weapon("Test blade", "A test weapon", 12, 25,
                WeaponClass.SWORD, DamageType.SLASH, 10);
        original.reduceDurability(3);

        Weapon copy = original.copy();

        assertNotSame(original, copy);
        assertEquals(original.getItemName(), copy.getItemName());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getPrice(), copy.getPrice());
        assertEquals(original.getDamage(), copy.getDamage());
        assertEquals(original.getWeaponClass(), copy.getWeaponClass());
        assertEquals(original.getDamageType(), copy.getDamageType());
        assertEquals(7, copy.getDurability());

        copy.reduceDurability(1);
        assertEquals(7, original.getDurability());
        assertEquals(6, copy.getDurability());
    }

    @Test
    void armourCopyPreservesPropertiesInAnIndependentInstance() {
        Armour original = new Armour("Test armour", "A test set", 20, 4, 8);
        original.reduceDurability(3);

        Armour copy = original.copy();

        assertNotSame(original, copy);
        assertEquals(original.getItemName(), copy.getItemName());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getPrice(), copy.getPrice());
        assertEquals(original.getArmourVal(), copy.getArmourVal());
        assertEquals(8, copy.getMaxDurability());
        assertEquals(5, copy.getDurability());

        copy.reduceDurability(1);
        assertEquals(5, original.getDurability());
        assertEquals(4, copy.getDurability());
    }

    @Test
    void copiesOfEmptyEquipmentKeepTheirSingletonInstances() {
        assertSame(NoWeapon.getInstance(), NoWeapon.getInstance().copy());
        assertSame(NoArmour.getInstance(), NoArmour.getInstance().copy());
    }

    @Test
    void weaponLibraryReturnsIndependentCopies() {
        Weapon first = WeaponLibrary.getRandomWeapon();
        Weapon second = WeaponLibrary.getRandomWeapon();

        assertNotSame(first, second);
        Weapon weighted = WeaponLibrary.getRandomWeightedShopWeapon(List.of(first), List.of(1), new Random(1));
        assertNotSame(first, weighted);

        Weapon listed = WeaponLibrary.getAllWeapons().get(0);
        listed.reduceDurability(1);
        assertEquals(listed.getMaxDurability(), WeaponLibrary.getAllWeapons().get(0).getDurability());
    }

    @Test
    void armourLibraryReturnsIndependentCopies() {
        Armour first = ArmourLibrary.getRandomArmour();
        Armour second = ArmourLibrary.getRandomArmour();

        assertNotSame(first, second);
        Armour listed = ArmourLibrary.getAllArmours().get(0);
        String templateName = listed.getItemName();
        listed.setArmourName("Changed copy");
        assertEquals(templateName, ArmourLibrary.getAllArmours().get(0).getItemName());
    }
}
