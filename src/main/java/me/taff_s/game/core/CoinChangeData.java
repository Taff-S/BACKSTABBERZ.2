package me.taff_s.game.core;

import me.taff_s.game.player.Player;

public class CoinChangeData {
    public final Player player;
    public final int delta;

    public CoinChangeData(Player player, int delta) {
        this.player = player;
        this.delta = delta; //delta being change of coins
    }
}