public class StoneSlime extends Slime {
    public StoneSlime() {
        super("stone","Stone Slime", 10, 10, 1, 1, 8,0, false);
        setDamageModifier(DamageType.FORCE, 2.00); 
        setDamageModifier(DamageType.SLASH, 0.50); 
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A stony slime rolls in front of you");
        enemyStatus();
    }
}