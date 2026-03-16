package me.taff_s.game.items.potions;

import me.taff_s.game.items.potions.EffectDecorator;
import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.effects.TimedEffect;
import me.taff_s.game.player.Player;

public class StrengthEffect extends EffectDecorator implements TimedEffect {

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
    public void onTurnStart(Object target) {
        if (!(target instanceof Player)) return;
        Player player = (Player) target;
        super.onTurnStart(player);  // Assuming EffectDecorator handles this
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
