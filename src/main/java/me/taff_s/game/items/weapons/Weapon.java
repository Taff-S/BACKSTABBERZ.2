package me.taff_s.game.items.weapons;

import me.taff_s.game.items.Item;
import me.taff_s.game.items.Equippable;
import me.taff_s.game.items.weapons.WeaponClass;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.items.weapons.NoWeapon;
import me.taff_s.game.player.Player;

public class Weapon extends Item implements Equippable{
    private int damage;
    public WeaponClass wClass;
    public DamageType dType;

    //weapon constructor
    public Weapon(String itemName, String description, int price, int damage, WeaponClass wc, DamageType dt, int max){
        super(itemName, description, price);
        this.damage = damage;
        this.wClass = wc;
        this.dType = dt;
        setDurability(max);
    }

    public String getWeaponName() {
        return itemName;
    }

    public void setWeaponName(String name){
        itemName = name;
    }
    
    public int getDamage(){
        return damage;
    }

    public void setDamageType(DamageType dt){
        this.dType = dt;
    }

    public void setWeaponClass(WeaponClass wc){
        this.wClass = wc;
    }

    public WeaponClass getWeaponClass(){
        return wClass;
    }

    public DamageType getDamageType(){
        return dType;
    }

    @Override
    public void equip(Player player) {
        player.getEquipment().setWeapon(this);
        player.sendMessage("Equipped weapon: " + getItemName());
    }
    
    @Override
    public void unequip(Player player) {
        Weapon weapon = player.getEquipment().getEquippedWeapon();
        if (weapon != NoWeapon.getInstance()) {
            player.getInventory().addItem(weapon);
            player.getEquipment().setWeapon(NoWeapon.getInstance());
        } else {
            player.sendMessage("No weapon equipped");
        }
    }

}
