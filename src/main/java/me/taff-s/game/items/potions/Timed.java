public interface Timed {
    void onTurnStart(Player player);
    boolean isExpired();
}

