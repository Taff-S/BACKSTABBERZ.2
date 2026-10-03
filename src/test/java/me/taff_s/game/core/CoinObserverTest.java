package me.taff_s.game.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import me.taff_s.game.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CoinObserverTest {

    @Test
    void coinChangeNotifiesObserversWithPlayerAndDelta() {
        GameEventManager eventManager = new GameEventManager();
        List<GameEvent> events = new ArrayList<>();
        eventManager.addListener(events::add);
        Player player = new Player("Tester", 200, 200, 20, eventManager);

        player.coinChange(10);

        assertEquals(30, player.getCoins());
        assertEquals(1, events.size());
        assertEquals(GameEvent.EventType.COINS_CHANGED, events.get(0).getType());
        CoinChangeData data = assertInstanceOf(CoinChangeData.class, events.get(0).getData());
        assertEquals(player, data.player);
        assertEquals(10, data.delta);
    }

    @Test
    void coinDisplayObserverPrintsBankedCoinTotal() {
        CoinDisplayObserver observer = new CoinDisplayObserver();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut = new PrintStream(output)) {
            System.setOut(capturedOut);
            observer.onEvent(new GameEvent(GameEvent.EventType.COINS_BANKED, 42));
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("Coins banked this run: 42" + System.lineSeparator(), output.toString());
    }
}
