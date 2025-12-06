package me.taff_s.game.items.potions;

import me.taff_s.game.items.potions.EffectDecorator;
import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.player.Player;

public class HealingEffect extends EffectDecorator {
    private final int healAmount;

    public HealingEffect(PotionEffect wrapped, int healAmount) {
        super(wrapped);
        this.healAmount = healAmount;
    }

    @Override
    public void apply(Player player) {
        super.apply(player); 
        player.heal(healAmount);
        player.sendMessage("Healed " + healAmount + " HP.");
    }
}
