package me.taff_s.game.combat;

import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.enemies.EnemyFactory;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponClass;
import me.taff_s.game.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatAndPlayerTest {

    @Test
    void hitReducesEnemyHealthByDamageAndAppliesDamageModifier() {
        Enemy enemy = EnemyFactory.create("goblin");

        enemy.isHit(10, DamageType.SLASH);
        assertEquals(70, enemy.getHealth());

        enemy.setDamageModifier(DamageType.FORCE, 0.5);
        enemy.isHit(11, DamageType.FORCE);
        assertEquals(64, enemy.getHealth());
    }

    @Test
    void hitBreaksEnemyArmourWithoutOverflowAndBoostsDamageAfterwards() {
        Enemy enemy = EnemyFactory.create("goblin", "armoured");
        int initialHealth = enemy.getHealth();

        enemy.isHit(enemy.getMaxArmourHealth(), DamageType.SLASH);
        assertTrue(enemy.getArmourBroken());
        assertEquals(initialHealth, enemy.getHealth());

        enemy.isHit(10, DamageType.SLASH);
        assertEquals(initialHealth - 13, enemy.getHealth());
    }

    @Test
    void combatAttackDamagesEnemyAndReducesWeaponDurability() {
        Player player = new Player("Tester", 200, 200, 0, new GameEventManager());
        Enemy enemy = EnemyFactory.create("goblin");
        Weapon weapon = new Weapon("Test bow", "Test weapon", 0, 10, WeaponClass.BOW, DamageType.PIERCE, 3);
        weapon.setDurability(3);
        player.getEquipment().setWeapon(weapon);

        CombatResult result = CombatSystem.performAttack(player, enemy);

        assertEquals(70, enemy.getHealth());
        assertEquals(2, weapon.getDurability());
        assertFalse(result.isEnemyDefeated());
    }

    @Test
    void healAndAntiHealRespectHealthBounds() {
        Player player = new Player("Tester", 50, 100, 0, new GameEventManager());

        player.heal(75);
        assertEquals(100, player.getHealth());

        player.antiHeal(25);
        assertEquals(75, player.getHealth());

        player.antiHeal(150);
        assertEquals(0, player.getHealth());
    }

    @Test
    void enemyAttackDamageIsWithinConfiguredRange() {
        Enemy enemy = EnemyFactory.create("goblin");

        for (int i = 0; i < 100; i++) {
            int damage = enemy.attack();
            assertTrue(damage >= 20 && damage <= 30);
        }
    }
}
