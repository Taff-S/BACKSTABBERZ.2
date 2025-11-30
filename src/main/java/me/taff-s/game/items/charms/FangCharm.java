public class FangCharm extends Charm {
    public FangCharm() {
        super("The Fang", "A shard from a dragon's fang. It feels lethal even nestled in your inventory.", 50);
    }

    @Override
    public void modifyStats(Player player) {
        player.changeStrength(1);
    }

}