package me.taff_s.game.items.potions;

import java.util.function.Supplier;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.Usable;
import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.player.Player;

public class Potion extends Item implements Usable{    
    private final Supplier<PotionEffect> effectSupplier;

    public Potion(String name, String description, int price, Supplier<PotionEffect> supplier){
        super(name, description, price);
        this.effectSupplier = supplier;
    }

    @Override
    public void use(Player player) {
        PotionEffect newEffect = effectSupplier.get();
        player.applyEffect(newEffect);
        player.getInventory().removeItem(this); 
        player.sendMessage("You drank the " + itemName + ".");
    }
}
