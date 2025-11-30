public class GameLogger implements GameEventObserver {
    
    @Override
    public void onEvent(GameEvent event) {
        switch(event.getType()) {
            case COINS_BANKED:
                System.out.println("Coins banked this run: " + event.getData());
                break;
            case COINS_CHANGED:
                if (event.getData() instanceof CoinChangeData) {
                    CoinChangeData data = (CoinChangeData) event.getData();
                    System.out.println("Current Coin Count: " + data.player.getCoins() + " (change: " + data.delta + ")");
                }
                break;
        }
    }
}
