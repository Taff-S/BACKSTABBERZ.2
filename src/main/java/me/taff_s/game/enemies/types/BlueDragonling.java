package me.taff_s.game.enemies.types;

import java.util.List;
import me.taff_s.game.combat.CombatResult;
import me.taff_s.game.combat.DealLibrary;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.player.Player;
import me.taff_s.game.style.Colours;

/**
 * Tier 1. Serene and detached.
 * Resists SLASH, weak to PIERCE, high health.
 * Special: Wave - hits both players for reduced damage.
 * Never reneges: its deals are the safe ones.
 */
public class BlueDragonling extends Dragonling {

    private static final double WAVE_DAMAGE_SCALE = 0.70;

    public BlueDragonling() {
        super(1, Colours.BLUE, "blue", "Blue Dragonling",
              150, 20, 30, 40, 2,
              0.25, 2,
              new String[] {
                  "drifts, perfectly calm",
                  "ripples with mild interest",
                  "dims, its calm beginning to slip",
                  "sinks low, still strangely serene"
              });

        setDamageModifier(DamageType.SLASH, 0.50);
        setDamageModifier(DamageType.PIERCE, 2.00);

        addDeal(DealLibrary.healForHarder(
            "The dungeon has not been kind to you. Let me tend your wounds. "
                + "In return, I will not go as easy on you.",
            "A fair trade. Be still.",
            60, 8));

        addDeal(DealLibrary.healBoth(
            "Rest. There is no rush. We can all mend a little before this continues.",
            "Breathe.",
            40));

        addDeal(DealLibrary.potionTribute(
            "You carry so many potions. Surrender them to me, "
                + "and I will send you directly to the Golden Dragon.",
            "Thank you. Go in peace.",
            5));
    }

    @Override
    protected String introLine() {
        return "You enter the next room. A blue dragonling floats above a pool of still water, "
             + "watching you with faint, distant interest.";
    }

    @Override
    protected CombatResult specialAttack(Player target, List<Player> everyone) {
        CombatResult result = new CombatResult();
        int damage = (int) Math.round(attack() * WAVE_DAMAGE_SCALE);

        result.addEvent(type + " sweeps a Wave through the room, hitting both of you for " + damage + " damage!");
        for (Player p : everyone) {
            strike(p, damage, result);
        }
        return result;
    }

    @Override
    public String getDeclineLine() {
        return "As you wish. Nothing is lost.";
    }
}
