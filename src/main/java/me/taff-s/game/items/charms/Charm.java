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
            player.inventory.removeItem(this);
            player.sendMessage("Equipped charm in slot 1: " + itemName);
        } else if (equipped.getCharm2() instanceof NoCharm) {
            equipped.setCharm2(this);
            player.inventory.removeItem(this);
            player.sendMessage("Equipped charm in slot 2: " + itemName);
        } else {
            player.sendMessage("Both charm slots are full. Unequip one first.");
        }
    }

    @Override
    public void unequip(Player player) {
        EquippedItems eq = player.getEquipment();
        if (eq.getCharm1() == this) {
            player.inventory.addItem(this);
            eq.setCharm1(NoCharm.getInstance());
            player.sendMessage("Unequipped charm from slot 1.");
        } else if (eq.getCharm2() == this) {
            player.inventory.addItem(this);
            eq.setCharm2(NoCharm.getInstance());
            player.sendMessage("Unequipped charm from slot 2.");
        } else {
            player.sendMessage("This charm is not currently equipped.");
        }
    }
}