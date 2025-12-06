package me.taff_s.game.items.potions;

import me.taff_s.game.player.Player;

public interface PotionEffect {
    void apply(Player player);

    default void onTurnStart(Player player) {}
    default boolean isExpired() {
        return false;
    }
}

