public abstract class Item{
    String itemName;
    String description;
    int price;
    protected int durability = -1; // -1 = indestructible by default
    protected int maxDurability = -1;

    public Item(String name, String description, int price){
        this.itemName = name;
        this.description = description;
        this.price = price;
    }

    public String getItemName(){
        return itemName;
    }

    public String getDescription(){
        return description;
    }

    public int getPrice(){
        return price;
    }
    
    public boolean isBreakable() {
        return maxDurability > 0;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void reduceDurability(int amount) {
        if (isBreakable()) {
            durability = Math.max(0, durability - amount);
        }
    }

    public boolean isBroken() {
        return isBreakable() && durability <= 0;
    }

    public void setDurability(int max) {
        this.maxDurability = max;
        this.durability = max;
    }

}
