package me.taff_s.game.items.charms;

import me.taff_s.game.items.Item;
import me.taff_s.game.items.Equippable;
import me.taff_s.game.items.charms.NoCharm;
import me.taff_s.game.player.Player;
import me.taff_s.game.player.EquippedItems;
import me.taff_s.game.player.RestChoice;

public class Charm extends Item implements Equippable{

    public Charm(String name, String description, int price) {
        super(name, description, price);
    }

    public void onRest(Player player) {}
    public void onCombat(Player player) {}
    public void onBackstab(Player attacker, Player defender) {}
    public void modifyStats(Player player) {}

    @Override
    public void equip(Player player){
        EquippedItems equipped = player.getEquipment();
        if (equipped.getCharm1() instanceof NoCharm) {
            equipped.setCharm1(this);
            player.getInventory().removeItem(this);
            player.sendMessage("Equipped charm in slot 1: " + getItemName());
        } else if (equipped.getCharm2() instanceof NoCharm) {
            equipped.setCharm2(this);
            player.getInventory().removeItem(this);
            player.sendMessage("Equipped charm in slot 2: " + getItemName());
        } else {
            player.sendMessage("Both charm slots are full. Unequip one first.");
        }
    }

    @Override
    public void unequip(Player player) {
        EquippedItems eq = player.getEquipment();
        if (eq.getCharm1() == this) {
            player.getInventory().addItem(this);
            eq.setCharm1(NoCharm.getInstance());
            player.sendMessage("Unequipped charm from slot 1.");
        } else if (eq.getCharm2() == this) {
            player.getInventory().addItem(this);
            eq.setCharm2(NoCharm.getInstance());
            player.sendMessage("Unequipped charm from slot 2.");
        } else {
            player.sendMessage("This charm is not currently equipped.");
        }
    }
}