import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EnemyFactory {

    private static final Map<String, Map<String, Supplier<Enemy>>> ENEMY_VARIANTS = Map.of(
        "goblin", Map.of(
            "standard", StandardGoblin::new,
            "armoured", ArmouredGoblin::new,
            "sword", SwordGoblin::new
        ),
        "skeleton", Map.of(
            "standard", StandardSkeleton::new,
            "bow", BowSkeleton::new,
            "armoured", ArmouredSkeleton::new
        ),
        "slime", Map.of(
            "standard", StandardSlime::new,
            "acid", AcidSlime::new,
            "stone", StoneSlime::new
        ),
        "kobold", Map.of(
            "standard", StandardKobold::new,
            "flying", FlyingKobold::new,
            "spear", SpearKobold::new
        )
    );
    
    private static final Map<String, Supplier<Enemy>> ENEMY_REGISTRY = Map.of(
    "skeleton", Skeleton::new,
    "goblin", Goblin::new,
    "kobold", Kobold::new,
    "thief", Thief::new,
    "slime", Slime::new
    );

    private static final List<EnemySpawnInfo> ENEMY_SPAWNS = List.of(
        new EnemySpawnInfo("skeleton", 30),
        new EnemySpawnInfo("goblin", 20),
        new EnemySpawnInfo("kobold", 15),
        new EnemySpawnInfo("thief", 15),
        new EnemySpawnInfo("slime", 20)
        // Dragon intentionally excluded (boss encounter)
    );

    public static Enemy create(String baseType, String variant) {
        Map<String, Supplier<Enemy>> variants = ENEMY_VARIANTS.get(baseType.toLowerCase());
        if (variants == null || !variants.containsKey(variant.toLowerCase())) {
            throw new IllegalArgumentException("Unknown variant: " + baseType + " - " + variant);
        }
        return variants.get(variant.toLowerCase()).get();
    }
    
    public static Enemy create(String baseType) {
        return create(baseType, "standard");
    }

    public static String getRandomEnemyName() {
        int totalWeight = ENEMY_SPAWNS.stream().mapToInt(e -> e.weight).sum(); 
        int roll = (int)(Math.random() * totalWeight);
        int cumulative = 0;

        for (EnemySpawnInfo e : ENEMY_SPAWNS) {
            cumulative += e.weight;
            if (roll < cumulative) {
                return e.name;
            }
        }
        return "Skeleton";
    }
}