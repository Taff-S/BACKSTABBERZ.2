package me.taff_s.game.enemies.types;

import me.taff_s.game.enemies.Enemy;
import me.taff_s.game.player.Player;

public class Dragon extends Enemy {
    

    public Dragon() {
        super("standard","The Golden Dragon", 30, 30, 5, 8, 300, 2, false); // 5-8 damage
    }

    // public void resetDragonState() {
    //     runCount = 0;
    // }

    @Override
    public void display(Player player) {
        player.sendMessage("The Golden Dragon towers above you");
        player.sendMessage(enemyStatus());
    }
    
    public String enemyStatus(){
        double healthPercent = (double) health/maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " grins with eternal malice";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " is enjoying the battle";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " is beginning to slow down";
        }
        else if (healthPercent <= 0.25){
            message = type + " pants when he thinks you're not looking";
        }
        return message;
    }
    
}