public class FeatherCharm extends Charm{
    public FeatherCharm() {
        super("The Feather", "Allows you to sleep a little comfier", 50);
    }

    @Override
    public void onRest(Player player) {
        player.heal(2); 
    }
}