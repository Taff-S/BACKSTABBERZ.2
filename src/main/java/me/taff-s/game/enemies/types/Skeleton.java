public class Skeleton extends Enemy {
    
    public Skeleton() {
        super("standard","Skeleton", 10, 10, 2, 2, 20, 2, false);
        setDamageModifier(DamageType.FORCE, 2.00); 
    }
    
    public Skeleton(String variant, String name, int health, int maxHealth, int minDamage, int maxDamage, int reward, int defence, boolean isArmoured) {
        super(variant, name, health, maxHealth,minDamage, maxDamage, reward, defence, isArmoured); 
        setDamageModifier(DamageType.FORCE, 2.00); 
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A skeleton shambles in front of you");
    }

    
    public String enemyStatus(){
        double healthPercent = (double) health/ (double) maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " is looking primed and ready";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " is still raring to fight";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " is looking shaky";
        }
        else if (healthPercent <= 0.25){
            message = type + " is barely holding on";
        }
        return message;
    }

    public String getVariant() {
        return variant;
    }
}