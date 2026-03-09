package me.taff_s.game.enemies;

import java.util.EnumMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import me.taff_s.game.player.Player;

import me.taff_s.game.items.weapons.DamageType;

import me.taff_s.game.effects.TimedEffect;

public abstract class Enemy{
    public String type;
    public int health;
    public int maxHealth;
    private int minDamage;
    private int maxDamage;
    private int coinDrop;

    protected boolean armoured = false; //whether the enemy has armour
    protected int naturalDefence; // without armour
    protected boolean armourBroken = false;

    protected boolean defenceDrop = false; //temporary reduction from axe weapon type
    protected EnumMap<DamageType, Double> damageModifiers = new EnumMap<>(DamageType.class);
    public String variant;
    protected int armourHealth = 0;
    protected int maxArmourHealth = 0;

    private final List<TimedEffect> timedEffects = new ArrayList<>();
    private boolean concussed = false;

    public Enemy(String variant, String name, int health, int maxHealth, int lowDmg, int highDmg, int reward, int defence, boolean isArmoured) {
        this.variant = variant;
        this.type = name;
        this.health = health;
        this.minDamage = lowDmg;
        this.maxHealth = maxHealth;
        this.maxDamage = highDmg;
        this.coinDrop = reward;
        this.naturalDefence = defence;
        this.armoured = isArmoured;
        if (isArmoured) {
            this.maxArmourHealth = defence * 5; // Example: scale armour health by defence
            this.armourHealth = this.maxArmourHealth;
        }
        initDefaultModifiers();
    }


    private void initDefaultModifiers() {
        for (DamageType dt : DamageType.values()) {
            damageModifiers.put(dt, 1.0); // neutral by default
        }
    }

    public void setDamageModifier(DamageType dt, double multiplier) {
        damageModifiers.put(dt, multiplier);
    }

    // public double getArmourVal() {
    //     double reduction = 0.05 * defence;
    //     double result = 1.0 - reduction;
    //     return Math.max(0.2, result); // prevents defense more than 80%
    // }

    // public double getEffectiveArmorMultiplier() {
    //     double totalDefense = naturalDefence;
    //     if (isArmoured && !armourBroken) {
    //         totalDefense += armour.getDefence();
    //     }
    //     double reduction = 0.05 * totalDefense;
    //     double result = 1.0 - reduction;
    //     if (defenceDrop) {
    //         result *= 1.25; // increases damage taken by 25%
    //     }       
    //     return Math.max(0.2, Math.min(2.0, result)); //damage capped at 2x
    // }


    public boolean getArmourBroken() {
        return armourBroken;
    }

    public void setArmourBroken(boolean status) {
        this.armourBroken = status;
    }

    public boolean living() {
        return health > 0;
    }

    public void isHit(int dmg, DamageType dt) {
        double effectiveness = damageModifiers.getOrDefault(dt, 1.0);
        int finalDmg = (int) Math.round(dmg * effectiveness);
        if (armoured && !armourBroken && armourHealth > 0) {
            if (finalDmg >= armourHealth) {
                armourHealth = 0;
                armourBroken = true;
                finalDmg = 0; // No overflow damage when armor breaks
            } else {
                armourHealth -= finalDmg;
                finalDmg = 0;
            }
        }
        if (armourBroken) {
            finalDmg = (int) Math.round(finalDmg * 1.25); // 25% more damage if armour is broken
        }
        health = Math.max(0, health - finalDmg);
    }
    
    public int attack() {
        return minDamage + (int)(Math.random() * (maxDamage - minDamage + 1));
    }

    public void reduceAttack(int amount, int turns) {
        this.minDamage = Math.max(0, this.minDamage - amount);
        this.maxDamage = Math.max(this.minDamage, this.maxDamage - amount);
    }

    public void reduceDefence(int amount, int turns) {
        this.naturalDefence = Math.max(0, this.naturalDefence - amount);
    }

    public void applyConcussion(int turns) {
        this.concussed = true;
        addEffect(new TimedEffect() {
            private int remaining = Math.max(0, turns);

            @Override
            public void onTurnStart(Object target) {
                remaining--;
                if (remaining <= 0) {
                    if (target instanceof Enemy) {
                        ((Enemy) target).concussed = false;
                    }
                }
            }

            @Override
            public boolean isExpired() {
                return remaining <= 0;
            }
        });
    }

    public void reduceArmour(int amount) {
        if (this.armoured) {
            this.armourHealth = Math.max(0, this.armourHealth - amount);
            if (this.armourHealth == 0) this.armourBroken = true;
        }
    }

    public void addEffect(TimedEffect effect) {
        if (effect != null) timedEffects.add(effect);
    }

    public void onTurnStart() {
        Iterator<TimedEffect> it = timedEffects.iterator();
        while (it.hasNext()) {
            TimedEffect e = it.next();
            e.onTurnStart(this);
            if (e.isExpired()) it.remove();
        }
    }

    public boolean isConcussed() {
        return concussed;
    }

    public void setDefence(int newDefence) {
        this.naturalDefence = newDefence;
    }

    public int getDefence() {
        // if (armour != null && !armourBroken) {
        //     return naturalDefence + armour.getDefence();
        // }
        return naturalDefence;
    }

    public int getCoinDrop() {
        return coinDrop;
    }

    public int getHealth() {
        return health;
    }

    public int getArmourHealth() {
        return armourHealth;
    }

    public int getMaxArmourHealth() {
        return maxArmourHealth;
    }

    public String getType() {
        return type;
    }

    public boolean isArmoured() {
        return armoured;
    }

    public abstract String enemyStatus();

    public abstract void display(Player player);
}

