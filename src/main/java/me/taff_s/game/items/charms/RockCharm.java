package me.taff_s.game.items.charms;

import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.player.Player;

public class RockCharm extends Charm {
    public RockCharm() {
        super("The Rock", "Increases defence a little", 50);
    }

    @Override
    public void modifyStats(Player player) {
        player.defenceMod = 0.95;
    }
}