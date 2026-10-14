package me.taff_s.game.combat;

import java.util.ArrayList;
import java.util.List;
import me.taff_s.game.enemies.types.Dragonling;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.potions.Potion;
import me.taff_s.game.player.Player;

/**
 * Reusable deal mechanics. Each factory takes the dragonling's own wording,
 * so Red and Blue can offer the same mechanic in a different voice.
 */
public final class DealLibrary {

    private DealLibrary() {}

    // ------------------------------------------------------------------ //
    //  HIGH health deals
    // ------------------------------------------------------------------ //

    /** "I'll cure your wounds, but I won't go easy on you." */
    public static Deal healForHarder(String pitch, String acceptLine, int healAmount, int attackBoost) {
        return new Deal(DealTiming.HIGH, Deal.Audience.BOTH, false, pitch, acceptLine) {
            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                for (Player p : acceptors) {
                    p.heal(healAmount);
                    p.sendMessage("Your wounds knit together. (+" + healAmount + " HP)");
                }
                dragon.boostAttack(attackBoost);
                for (Player p : everyone) {
                    p.sendMessage(dragon.getType() + " looks a little more dangerous.");
                }
            }
        };
    }

    /** "Let me hit you once and I'll restore your gear's durability." */
    public static Deal repairForHit(String pitch, String acceptLine) {
        return new Deal(DealTiming.HIGH, Deal.Audience.BOTH, false, pitch, acceptLine) {
            @Override
            public boolean isEligible(Dragonling dragon, Player p1, Player p2) {
                return hasDamagedGear(p1) || hasDamagedGear(p2);
            }

            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                for (Player p : acceptors) {
                    int dealt = safeDamage(p, dragon.attack());
                    p.sendMessage(dragon.getType() + " clubs you for " + dealt + " damage!");
                    repairGear(p);
                    p.sendMessage("Your equipment gleams like new. (durability restored)");
                }
            }
        };
    }

    // ------------------------------------------------------------------ //
    //  MID health deals
    // ------------------------------------------------------------------ //

    /** "Let's all take a breather." Acceptors heal, and so does the dragonling. */
    public static Deal healBoth(String pitch, String acceptLine, int healAmount) {
        return new Deal(DealTiming.MID, Deal.Audience.BOTH, false, pitch, acceptLine) {
            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                for (Player p : acceptors) {
                    p.heal(healAmount);
                    p.sendMessage("You rest and recover. (+" + healAmount + " HP)");
                }
                dragon.heal(healAmount);
                for (Player p : everyone) {
                    p.sendMessage(dragon.getType() + " recovers too.");
                }
            }
        };
    }

    /** "Give me a cut of your coins and I'll send you straight to my father." Both must agree. */
    public static Deal coinTribute(String pitch, String acceptLine, double fraction) {
        return new Deal(DealTiming.MID, Deal.Audience.BOTH, true, pitch, acceptLine) {
            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                for (Player p : acceptors) {
                    int tribute = (int) (p.getCoins() * fraction);
                    if (tribute > 0) {
                        p.coinChange(-tribute);
                    }
                }
                dragon.concede();
            }
        };
    }

    /** "Hand over all your potions and I'll send you to the Golden Dragon." Both must agree. */
    public static Deal potionTribute(String pitch, String acceptLine, int minPotions) {
        return new Deal(DealTiming.MID, Deal.Audience.BOTH, true, pitch, acceptLine) {
            @Override
            public boolean isEligible(Dragonling dragon, Player p1, Player p2) {
                return countPotions(p1) + countPotions(p2) > minPotions;
            }

            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                for (Player p : acceptors) {
                    p.getInventory().getItems().removeIf(item -> item instanceof Potion);
                    p.sendMessage("You hand over every potion you carry.");
                }
                dragon.concede();
            }
        };
    }

    // ------------------------------------------------------------------ //
    //  LOW health deals
    // ------------------------------------------------------------------ //

    /**
     * Private offer to one player: "Let's jump your friend and take their stuff."
     * The conspirator steals half the partner's coins, the partner is hit, and the fight ends.
     * The partner is never asked.
     */
    public static Deal jumpPartner(String pitch, String acceptLine) {
        return new Deal(DealTiming.LOW, Deal.Audience.ONE_RANDOM, false, pitch, acceptLine) {
            @Override
            protected void resolve(Dragonling dragon, List<Player> acceptors,
                                   List<Player> decliners, List<Player> everyone) {
                Player conspirator = acceptors.get(0);
                Player victim = (everyone.get(0) == conspirator) ? everyone.get(1) : everyone.get(0);

                int dealt = safeDamage(victim, dragon.attack());
                int stolen = victim.getCoins() / 2;

                victim.sendMessage(conspirator.getName() + " and " + dragon.getType()
                        + " turn on you! You take " + dealt + " damage.");
                conspirator.sendMessage("You and " + dragon.getType() + " jump " + victim.getName() + ".");

                if (stolen > 0) {
                    victim.coinChange(-stolen);
                    conspirator.coinChange(stolen);
                }
                dragon.concede();
            }
        };
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    /** Direct damage that ignores armour and stance but never kills. Returns the damage dealt. */
    private static int safeDamage(Player player, int damage) {
        int dealt = Math.max(0, Math.min(damage, player.getHealth() - 1));
        player.antiHeal(dealt);
        return dealt;
    }

    private static List<Item> gearOf(Player player) {
        List<Item> gear = new ArrayList<>(player.getInventory().getItems());
        gear.add(player.getEquipment().getEquippedWeapon());
        gear.add(player.getEquipment().getEquippedArmour());
        return gear;
    }

    private static boolean hasDamagedGear(Player player) {
        for (Item item : gearOf(player)) {
            if (item.isBreakable() && item.getDurability() < item.getMaxDurability()) {
                return true;
            }
        }
        return false;
    }

    private static void repairGear(Player player) {
        for (Item item : gearOf(player)) {
            if (item.isBreakable()) {
                item.setDurability(item.getMaxDurability());   // resets current to max
            }
        }
    }

    private static int countPotions(Player player) {
        int count = 0;
        for (Item item : player.getInventory().getItems()) {
            if (item instanceof Potion) count++;
        }
        return count;
    }
}
