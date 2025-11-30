public class GameEvent {
    public enum EventType {
        COINS_BANKED,
        COINS_CHANGED
    }

    private final EventType type;
    private final Object data;

    public GameEvent(EventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType() { 
        return type; 
    }
    public Object getData() { 
        return data; 
    }
}