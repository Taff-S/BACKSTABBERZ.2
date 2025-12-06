package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.types.Skeleton;
import me.taff_s.game.player.Player;

public class BowSkeleton extends Skeleton {
    public BowSkeleton() {
        super("bow","Bow Skeleton", 10, 10, 3, 4, 20, 1, false); 
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A skeleton wraps its phalanges around the string of a bow and noks an arrow");
    }
}
