public class CoinDisplayObserver implements GameEventObserver {
    @Override
    public void onEvent(GameEvent event) {
        if (event.getData() instanceof Integer) {
            int coins = (Integer) event.getData();
            System.out.println("Coins banked this run: " + coins);
        } 
    }
}