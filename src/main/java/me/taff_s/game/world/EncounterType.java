package me.taff_s.game.world;

/**
 * Enum representing the types of encounters in the game.
 */
public enum EncounterType {
    SHOP,
    TREASURE,
    MIMIC,
    VQUEEN,
    VSHOP,
    BLACKSMITH,
    BLOODMACHINE;

    public static EncounterType getRandomEncounter() {
        EncounterType[] values = values();
        return values[(int) (Math.random() * values.length)];
    }
}