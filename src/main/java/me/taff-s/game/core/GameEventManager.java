import java.util.ArrayList;
import java.util.List;

public class GameEventManager {
    private final List<GameEventObserver> listeners = new ArrayList<>();

    public void addListener(GameEventObserver listener) {
        listeners.add(listener);
    }

    public void removeListener(GameEventObserver listener) {
        listeners.remove(listener);
    }

    public void notify(GameEvent event) {
        for (GameEventObserver listener : listeners) {
            listener.onEvent(event);
        }
    }
}
