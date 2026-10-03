package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.types.Goblin;
import me.taff_s.game.player.Player;

public class SwordGoblin extends Goblin {
    
    public SwordGoblin() {
        super("sword","Sword Goblin", 90, 90, 30, 60, 15, 1, false);
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A goblin stares up at you, wielding a large, crimson coated sword, grinning maniacally.");
        enemyStatus();
    }
}
