import java.util.ArrayList;
import java.util.List;
public class Shop {
    private List<Item> stock = new ArrayList<>();
    
    public Shop(List<Item> startingStock) {
        this.stock.addAll(startingStock);
    }

    public String showStockDetailed() {
        StringBuilder sb = new StringBuilder("Items for Sale:\n");
        for (int i = 0; i < stock.size(); i++) {
            Item item = stock.get(i);
            sb.append("[").append(i + 1).append("] ")
              .append(item.getItemName())
              .append(" - ").append(item.getDescription())
              .append(" (").append(item.getPrice()).append(" gold)\n");
        }
        return sb.toString();
    }

    public void displayIntro(Player player){
        player.sendMessage("You continue to the next room and as soon as you step in, you feel the tension lift from your shoulders. The air is warm, spicy – not oppressive, but in a comforting way. You could swear that you can smell your mother's tastiest recipe.\n\"Welcome welcome, to my humble little shop! One item each, and I only take gold. None of that trading nonsense.\"");
    } 

    public void interact(Player player, PlayerHandler handler) {
        displayIntro(player);
    
        boolean shopping = true;
    
        while (shopping) {
            player.sendMessage(showStockDetailed());
            player.sendMessage("Type the number of the item to buy, 'i#' to inspect (e.g. i2), or 'leave' to leave the shop.");
    
            String choice = player.userInput("> ").trim().toLowerCase();
    
            if (choice.equals("leave")) {
                player.sendMessage("You leave the shop.\n");
                shopping = false;
                continue;
            }
    
            // Inspection handling
            if (choice.startsWith("i")) {
                try {
                    int inspectIndex = Integer.parseInt(choice.substring(1).trim()) - 1;
                    if (inspectIndex >= 0 && inspectIndex < stock.size()) {
                        Item item = stock.get(inspectIndex);
                        player.sendMessage("Inspecting: " + item.getItemName());
                        player.sendMessage("Description: " + item.getDescription());
                        player.sendMessage("Price: " + item.getPrice() + " gold");
                    } else {
                        player.sendMessage("Invalid item number.");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid inspect format. Use i# like i1.");
                }
                continue;
            }
    
            int index;
            try {
                index = Integer.parseInt(choice) - 1;
            } catch (NumberFormatException e) {
                player.sendMessage("That's not a valid number.");
                continue;
            }
    
            if (index >= 0 && index < stock.size()) {
                Item selected = stock.get(index);
                if (player.getCoins() >= selected.getPrice()) {
                    player.getInventory().addItem(selected);
                    player.coinChange(-selected.getPrice());
                    player.sendMessage("You bought: " + selected.getItemName());
                    stock.remove(index);
                } else {
                    player.sendMessage("Not enough gold!");
                }
            } else {
                player.sendMessage("Invalid selection.");
            }
        }
    }
}