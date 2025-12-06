package me.taff_s.game.enemies.types;

import me.taff_s.game.player.Player;
import me.taff_s.game.enemies.Enemy;

public class Thief extends Enemy {

    public Thief() {
        super("standard","Thief", 12, 12, 1, 3, 35,2, false);
    }
    
    public Thief(String variant, String name, int health, int maxHealth, int minDamage, int maxDamage, int reward, int defence, boolean isArmoured) {
        super(variant, name, health, maxHealth,minDamage, maxDamage, reward, defence, isArmoured); 
    }
    
    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A thief rummages around the floor. When you clear your throat, they leap at you!");
        enemyStatus();
    }
    
    public String enemyStatus(){
        double healthPercent = (double) health/ (double) maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " swings their knife wildly";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " is still quick on their feet";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " shows signs of tiring";
        }
        else if (healthPercent <= 0.25){
            message = type + " barely stays on their feet";
        }
        return message;
    }

    public String getVariant() {
        return variant;
    }

}