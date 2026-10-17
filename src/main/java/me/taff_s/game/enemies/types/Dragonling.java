package me.taff_s.game.enemies.types;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import me.taff_s.game.combat.CombatResult;
import me.taff_s.game.combat.Deal;
import me.taff_s.game.combat.DealTiming;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.player.Player;
import me.taff_s.game.style.Colours;

/**
 * Base class for all dragonlings.
 *
 * Shared here: deal polling, special-attack rolling and cooldown, health-bracket
 * status text, and betrayal. Subclasses supply stats, resistances, one special
 * attack, their deal list, and their personality strings.
 *
 * Intentionally extends Enemy, NOT Dragon, so CombatEncounter's
 * "enemy instanceof Dragon" boss rules don't apply to dragonlings.
 */
public abstract class Dragonling extends Enemy {

    private static final double DEAL_CHANCE_PER_ROUND = 0.35;

    private final int tier;
    private final String colour;          // ANSI colour for this dragonling's speech
    private final String[] statusLines;   // 4 lines: >75%, >50%, >25%, <=25%

    // Special attack
    private final double specialChance;
    private final int specialCooldown;    // rounds to wait after using the special
    private int cooldownLeft = 0;

    // Deals (only one is offered per battle)
    private final List<Deal> deals = new ArrayList<>();
    private boolean dealOffered = false;
    private boolean conceded = false;

    protected Dragonling(int tier, String colour, String variant, String name,
                         int health, int lowDmg, int highDmg, int reward, int defence,
                         double specialChance, int specialCooldown, String[] statusLines) {
        super(variant, name, health, health, lowDmg, highDmg, reward, defence, false);
        if (statusLines.length != 4) {
            throw new IllegalArgumentException("statusLines must contain exactly 4 entries");
        }
        this.tier = tier;
        this.colour = colour;
        this.specialChance = specialChance;
        this.specialCooldown = specialCooldown;
        this.statusLines = statusLines;
    }

    // ------------------------------------------------------------------ //
    //  Personality hooks (override per dragonling)
    // ------------------------------------------------------------------ //

    protected abstract String introLine();

    /** Chance (0-1) that an accepted deal is NOT honoured. */
    public double getRenegeChance() {
        return 0.0;
    }

    public String getDeclineLine() {
        return "Suit yourselves.";
    }

    protected String betrayalLine() {
        return "Did you really think I'd keep my word?";
    }

    protected int betrayalDamage() {
        return attack();
    }

    // ------------------------------------------------------------------ //
    //  Special attacks
    // ------------------------------------------------------------------ //

    /** Implement the dragonling's special. Called only when the roll and cooldown allow it. */
    protected abstract CombatResult specialAttack(Player target, List<Player> everyone);

    @Override
    public CombatResult trySpecial(Player target, List<Player> everyone) {
        if (cooldownLeft > 0) {
            cooldownLeft--;
            return null;
        }
        if (Math.random() >= specialChance) {
            return null;
        }
        cooldownLeft = specialCooldown;
        return specialAttack(target, everyone);
    }

    /** Hit a player the same way a normal enemy attack does (armour and defensive stance apply). */
    protected void strike(Player victim, int damage, CombatResult result) {
        victim.takeDamage(damage, victim.getEquipment().getEquippedArmour(), victim.getStance(), false);
        if (!victim.living()) {
            result.setPlayerDefeated(true);
            result.addEvent(victim.getName() + " has been defeated!");
        }
    }

    // ------------------------------------------------------------------ //
    //  Deals
    // ------------------------------------------------------------------ //

    protected void addDeal(Deal deal) {
        deals.add(deal);
    }

    @Override
    public boolean tryDeal(Player p1, Player p2) {
        if (dealOffered || !living()) return false;
        if (Math.random() >= DEAL_CHANCE_PER_ROUND) return false;

        DealTiming now = DealTiming.fromHealth(health, maxHealth);
        List<Deal> candidates = new ArrayList<>();
        for (Deal deal : deals) {
            if (deal.getTiming() == now && deal.isEligible(this, p1, p2)) {
                candidates.add(deal);
            }
        }
        if (candidates.isEmpty()) return false;

        dealOffered = true;
        candidates.get(ThreadLocalRandom.current().nextInt(candidates.size())).offer(this, p1, p2);
        return true;
    }

    /** The dragonling reneges: BACKSTABBED, followed by an unblockable hit on whoever trusted it. */
    public void betray(List<Player> victims, List<Player> witnesses) {
        for (Player p : witnesses) {
            p.sendMessage(Colours.RED + "*** BACKSTABBED ***" + Colours.RESET);
        }
        for (Player p : witnesses) {
            say(p, betrayalLine());
        }
        for (Player victim : victims) {
            int dmg = betrayalDamage();
            victim.antiHeal(dmg);
            victim.sendMessage(type + " strikes you for " + dmg + " damage!");
        }
    }

    // ------------------------------------------------------------------ //
    //  Effects deals can use
    // ------------------------------------------------------------------ //

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /** Ends the fight without a kill (the combat loop stops once the enemy is no longer living). */
    public void concede() {
        conceded = true;
        health = 0;
    }

    public boolean wasConceded() {
        return conceded;
    }

    // ------------------------------------------------------------------ //
    //  Speech and display
    // ------------------------------------------------------------------ //

    public void say(Player player, String line) {
        player.sendMessage(colour + "[" + type + "] \"" + line + "\"" + Colours.RESET);
    }

    public void sayAll(List<Player> players, String line) {
        for (Player p : players) {
            say(p, line);
        }
    }

    @Override
    public String enemyStatus() {
        double pct = (double) health / maxHealth;
        int index = pct > 0.75 ? 0 : pct > 0.5 ? 1 : pct > 0.25 ? 2 : 3;
        return type + " " + statusLines[index];
    }

    @Override
    public void display(Player player) {
        player.sendMessage(introLine());
        player.sendMessage(enemyStatus());
    }

    public int getTier() {
        return tier;
    }
}
