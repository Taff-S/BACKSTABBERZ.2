package me.taff_s.game.items.armour;

import me.taff_s.game.items.Item;
import me.taff_s.game.items.Equippable;
import me.taff_s.game.items.armour.NoArmour;
import me.taff_s.game.player.Player;

public class Armour extends Item implements Equippable{
    private int defence;

    public Armour(String itemName, String description, int price, int defence){
        this(itemName, description, price, defence, 10);
    }

    public Armour(String itemName, String description, int price, int defence, int maxDurability){
        super(itemName, description, price);
        this.defence = defence;
        setDurability(maxDurability);
    }

    public double getArmourVal() {
        double reduction = 0.05 * defence;
        double result = 1.0 - reduction;
        return Math.max(0.2, result); // prevents defense more than 80%
    }

    public String getArmourName() {
        return itemName;
    }

    public void setArmourName(String name){
        itemName = name;
    }

    public Armour copy() {
        Armour copy = new Armour(itemName, description, getPrice(), defence, maxDurability);
        if (isBreakable()) {
            copy.reduceDurability(maxDurability - durability);
        }
        return copy;
    }

    @Override
    public void equip(Player player) {
        if (isBroken()) {
            player.sendMessage("You can't equip broken armour.");
            return;
        }
        player.getEquipment().setArmour(this);
        player.sendMessage("Equipped armour: " + getItemName());
    }

    @Override
    public void unequip(Player player) {
        Armour armour = player.getEquipment().getEquippedArmour();
        if (armour != NoArmour.getInstance()) {
            player.getInventory().addItem(armour);
            player.getEquipment().setArmour(NoArmour.getInstance());
        } else {
            player.sendMessage("No armour equipped");
        }
    }

}
