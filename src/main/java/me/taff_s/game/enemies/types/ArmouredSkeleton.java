package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.types.Skeleton;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.player.Player;

public class ArmouredSkeleton extends Skeleton {
    public ArmouredSkeleton() {
        super("armoured","Armoured Skeleton", 10, 10, 2, 2, 20, 10, true); 
        setDamageModifier(DamageType.FORCE, 4.00); 
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A skeleton shambles towards you, though you can barely tell from its bulky armour");
        enemyStatus();
    }
}