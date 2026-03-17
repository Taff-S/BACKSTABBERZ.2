// package me.taff_s.game.player;

// import java.io.*;
// import java.net.*;
// import java.util.concurrent.*;
// // same-package classes don't need importing
// import me.taff_s.game.core.GameEventManager;

// public class PlayerHandler implements Runnable {
//     private final Socket socket;
//     private final int playerId;

//     private Player player;
//     private PlayerMessenger messenger;

//     private final ConcurrentHashMap<Integer, Boolean> readyPlayers;
//     private final ConcurrentHashMap<Integer, Player> playerObjects;
//     private final ConcurrentHashMap<Integer, BufferedReader> inputReaders;
//     private final ConcurrentHashMap<Integer, PrintWriter> players;
//     private final ConcurrentHashMap<Integer, Socket> playerSockets;

//     public PlayerHandler(Socket socket, int playerId,
//                          ConcurrentHashMap<Integer, Boolean> readyPlayers,
//                          ConcurrentHashMap<Integer, Player> playerObjects,
//                          ConcurrentHashMap<Integer, BufferedReader> inputReaders,
//                          ConcurrentHashMap<Integer, PrintWriter> players,
//                          ConcurrentHashMap<Integer, Socket> playerSockets) {
//         this.socket = socket;
//         this.playerId = playerId;
//         this.readyPlayers = readyPlayers;
//         this.playerObjects = playerObjects;
//         this.inputReaders = inputReaders;
//         this.players = players;
//         this.playerSockets = playerSockets;
//     }

//     public void run() {
//         try {
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             messenger = new PlayerMessenger(out, in);

//             messenger.send("*** WELCOME TO BACKSTABBERS ***");

//             String playerName = messenger.prompt("Please enter your name");
//             this.player = new Player(playerName, 20, 20, 50, new GameEventManager());
//             this.player.setHandler(this);

//             playerObjects.put(playerId, player);
//             inputReaders.put(playerId, in);
//             players.put(playerId, out);
//             playerSockets.put(playerId, socket);
//             readyPlayers.put(playerId, false);

//             messenger.send("Welcome " + playerName + "!");

//             waitForReady();

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private void waitForReady() throws IOException {
//         while (true) {
//             String input = messenger.prompt("Press '1' to confirm you are ready");
//             if (input == null) break;
//             if (input.equals("1")) {
//                 readyPlayers.put(playerId, true);
//                 messenger.send("You are ready! Waiting for the other player...");
//                 break;
//             }
//         }
//     }

//     public PlayerMessenger getMessenger() {
//         return messenger;
//     }

//     public Player getPlayer() {
//         return player;
//     }

//     public int getPlayerId() {
//         return playerId;
//     }

//     public void setPartner(Player partner) {
//         player.setPartner(partner);
//     }

//     // Convenience delegations to the messenger so other classes can use the handler
//     public void send(String message) {
//         if (messenger != null) messenger.send(message);
//     }

//     public String prompt(String message) {
//         if (messenger == null) return null;
//         return messenger.prompt(message);
//     }

//     public void sendMulti(String... lines) {
//         if (messenger == null) return;
//         messenger.promptMulti(lines); // show multi-line and prompt; return value ignored
//     }

//     public String promptMulti(String... lines) {
//         if (messenger == null) return null;
//         return messenger.promptMulti(lines);
//     }
// }



package me.taff_s.game.player;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import me.taff_s.game.core.GameEventManager;

/**
 * Manages the lifecycle of a single connected player.
 *
 * run() handles the initial handshake (name + ready confirmation) then returns,
 * freeing the thread. GameServer should call awaitReady() before proceeding.
 *
 * Delegation rules — always prefer calling these over touching the messenger directly:
 *
 *   handler.send(msg)              → PlayerMessenger.send(msg)
 *   handler.sendMulti(lines...)    → PlayerMessenger.sendMulti(lines...)
 *   handler.prompt(msg)            → PlayerMessenger.prompt(msg)      [blocks for input]
 *   handler.promptMulti(lines...)  → PlayerMessenger.promptMulti(...) [blocks for input]
 *   handler.getMessenger()         → direct messenger access (use sparingly)
 */
public class PlayerHandler implements Runnable {
    private final Socket socket;
    private final int playerId;

    private Player player;
    private PlayerMessenger messenger;

    // Signalled once this player has entered their name and pressed ready.
    // GameServer calls awaitReady() on both handlers before starting the game.
    private final CountDownLatch readyLatch = new CountDownLatch(1);

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

    // ------------------------------------------------------------------ //
    //  Runnable — initial handshake only; returns once player is ready
    // ------------------------------------------------------------------ //

    @Override
    public void run() {
        try {
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    out = new PrintWriter(socket.getOutputStream(), true);
            messenger = new PlayerMessenger(out, in);

            messenger.send("*** WELCOME TO BACKSTABBERS ***");

            String playerName = messenger.prompt("Please enter your name");
            if (playerName == null) return; // client disconnected during name entry

            playerName = playerName.trim();
            if (playerName.isEmpty()) playerName = "Player " + playerId;

            this.player = new Player(playerName, 20, 20, 50, new GameEventManager());
            this.player.setHandler(this);

            playerObjects.put(playerId, player);
            inputReaders.put(playerId, in);
            players.put(playerId, out);
            playerSockets.put(playerId, socket);

            messenger.send("Welcome, " + playerName + "!");

            waitForReady();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForReady() {
        while (true) {
            String input = messenger.prompt("Press '1' to confirm you are ready");
            if (input == null) break; // client disconnected
            if (input.trim().equals("1")) {
                readyPlayers.put(playerId, true);
                messenger.send("You are ready! Waiting for the other player...");
                readyLatch.countDown(); // unblocks awaitReady() in GameServer
                break;
            }
        }
    }

    /**
     * Blocks the calling thread until this player has completed the handshake,
     * or until the timeout elapses.
     *
     * @return true if the player became ready within the timeout, false otherwise
     */
    public boolean awaitReady(long timeout, TimeUnit unit) throws InterruptedException {
        return readyLatch.await(timeout, unit);
    }

    // ------------------------------------------------------------------ //
    //  Convenience delegation to PlayerMessenger
    // ------------------------------------------------------------------ //

    /** Send a single line — no input expected. */
    public void send(String message) {
        if (messenger != null) messenger.send(message);
    }

    /** Send multiple lines — no input expected. */
    public void sendMulti(String... lines) {
        if (messenger != null) messenger.sendMulti(lines);
    }

    /**
     * Send {@code message} then show "> " and wait for the player's reply.
     * Returns the input, or {@code null} on disconnect.
     */
    public String prompt(String message) {
        if (messenger == null) return null;
        return messenger.prompt(message);
    }

    /**
     * Send several lines then show "> " and wait for the player's reply.
     * Returns the input, or {@code null} on disconnect.
     */
    public String promptMulti(String... lines) {
        if (messenger == null) return null;
        return messenger.promptMulti(lines);
    }

    // ------------------------------------------------------------------ //
    //  Accessors
    // ------------------------------------------------------------------ //

    /** Direct messenger access — prefer the delegation methods above where possible. */
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
}