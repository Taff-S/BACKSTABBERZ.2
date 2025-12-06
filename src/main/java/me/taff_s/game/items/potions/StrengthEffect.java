package me.taff_s.game.items.potions;

import me.taff_s.game.items.potions.EffectDecorator;
import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.items.potions.Timed;
import me.taff_s.game.player.Player;

public class StrengthEffect extends EffectDecorator implements Timed {

    private int strengthBoost;
    public int remainingTurns;

    public StrengthEffect(PotionEffect wrapped, int strengthBoost,int duration) {
        super(wrapped);
        this.remainingTurns = duration;
        this.strengthBoost = strengthBoost;
    }

    @Override
    public void apply(Player player) {
        player.changeStrength(strengthBoost);
        player.sendMessage("You feel stronger");
    }

    @Override
    public void onTurnStart(Player player) {
        super.onTurnStart(player);
        remainingTurns--;
        
        if (isExpired()) {
            player.changeStrength(-strengthBoost); 
            player.sendMessage("Strength buff expired.");
        }
    }

    @Override
    public boolean isExpired() {
        return remainingTurns <= 0;
    }
}
