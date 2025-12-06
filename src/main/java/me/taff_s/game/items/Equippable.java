package me.taff_s.game.items;

import me.taff_s.game.player.Player;

public interface Equippable {
    void equip(Player player);
    void unequip(Player player);
}