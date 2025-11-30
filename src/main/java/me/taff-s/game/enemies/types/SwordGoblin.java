public class SwordGoblin extends Goblin{
    
    public SwordGoblin() {
        super("sword","Sword Goblin", 9, 9, 3, 6, 15, 1, false);
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A goblin stares up at you, wielding a large, crimson coated sword, grinning maniacally.");
        enemyStatus();
    }
}
