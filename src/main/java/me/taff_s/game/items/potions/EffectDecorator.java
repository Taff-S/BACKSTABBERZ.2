package me.taff_s.game.items.potions;

import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.items.potions.Timed;
import me.taff_s.game.player.Player;

public abstract class EffectDecorator implements PotionEffect {
    protected final PotionEffect wrapped;

    public EffectDecorator(PotionEffect wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void apply(Player player) {
        wrapped.apply(player);
    }

    @Override
    public void onTurnStart(Player player) {
        if (wrapped instanceof Timed) {
            ((Timed) wrapped).onTurnStart(player);
        }
    }

    @Override
    public boolean isExpired() {
        return wrapped instanceof Timed && ((Timed) wrapped).isExpired();
    }
}