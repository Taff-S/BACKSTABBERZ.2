package me.taff_s.game.items.potions;

import me.taff_s.game.player.Player;

public interface Timed {
    void onTurnStart(Player player);
    boolean isExpired();
}

