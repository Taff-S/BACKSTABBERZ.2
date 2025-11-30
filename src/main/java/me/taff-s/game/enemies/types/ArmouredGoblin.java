public class ArmouredGoblin extends Goblin{
    public ArmouredGoblin() {
        super("armoured","Armoured Goblin",10, 10, 2, 3, 15, 5, true);
        setDamageModifier(DamageType.FORCE, 2.00); 
    }
    
    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A goblin, dressed in a cobbled together set of rusty iron armour looks up surprised and angrily raises its blade.");
        enemyStatus();
    }

    
}
