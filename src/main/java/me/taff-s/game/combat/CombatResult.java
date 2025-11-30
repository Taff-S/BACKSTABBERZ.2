import java.util.ArrayList;
import java.util.List;

public class CombatResult {
    private final List<String> events = new ArrayList<>();
    private boolean enemyDefeated;
    private boolean playerDefeated;

    public void addEvent(String event) {
        events.add(event);
    }

    public List<String> getEvents() {
        return events;
    }

    public boolean isEnemyDefeated() {
        return enemyDefeated;
    }

    public void setEnemyDefeated(boolean enemyDefeated) {
        this.enemyDefeated = enemyDefeated;
    }

    public boolean isPlayerDefeated() {
        return playerDefeated;
    }

    public void setPlayerDefeated(boolean playerDefeated) {
        this.playerDefeated = playerDefeated;
    }
}
