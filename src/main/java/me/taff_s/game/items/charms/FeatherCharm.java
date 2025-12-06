package me.taff_s.game.items.charms;

import me.taff_s.game.player.Player;
import me.taff_s.game.items.charms.Charm;

public class FeatherCharm extends Charm{
    public FeatherCharm() {
        super("The Feather", "Allows you to sleep a little comfier", 50);
    }

    @Override
    public void onRest(Player player) {
        player.heal(2); 
    }
}