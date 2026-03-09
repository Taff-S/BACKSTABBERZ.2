package me.taff_s.game.effects;

public interface TimedEffect {
    void onTurnStart(Object target);
    boolean isExpired();
}