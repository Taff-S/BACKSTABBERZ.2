public class Kobold extends Enemy {
    
    public Kobold(){
        super("standard", "Kobold", 8, 8, 3, 4, 18, 1, false);
        setDamageModifier(DamageType.SLASH, 2.00); 
    }
    public Kobold(String variant, String name, int health, int maxHealth, int minDamage, int maxDamage, int reward, int defence, boolean isArmoured) {
        super(variant, name, health, maxHealth,minDamage, maxDamage, reward, defence, isArmoured); 
        setDamageModifier(DamageType.SLASH, 2.00);
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A kobold perks up at your footsteps.");
        enemyStatus();
    }
    
    public String enemyStatus(){
        double healthPercent = (double) health/ (double) maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " snaps its jaws at you";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " weaves around the room";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " gnashes their teeth at you";
        }
        else if (healthPercent <= 0.25){
            message = type + " is looking battered and bruised";
        }
        return message;
    }

    public String getVariant() {
        return variant;
    }
    //Chance to dodge
}