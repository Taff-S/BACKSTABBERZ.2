package me.taff_s.game.combat;

/**
 * When in a fight a deal may be offered, based on the enemy's remaining health.
 */
public enum DealTiming {
    HIGH,   // above 66% health (near the start of the fight)
    MID,    // 33% - 66%
    LOW;    // below 33%

    public static DealTiming fromHealth(int health, int maxHealth) {
        double pct = (double) health / maxHealth;
        if (pct > 0.66) return HIGH;
        if (pct > 0.33) return MID;
        return LOW;
    }
}
