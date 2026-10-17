package me.taff_s.game.player;

import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.armour.NoArmour;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.items.weapons.NoWeapon;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEquipmentTest {

    @Test
    void equippingWeaponTransfersItFromInventoryAndKeepsReplacedWeapon() {
        Player player = newPlayer();
        Weapon previousWeapon = player.getEquipment().getEquippedWeapon();
        Weapon weapon = new Weapon("Test weapon", "Test", 0, 10,
                WeaponClass.SWORD, DamageType.SLASH, 5);
        player.getInventory().addItem(weapon);

        player.useItem(weapon);

        assertSame(weapon, player.getEquipment().getEquippedWeapon());
        assertFalse(player.getInventory().getItems().contains(weapon));
        assertTrue(player.getInventory().getItems().contains(previousWeapon));

        weapon.unequip(player);

        assertSame(NoWeapon.getInstance(), player.getEquipment().getEquippedWeapon());
        assertEquals(1, player.getInventory().getItems().stream()
                .filter(item -> item == weapon).count());
    }

    @Test
    void equippingArmourTransfersItFromInventoryAndKeepsReplacedArmour() {
        Player player = newPlayer();
        Armour previousArmour = player.getEquipment().getEquippedArmour();
        Armour armour = new Armour("Test armour", "Test", 0, 4);
        player.getInventory().addItem(armour);

        player.useItem(armour);

        assertSame(armour, player.getEquipment().getEquippedArmour());
        assertFalse(player.getInventory().getItems().contains(armour));
        assertTrue(player.getInventory().getItems().contains(previousArmour));

        armour.unequip(player);

        assertSame(NoArmour.getInstance(), player.getEquipment().getEquippedArmour());
        assertEquals(1, player.getInventory().getItems().stream()
                .filter(item -> item == armour).count());
    }

    @Test
    void equippingCharmTransfersItFromInventoryAndUnequippingReturnsItOnce() {
        Player player = newPlayer();
        Charm charm = new Charm("Test charm", "Test", 0);
        player.getInventory().addItem(charm);

        player.useItem(charm);

        assertTrue(player.getEquipment().getCharm1() == charm
                || player.getEquipment().getCharm2() == charm);
        assertFalse(player.getInventory().getItems().contains(charm));

        charm.unequip(player);
        charm.unequip(player);

        assertNotSame(charm, player.getEquipment().getCharm1());
        assertNotSame(charm, player.getEquipment().getCharm2());
        assertEquals(1, player.getInventory().getItems().stream()
                .filter(item -> item == charm).count());
    }

    private static Player newPlayer() {
        return new Player("Tester", 100, 100, 0, new GameEventManager());
    }
}
