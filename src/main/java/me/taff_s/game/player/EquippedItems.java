package me.taff_s.game.player;

import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.NoWeapon;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.armour.NoArmour;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.items.charms.NoCharm;

public class EquippedItems {

    private Weapon equippedWeapon = NoWeapon.getInstance();
    private Armour equippedArmour = NoArmour.getInstance();
    private Charm charm1 = NoCharm.getInstance();
    private Charm charm2 = NoCharm.getInstance();

    //i want equipped item to have a static length (4)
    //always same order (weaponn, armour, charm1, charm2)
    //when element is empty, have a default value
        //e.g when weapon slot is empty, have fists out

        public void setWeapon(Weapon weapon) {
            this.equippedWeapon = weapon;
        }
    
        public void setArmour(Armour armour) {
            this.equippedArmour = armour;
        }
    
        public void setCharm1(Charm charm) {
            this.charm1 = charm;
        }
    
        public void setCharm2(Charm charm) {
            this.charm2 = charm;
        }

        public Weapon getEquippedWeapon() {
            return equippedWeapon != null ? equippedWeapon : NoWeapon.getInstance();
        }

        public Armour getEquippedArmour() {
            return equippedArmour != null ? equippedArmour : NoArmour.getInstance();
        }
        public Charm getCharm1() {
            return charm1 != null ? charm1 : NoCharm.getInstance();
        }
        
        public Charm getCharm2() {
            return charm2 != null ? charm2 : NoCharm.getInstance();
        }

}