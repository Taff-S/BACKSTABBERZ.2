public class AcidSlime extends Slime{
    public AcidSlime() {
        super("acid","Acid Slime", 6, 6, 1, 3, 8,0, false);
        }

    @Override
    public void display(Player player) {
        player.sendMessage("You enter the next room. A bubbling slime wobbles in front of you");
        enemyStatus();
    }
        //maybe hits through armour?
}
