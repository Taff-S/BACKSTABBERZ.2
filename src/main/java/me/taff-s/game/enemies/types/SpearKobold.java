public class SpearKobold extends Kobold{
    public SpearKobold() {
        super("spear","Kobold", 8, 8, 4, 5, 18, 2, false); 
    }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A kobold perks up at your footsteps and pulls a shoddily crafted spear from its back.");
        enemyStatus();
    }
}
