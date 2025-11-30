public class Slime extends Enemy {

    public Slime() {
        super("standard","Slime", 6, 6, 1, 1, 8,0, false);
        setDamageModifier(DamageType.FORCE, 0.50); 
        setDamageModifier(DamageType.PIERCE, 0.50); 
    }

    public Slime(String variant, String name, int health, int maxHealth, int minDamage, int maxDamage, int reward, int defence, boolean isArmoured) {
        super(variant, name, health, maxHealth,minDamage, maxDamage, reward, defence, isArmoured); 
        setDamageModifier(DamageType.FORCE, 0.50);
        setDamageModifier(DamageType.PIERCE, 0.50);  
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A slime wobbles in front of you");
        enemyStatus();
    }
    
    public String enemyStatus(){
        double healthPercent = (double) health/ (double) maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " burbles proudly";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " jiggles a little";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " is losing viscosity";
        }
        else if (healthPercent <= 0.25){
            message = type + " is little more than a puddle on the floor";
        }
        return message;
    }

    public String getVariant() {
        return variant;
    }
}