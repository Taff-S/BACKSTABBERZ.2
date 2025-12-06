package me.taff_s.game.enemies;

import java.util.EnumMap;
import me.taff_s.game.player.Player;

import me.taff_s.game.items.weapons.DamageType;

public abstract class Enemy{
    public String type;
    public int health;
    public int maxHealth;
    private int minDamage;
    private int maxDamage;
    private int coinDrop;
    //private int defence; //with armour
    //protected Armour armour = null;
    protected boolean armoured = false; //whether the enemy has armour
    protected int naturalDefence; // without armour
    protected boolean armourBroken = false;
    protected boolean defenceDrop = false; //temporary reduction from axe weapon type
    protected EnumMap<DamageType, Double> damageModifiers = new EnumMap<>(DamageType.class);
    public String variant;
    protected int armourHealth = 0;
    protected int maxArmourHealth = 0;

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
                finalDmg -= armourHealth;
                armourHealth = 0;
                armourBroken = true;
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

    public void reduceAttack(int amount) {
        this.minDamage = Math.max(0, this.minDamage - amount);
        this.maxDamage = Math.max(this.minDamage, this.maxDamage - amount);
    }

    public void reduceDefence(int amount) {
        this.naturalDefence = Math.max(0, this.naturalDefence - amount);
    }

    public void applyConcussion(int turns) {
        // Placeholder: concussion mechanics not implemented yet
    }

    public void reduceArmour(int amount, int turns) {
        if (this.armoured) {
            this.armourHealth = Math.max(0, this.armourHealth - amount);
            if (this.armourHealth == 0) this.armourBroken = true;
        }
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

