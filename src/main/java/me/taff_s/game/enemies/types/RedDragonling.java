package me.taff_s.game.enemies.types;

import java.util.List;
import me.taff_s.game.combat.CombatResult;
import me.taff_s.game.combat.DealLibrary;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.weapons.DamageType;
import me.taff_s.game.player.Player;
import me.taff_s.game.style.Colours;

/**
 * Tier 1. Self-absorbed and confident.
 * Resists FORCE, weak to SLASH, hits harder than Blue.
 * Special: Flame Burst - heavy damage to one player plus armour durability loss.
 * Mostly keeps its word, but is crueller when it doesn't.
 */
public class RedDragonling extends Dragonling {

    private static final int BURN_DURABILITY = 3;

    public RedDragonling() {
        super(1, Colours.RED, "red", "Red Dragonling",
              120, 35, 50, 40, 1,
              0.30, 2,
              new String[] {
                  "preens, entirely unbothered",
                  "flares its nostrils at you",
                  "snarls, its scales dulled",
                  "smoulders, wounded pride on full display"
              });

        setDamageModifier(DamageType.FORCE, 0.50);
        setDamageModifier(DamageType.SLASH, 2.00);

        addDeal(DealLibrary.repairForHit(
            "You've been hacking and slashing a lot, haven't you? Your gear has seen better days. "
                + "Let me hit you once each and I'll restore all of it.",
            "Brace yourselves, darlings."));

        addDeal(DealLibrary.coinTribute(
            "I find myself rather uncaring of this battle's outcome. Give me a mere quarter of your "
                + "coins, and I'll let you pass just like that.",
            "Splendid. Do leave the coins on your way out.",
            0.25));

        addDeal(DealLibrary.jumpPartner(
            " \"Psst. You. This... This fight bores me.\" The dragonling whispers telepathically. Even in your mind, you can hear the weariness in its voice. It is weak, no matter how hard it tries to ignore it."
            + "\"You seem like the leader to me. Let's jump your 'friend' and split their things between us.\" "
            + "They won't see it coming.",
            "Delicious. Watch this."));
    }

    @Override
    protected String introLine() {
        return "You enter the next room. A crimson dragonling lounges on a heap of coins, "
             + "admiring its reflection in a polished shield. As soon as it notices you, it flares its nostrils and smirks."
             + " \"Well well well. Who dares enter the domain of the omnipotent Red Dragon?\" "
             + " Overcoming your initial shock, you note that the scaled beast is much smaller than the Golden Dragon, and its voice certianly isn't as awe-inspiring."
             + "More likely, it is a dragonling, a young wyrm yet to have obtain its true power and resultant hoarde. But it is no less dangerous than an adult dragon. You steel yourself for the fight ahead.";
    }

    @Override
    protected CombatResult specialAttack(Player target, List<Player> everyone) {
        CombatResult result = new CombatResult();
        int damage = (int) Math.round(attack() * 1.25);

        result.addEvent(type + " unleashes Flame Burst on " + target.getName() + " for " + damage + " damage!");
        strike(target, damage, result);

        // Armour only has durability once Armour sets it (see notes); until then this is a no-op.
        Armour armour = target.getEquipment().getEquippedArmour();
        if (target.living() && armour.isBreakable() && !armour.isBroken()) {
            armour.reduceDurability(BURN_DURABILITY);
            result.addEvent(target.getName() + "'s " + armour.getItemName()
                    + (armour.isBroken() ? " is burnt to ash!" : " is scorched!"));
        }
        return result;
    }

    @Override
    public double getRenegeChance() {
        return 0.10;
    }

    @Override
    public String getDeclineLine() {
        return "Your loss. Do try to look less pathetic while you lose.";
    }

    @Override
    protected String betrayalLine() {
        return "Did you honestly think I'd keep my word? Adorable.";
    }

    @Override
    protected int betrayalDamage() {
        return (int) Math.round(attack() * 1.5);
    }
}
