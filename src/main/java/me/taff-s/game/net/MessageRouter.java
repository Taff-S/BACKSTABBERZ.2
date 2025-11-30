//Mostlty Unused
import java.io.PrintWriter;
import java.util.Map;

public class MessageRouter {
    private static Map<Integer, PrintWriter> players;

    public static void initialize(Map<Integer, PrintWriter> playerMap) {
        players = playerMap;
    }

    public static void broadcast(String message) {
        for (PrintWriter writer : players.values()) {
            writer.println(message);
        }
    }

    public static void sendToPlayer(int playerId, String message) {
        PrintWriter writer = players.get(playerId);
        if (writer != null) {
            writer.println(message);
        }
    }

    public static void serverLog(String message) {
        System.out.println("[SERVER LOG] " + message);
    }

    public static void debug(String message) {
        broadcast("[DEBUG] " + message);
        serverLog("[DEBUG] " + message);
    }
}
