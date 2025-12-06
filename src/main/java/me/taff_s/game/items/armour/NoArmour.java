package me.taff_s.game.items.armour;

import me.taff_s.game.items.armour.Armour;

public class NoArmour extends Armour {
    private static final NoArmour instance = new NoArmour();

    private NoArmour(){
        super("No armour", "You aren't wearing any armour",0, 0);
    }

    public static NoArmour getInstance() {
        return instance;
    }

}
