package me.taff_s.game.player;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import me.taff_s.game.player.Player;
import me.taff_s.game.player.PlayerMessenger;
import me.taff_s.game.core.GameEventManager;

public class PlayerHandler implements Runnable {
    private final Socket socket;
    private final int playerId;

    private Player player;
    private PlayerMessenger messenger;

    private final ConcurrentHashMap<Integer, Boolean> readyPlayers;
    private final ConcurrentHashMap<Integer, Player> playerObjects;
    private final ConcurrentHashMap<Integer, BufferedReader> inputReaders;
    private final ConcurrentHashMap<Integer, PrintWriter> players;
    private final ConcurrentHashMap<Integer, Socket> playerSockets;

    public PlayerHandler(Socket socket, int playerId,
                         ConcurrentHashMap<Integer, Boolean> readyPlayers,
                         ConcurrentHashMap<Integer, Player> playerObjects,
                         ConcurrentHashMap<Integer, BufferedReader> inputReaders,
                         ConcurrentHashMap<Integer, PrintWriter> players,
                         ConcurrentHashMap<Integer, Socket> playerSockets) {
        this.socket = socket;
        this.playerId = playerId;
        this.readyPlayers = readyPlayers;
        this.playerObjects = playerObjects;
        this.inputReaders = inputReaders;
        this.players = players;
        this.playerSockets = playerSockets;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            messenger = new PlayerMessenger(out, in);

            messenger.send("*** WELCOME TO BACKSTABBERS ***");

            String playerName = messenger.prompt("Please enter your name");
            this.player = new Player(playerName, 20, 20, 50, new GameEventManager());
            this.player.setHandler(this);

            playerObjects.put(playerId, player);
            inputReaders.put(playerId, in);
            players.put(playerId, out);
            playerSockets.put(playerId, socket);
            readyPlayers.put(playerId, false);

            messenger.send("Welcome " + playerName + "!");

            waitForReady();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForReady() throws IOException {
        while (true) {
            String input = messenger.prompt("Press '1' to confirm you are ready");
            if (input == null) break;
            if (input.equals("1")) {
                readyPlayers.put(playerId, true);
                messenger.send("You are ready! Waiting for the other player...");
                break;
            }
        }
    }

    public PlayerMessenger getMessenger() {
        return messenger;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPartner(Player partner) {
        player.setPartner(partner);
    }

    // Convenience delegations to the messenger so other classes can use the handler
    public void send(String message) {
        if (messenger != null) messenger.send(message);
    }

    public String prompt(String message) {
        if (messenger == null) return null;
        return messenger.prompt(message);
    }

    public void sendMulti(String... lines) {
        if (messenger == null) return;
        messenger.promptMulti(lines); // show multi-line and prompt; return value ignored
    }

    public String promptMulti(String... lines) {
        if (messenger == null) return null;
        return messenger.promptMulti(lines);
    }
}
