package me.taff_s.game.world;

import me.taff_s.game.player.Player;

/**
 * Interface for all encounters in the game.
 * Encounters are events that occur when players enter a room.
 */
public interface Encounter {
    /**
     * Executes the encounter logic for the two players.
     * @param player1 The first player.
     * @param player2 The second player.
     */
    void execute(Player player1, Player player2);
}