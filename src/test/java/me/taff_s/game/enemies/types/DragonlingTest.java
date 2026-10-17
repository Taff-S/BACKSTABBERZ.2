package me.taff_s.game.enemies.types;

import me.taff_s.game.items.weapons.DamageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DragonlingTest {

    @Test
    void redDragonlingHasItsConfiguredStatsAndDamageModifiers() {
        RedDragonling dragonling = new RedDragonling();

        assertEquals("Red Dragonling", dragonling.getType());
        assertEquals(1, dragonling.getTier());
        assertEquals(120, dragonling.getHealth());

        dragonling.isHit(10, DamageType.FORCE);
        assertEquals(115, dragonling.getHealth());
        dragonling.isHit(10, DamageType.SLASH);
        assertEquals(95, dragonling.getHealth());
    }

    @Test
    void blueDragonlingHasItsConfiguredStatsAndDamageModifiers() {
        BlueDragonling dragonling = new BlueDragonling();

        assertEquals("Blue Dragonling", dragonling.getType());
        assertEquals(1, dragonling.getTier());
        assertEquals(150, dragonling.getHealth());

        dragonling.isHit(10, DamageType.SLASH);
        assertEquals(145, dragonling.getHealth());
        dragonling.isHit(10, DamageType.PIERCE);
        assertEquals(125, dragonling.getHealth());
    }

    @Test
    void dragonlingAttacksStayWithinConfiguredDamageRanges() {
        RedDragonling red = new RedDragonling();
        BlueDragonling blue = new BlueDragonling();

        for (int i = 0; i < 100; i++) {
            int redDamage = red.attack();
            int blueDamage = blue.attack();
            assertTrue(redDamage >= 35 && redDamage <= 50);
            assertTrue(blueDamage >= 20 && blueDamage <= 30);
        }
    }

    @Test
    void healingIsCappedAtMaximumHealth() {
        RedDragonling dragonling = new RedDragonling();
        dragonling.isHit(40, DamageType.PIERCE);

        dragonling.heal(100);

        assertEquals(120, dragonling.getHealth());
    }

    @Test
    void concessionEndsTheFightWithoutCountingAsAKill() {
        BlueDragonling dragonling = new BlueDragonling();

        assertFalse(dragonling.wasConceded());
        dragonling.concede();

        assertFalse(dragonling.living());
        assertTrue(dragonling.wasConceded());
    }
}
