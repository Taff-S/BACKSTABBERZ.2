package me.taff_s.game.combat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import me.taff_s.game.enemies.types.Dragonling;
import me.taff_s.game.player.Player;

/**
 * A bargain a dragonling can offer mid-fight.
 *
 * A Deal owns the flow (pitch -> prompt -> renege roll -> outcome).
 * Subclasses (see DealLibrary) only define what happens when it goes through.
 * Personality lives in the strings and in Dragonling#getRenegeChance(), so the
 * same mechanical deal can be reused by different dragonlings.
 */
public abstract class Deal {

    public enum Audience {
        BOTH,        // both players hear the offer and answer
        ONE_RANDOM   // a random player gets a private offer; the other only sees a whisper
    }

    private final DealTiming timing;
    private final Audience audience;
    private final boolean requireAll;   // every targeted player must accept
    private final String pitch;
    private final String acceptLine;

    protected Deal(DealTiming timing, Audience audience, boolean requireAll,
                   String pitch, String acceptLine) {
        this.timing = timing;
        this.audience = audience;
        this.requireAll = requireAll;
        this.pitch = pitch;
        this.acceptLine = acceptLine;
    }

    public DealTiming getTiming() {
        return timing;
    }

    /** Override to restrict when this deal makes sense (e.g. "players hold more than 5 potions"). */
    public boolean isEligible(Dragonling dragon, Player p1, Player p2) {
        return true;
    }

    /**
     * Apply the deal's effects. Only called when the deal is honoured.
     *
     * @param acceptors players who said yes
     * @param decliners targeted players who said no
     * @param everyone  both players
     */
    protected abstract void resolve(Dragonling dragon, List<Player> acceptors,
                                    List<Player> decliners, List<Player> everyone);

    // ------------------------------------------------------------------ //
    //  Flow
    // ------------------------------------------------------------------ //

    public final void offer(Dragonling dragon, Player p1, Player p2) {
        List<Player> everyone = List.of(p1, p2);
        List<Player> targets = (audience == Audience.BOTH)
                ? everyone
                : List.of(Math.random() < 0.5 ? p1 : p2);

        for (Player p : everyone) {
            if (targets.contains(p)) {
                dragon.say(p, pitch);
            } else {
                p.sendMessage(dragon.getType() + " leans toward " + targets.get(0).getName()
                        + " and whispers. You can't make out the words.");
            }
        }

        Map<Player, Boolean> answers = collectAnswers(targets);
        List<Player> acceptors = new ArrayList<>();
        List<Player> decliners = new ArrayList<>();
        for (Player t : targets) {
            if (answers.get(t)) {
                acceptors.add(t);
            } else {
                decliners.add(t);
            }
        }

        if (acceptors.isEmpty() || (requireAll && !decliners.isEmpty())) {
            for (Player t : targets) {
                dragon.say(t, dragon.getDeclineLine());
            }
            return;
        }

        for (Player t : targets) {
            dragon.say(t, acceptLine);
        }

        // Some dragonlings don't keep their word.
        if (Math.random() < dragon.getRenegeChance()) {
            dragon.betray(acceptors, targets);
            return;
        }

        resolve(dragon, acceptors, decliners, everyone);
    }

    // ------------------------------------------------------------------ //
    //  Input
    // ------------------------------------------------------------------ //

    /** Ask the targeted players at the same time, like the rest of the game's simultaneous prompts. */
    private Map<Player, Boolean> collectAnswers(List<Player> targets) {
        ExecutorService executor = Executors.newFixedThreadPool(targets.size());
        Map<Player, Boolean> answers = new LinkedHashMap<>();
        try {
            Map<Player, Future<Boolean>> pending = new LinkedHashMap<>();
            for (Player p : targets) {
                pending.put(p, executor.submit(() -> askYesNo(p)));
            }
            for (Map.Entry<Player, Future<Boolean>> entry : pending.entrySet()) {
                try {
                    answers.put(entry.getKey(), entry.getValue().get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    answers.put(entry.getKey(), false);
                } catch (ExecutionException e) {
                    answers.put(entry.getKey(), false);
                }
            }
        } finally {
            executor.shutdown();
        }
        return answers;
    }

    private static boolean askYesNo(Player player) {
        if (player.getHandler() == null) return false;   // no connection (e.g. in tests)
        while (true) {
            String input = player.userInput("Accept the deal? (yes/no)");
            if (input == null) return false;             // disconnected
            input = input.trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            player.sendMessage("Please type 'yes' or 'no'.");
        }
    }
}
