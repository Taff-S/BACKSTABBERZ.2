public class Goblin extends Enemy {

    public Goblin(){
        super("standard","Goblin",8, 8, 2, 3, 15, 2, false);
    }
    public Goblin(String variant, String name, int health, int maxHealth, int minDamage, int maxDamage, int reward, int defence, boolean isArmoured) {
        super(variant, name, health, maxHealth,minDamage, maxDamage, reward, defence, isArmoured); 
    }
    
    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A goblin looks up and stares at you with beady red eyes.");
        enemyStatus();
    }
    
    public String enemyStatus(){
        double healthPercent = (double) health/ (double) maxHealth;
        String message = "";
        if (healthPercent ==1){
            message = type + " hops from side to side";    
        }
        else if (healthPercent > 0.5 && healthPercent <= 0.75){
            message = type + " grips its weapon and grins";
        }
        else if (healthPercent > 0.25 && healthPercent <= 0.5){
            message = type + " slows down its attacks";
        }
        else if (healthPercent <= 0.25){
            message = type + " barely stays on two legs";
        }
        return message;
        //chance to run away?
    }

    public String getVariant() {
        return variant;
    }
}