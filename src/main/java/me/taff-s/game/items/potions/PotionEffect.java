public interface PotionEffect {
    void apply(Player player);

    default void onTurnStart(Player player) {}
    default boolean isExpired() {
        return false;
    }
}

