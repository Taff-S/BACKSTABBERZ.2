package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.types.Slime;
import me.taff_s.game.player.Player;

public class AcidSlime extends Slime {
    public AcidSlime() {
        super("acid","Acid Slime", 60, 60, 10, 30, 8,0, false);
        }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A bubbling slime wobbles in front of you");
        enemyStatus();
    }
        //maybe hits through armour?
}
