package me.taff_s.game.enemies;

import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnemyFactoryTest {

    private static final Set<String> SPAWNABLE_ENEMIES = Set.of(
        "skeleton", "goblin", "kobold", "thief", "slime"
    );

    @Test
    void everySpawnPoolEnemyCanBeCreated() {
        for (String enemyName : SPAWNABLE_ENEMIES) {
            Enemy enemy = EnemyFactory.create(enemyName);
            assertNotNull(enemy);
            assertTrue(enemy.getHealth() > 0);
        }
    }

    @Test
    void randomEnemySpawnsUseRegisteredNames() {
        for (int i = 0; i < 500; i++) {
            String enemyName = EnemyFactory.getRandomEnemyName().toLowerCase();
            assertTrue(SPAWNABLE_ENEMIES.contains(enemyName));
        }
    }

    @Test
    void supportedVariantsCanBeSpawned() {
        assertNotNull(EnemyFactory.create("goblin", "armoured"));
        assertNotNull(EnemyFactory.create("goblin", "sword"));
        assertNotNull(EnemyFactory.create("skeleton", "bow"));
        assertNotNull(EnemyFactory.create("skeleton", "armoured"));
        assertNotNull(EnemyFactory.create("slime", "acid"));
        assertNotNull(EnemyFactory.create("slime", "stone"));
        assertNotNull(EnemyFactory.create("kobold", "flying"));
        assertNotNull(EnemyFactory.create("kobold", "spear"));
    }
}
