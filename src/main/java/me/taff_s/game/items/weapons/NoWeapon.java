package me.taff_s.game.items.weapons;

import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponClass;
import me.taff_s.game.items.weapons.DamageType;

public class NoWeapon extends Weapon{
    private static final NoWeapon instance = new NoWeapon();

    private NoWeapon(){
        super("Fists", "You don't have a weapon", 0, 0, WeaponClass.BLUNT, DamageType.FORCE,-1);
    }

    public static NoWeapon getInstance() {
        return instance;
    }

}