package me.taff_s.game.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.taff_s.game.player.Inventory;
import me.taff_s.game.player.EquippedItems;
import me.taff_s.game.player.PlayerHandler;
import me.taff_s.game.items.Item;
import me.taff_s.game.items.Usable;
import me.taff_s.game.items.Equippable;
import me.taff_s.game.items.potions.Timed;
import me.taff_s.game.items.potions.PotionEffect;
import me.taff_s.game.items.weapons.Weapon;
import me.taff_s.game.items.weapons.WeaponLibrary;
import me.taff_s.game.items.armour.Armour;
import me.taff_s.game.items.armour.ArmourLibrary;
import me.taff_s.game.items.charms.Charm;
import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.core.GameEventManager;
import me.taff_s.game.core.GameEvent;
import me.taff_s.game.core.CoinChangeData;

public class Player {

    /*==================================================
      FIELDS & ATTRIBUTES
    ==================================================*/
    String name;

    protected int health;
    int maxHealth;
    int coins;

    Inventory inventory = new Inventory();
    EquippedItems equipment;

    private List<Timed> activeTimedEffects = new ArrayList<>();

    boolean dStance;
    public int dStanceCounter = 2;

    int strengthMod = 0;
    public double defenceMod = 1;

    RestChoice sleepType;

    private transient PlayerHandler handler;
    public Player partner;

    private GameEventManager eventManager;


    /*==================================================
      CONSTRUCTORS
    ==================================================*/

    /**
     * Constructs a Player with only an event manager.
     * Useful for initializing and setting properties later.
     */
    public Player(GameEventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Constructs a fully initialized Player with basic starting equipment.
     */
    public Player(String name, int health, int maxHealth, int coins, GameEventManager eventManager) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.coins = coins;
        this.inventory = new Inventory();
        this.equipment = new EquippedItems();
        this.eventManager = eventManager;

        // Starting equipment
        equipment.setWeapon(WeaponLibrary.butterKnife);
        equipment.setArmour(ArmourLibrary.clothShirt);
    }


    /*==================================================
      GETTERS & SETTERS
    ==================================================*/

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EquippedItems getEquipment() {
        return equipment;
    }

    public List<Charm> getEquippedCharms() {
        List<Charm> equippedCharmsList = new ArrayList<>();
        if (equipment.getCharm1() != null) equippedCharmsList.add(equipment.getCharm1());
        if (equipment.getCharm2() != null) equippedCharmsList.add(equipment.getCharm2());
        return equippedCharmsList;
    }

    public int getCoins() {
        return coins;
    }

    public boolean getStance() {
        return dStance;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int newHealth) {
        maxHealth = newHealth;
    }

    public int getStrengthMod() {
        return strengthMod;
    }

    public Player getPartner() {
        return partner;
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public RestChoice getRestType() {
        return sleepType;
    }

    public void setRestType(RestChoice choice) {
        this.sleepType = choice;
    }


    /*==================================================
      HANDLER METHODS
    ==================================================*/

    /**
     * Sets the handler for player interactions.
     */
    public void setHandler(PlayerHandler handler) {
        this.handler = handler;
    }

    public PlayerHandler getHandler() {
        return handler;
    }

    public void sendMessage(String msg) {
        if (handler != null) handler.getMessenger().send(msg);
    }

    public String userInput(String msg) {
        if (handler != null) {
            return handler.getMessenger().prompt(msg);
        }
        return "";
    }


    /*==================================================
      MONEY METHODS
    ==================================================*/

    /**
     * Adjusts the player's coin count and notifies listeners.
     */
    public void coinChange(int amount) {
        if (amount >= 0) {
            sendMessage("You've gained " + amount + " coins!");
        } else {
            sendMessage("You've lost " + Math.abs(amount) + " coins!");
        }
        coins += amount;

        eventManager.notify(
            new GameEvent(GameEvent.EventType.COINS_CHANGED, new CoinChangeData(this, amount))
        );
    }


    /*==================================================
      COMBAT METHODS
    ==================================================*/

    /**
     * Checks if the player is still alive.
     */
    public boolean living() {
        return this.health > 0;
    }

    /**
     * Attacks an enemy using the equipped weapon.
     */
    public String attack(Enemy enemy, Weapon equippedWeapon) {
        int dmg = equippedWeapon.getDamage() + strengthMod;
        enemy.isHit(dmg, equippedWeapon.getDamageType());
        equippedWeapon.reduceDurability(1);
        return name + " attacks " + enemy.getType() + " for " + dmg + " damage!";
    }

    /**
     * Attacks another player, factoring in defense.
     */
    public String attackPlayer(Player target, Weapon equippedWeapon, boolean defence) {
        int dmg = equippedWeapon.getDamage() + strengthMod;
        equippedWeapon.reduceDurability(1);
        target.takeDamage(dmg, target.getEquipment().getEquippedArmour(), defence, true);
        return name + " attacks " + target.getName() + " for " + dmg + " damage!";
    }

    /**
     * Puts the player into a defensive stance.
     */
    public String defend() {
        dStance = true;
        return name + " takes a defensive stance!";
    }

    /**
     * Handles taking damage, including armor, defensive stances, and notifying partners.
     */
    public void takeDamage(int dmg, Armour equippedArmour, boolean isDefending, boolean notifyPartner) {
        int actual = (int) Math.round(dmg * equippedArmour.getArmourVal() * defenceMod);

        if (isDefending) {
            if (dStanceCounter > 0) {
                // Perfect block
                actual = 0;
                dStance = false;
                sendMessage(name + " blocked the attack!");
                if (notifyPartner && partner != null) {
                    partner.sendMessage(name + " blocked the attack!");
                }
                dStanceCounter -= 1;
            } else {
                // Imperfect block
                actual = actual / 2;
                dStance = false;
                sendMessage(name + " could only partially block the attack!");
                if (notifyPartner && partner != null) {
                    partner.sendMessage(name + " could only partially block the attack!");
                }
            }
        } else {
            sendMessage(name + " takes " + actual + " damage.");
        }

        health -= actual;
        if (health < 0) health = 0;
    }


    /*==================================================
      ITEM METHODS
    ==================================================*/

    /**
     * Adds an item to the player's inventory.
     */
    public void pickUpItem(Item item) {
        inventory.addItem(item);
    }

    /**
     * Uses or equips an item depending on its type.
     */
    public void useItem(Item item) {
        if (item instanceof Usable) {
            ((Usable) item).use(this);
            inventory.removeItem(item);
        } else if (item instanceof Equippable) {
            ((Equippable) item).equip(this);
        } else {
            sendMessage("This item can't be used or equipped.");
        }
    }


    /*==================================================
      POTION & EFFECT METHODS
    ==================================================*/

    /**
     * Applies a potion effect to the player, including timed effects.
     */
    public void applyEffect(PotionEffect effect) {
        effect.apply(this);
        if (effect instanceof Timed) {
            activeTimedEffects.add((Timed) effect);
        }
    }

    /**
     * Updates all active timed effects at the start of the player's turn.
     */
    public void onTurnStart() {
        Iterator<Timed> iterator = activeTimedEffects.iterator();
        while (iterator.hasNext()) {
            Timed effect = iterator.next();
            effect.onTurnStart(this);
            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }

    /**
     * Heals the player without exceeding max health.
     */
    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
    }

    /**
     * Damages the player without going below zero.
     */
    public void antiHeal(int amount) {
        health = Math.max(health + amount, 0);
    }

    /**
     * Modifies the player's strength modifier.
     */
    public void changeStrength(int amount) {
        strengthMod += amount;
    }

}
