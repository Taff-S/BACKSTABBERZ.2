package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.types.Kobold;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.player.Player;

public class FlyingKobold extends Kobold {
    public FlyingKobold() {
        super("flying", "Flying Kobold", 6, 6, 3, 4, 18, 0, false); 
        setDamageModifier(DamageType.PIERCE, 2.00);
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A winged kobold eyes' widen at your party, before taking to the air and brandishing its claws.");
        enemyStatus();
    }

}
