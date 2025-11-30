import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory{
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public String showInventory() {
        String m = "";
        if (items.isEmpty()) {
            m = "Inventory is empty.";
            return m;
        }
        else{
            m = "Inventory:\n";
            for (Item item : items) {
                m += "- " + item.getItemName() +"\n";
            }
            return m;
        }
    }

    public String showInventoryDetailed() {
        if (items.isEmpty()) {
            return "Inventory is empty.";
        }
    
        StringBuilder sb = new StringBuilder("Your Inventory:\n");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            sb.append("[").append(i + 1).append("] ")
              .append(item.getItemName())
              .append(" - ").append(item.getDescription())
              .append("\n");
        }
        return sb.toString();
    }

    public void manageInventory(PlayerHandler handler, Player player) throws IOException {
    Inventory inventory = player.getInventory();
    EquippedItems equipped = player.getEquipment();

    while (true) {
        handler.sendMulti(
            inventory.showInventoryDetailed(),
            "\nCurrently Equipped:",
            "Gold: " + player.getCoins(),
            "[Weapon] " + equipped.getEquippedWeapon().getItemName(),
            "[Armour] " + equipped.getEquippedArmour().getItemName(),
            "[Charm1] " + equipped.getCharm1().getItemName(),
            "[Charm2] " + equipped.getCharm2().getItemName(),
            "\nWhat would you like to do?",
            "[1] Equip item",
            "[2] Unequip item",
            "[3] Use potion",
            "[4] Exit inventory"
        );
        String choice = handler.prompt("> ");
        if (choice == null) break;

        switch (choice.trim()) {
            case "1": // Equip
                handler.send("Enter the number of the item to equip:");
                String itemChoice = handler.prompt("> ");
                try {
                    int idx = Integer.parseInt(itemChoice) - 1;
                    Item selected = inventory.getItems().get(idx);

                    if (selected instanceof Weapon) {
                        player.useItem(selected);
                    } else if (selected instanceof Armour) {
                        player.useItem(selected);
                    } else if (selected instanceof Charm) {
                        if (equipped.getCharm1() instanceof NoCharm) {
                            player.useItem(selected);
                        } else if (equipped.getCharm2() instanceof NoCharm) {
                            player.useItem(selected);
                        } else {
                            handler.send("Both charm slots are full. Unequip one first.");
                        }
                    } else {
                        handler.send("You can't equip that item.");
                    }
                } catch (Exception e) {
                    handler.send("Invalid selection.");
                }
                break;

            case "2": // Unequip
                handler.sendMulti("What do you want to unequip?",
                    "[1] Weapon", "[2] Armour", "[3] Charm 1", "[4] Charm 2");

                String unequipChoice = handler.prompt("> ");
                Item itemToUnequip = null;

                switch (unequipChoice.trim()) {
                    case "1":
                        itemToUnequip = equipped.getEquippedWeapon();
                        handler.send(itemToUnequip.getItemName() + " added to inventory.");
                        break;
                    case "2":
                        itemToUnequip = equipped.getEquippedArmour();
                        handler.send(itemToUnequip.getItemName() + " added to inventory.");
                        break;
                    case "3":
                        itemToUnequip = equipped.getCharm1();
                        handler.send(itemToUnequip.getItemName() + " added to inventory.");
                        break;
                    case "4":
                        itemToUnequip = equipped.getCharm2();
                        handler.send(itemToUnequip.getItemName() + " added to inventory.");
                        break;
                    default:
                        handler.send("Invalid slot.");
                        break;
                }

                if (itemToUnequip == null ||
                    itemToUnequip instanceof NoWeapon ||
                    itemToUnequip instanceof NoArmour ||
                    itemToUnequip instanceof NoCharm) {
                    handler.send("That slot is already empty or invalid.");
                } else if (itemToUnequip instanceof Equippable) {
                    ((Equippable) itemToUnequip).unequip(player);
                    handler.send(itemToUnequip.getItemName() + " unequipped.");
                } else {
                    handler.send("You can't unequip that.");
                }
                break;
                case "3":
                handler.send("Enter the number of the item to use:");
                String useChoice = handler.prompt("> ");
                try {
                    int idx = Integer.parseInt(useChoice) - 1;
                    Item selected = inventory.getItems().get(idx);
            
                    if (selected instanceof Usable) {
                        ((Usable) selected).use(player);
                        inventory.removeItem(selected);
                        handler.send(selected.getItemName() + " removed from inventory.");
                    } else {
                        handler.send("This item can't be used here. Maybe it needs to be equipped?");
                    }
                } catch (Exception e) {
                    handler.send("Invalid selection.");
                }
                break;

                case "4":
                    handler.send("Exiting inventory...");
                return;

            default:
                handler.send("Invalid input.");
        }
    }
}

    public List<Item> getItems() {
        return items;
    }

}