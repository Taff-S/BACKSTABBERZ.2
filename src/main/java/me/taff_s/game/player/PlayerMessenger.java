// package me.taff_s.game.player;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.PrintWriter;

// public class PlayerMessenger {
//     private final PrintWriter out;
//     private final BufferedReader in;
//     private boolean connected = true;

//     public PlayerMessenger(PrintWriter out, BufferedReader in) {
//         this.out = out;
//         this.in = in;
//     }

//     public void send(String message) {
//         if (!connected) return;
//         try {
//             out.println(message);
//             out.flush();
//         } catch (Exception e) {
//             System.err.println("Failed to send message: " + e.getMessage());
//             connected = false;
//         }
//     }

//     public void sendMulti(String... message) {
//         if (!connected) return;
//         try {
//             out.println(String.join("\n", message));
//             out.flush();
//         } catch (Exception e) {
//             System.err.println("Failed to send message: " + e.getMessage());
//             connected = false;
//         }
//     }

//     public String prompt(String message) {
//         send(message);
//         send(">"); // Prompt marker
//         return readLine();
//     }

//     public String promptMulti(String... lines) {
//         StringBuilder sb = new StringBuilder();
//         for (String line : lines) {
//             sb.append(line).append("\n");
//         }
//         sb.append(">");
//         send(sb.toString());
//         return readLine();
//     }

//     public String readLine() {
//         if (!connected) return null;
//         try {
//             String input = in.readLine();
//             if (input == null) {
//                 connected = false;
//                 System.err.println("Client disconnected.");
//             }
//             return input;
//         } catch (IOException e) {
//             connected = false;
//             System.err.println("Error reading input: " + e.getMessage());
//             return null;
//         }
//     }

//     public boolean isConnected() {
//         return connected;
//     }
// }
package me.taff_s.game.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles low-level I/O for a single player's socket connection.
 *
 * Convention:
 *   - send*(...)   → writes text to the client, no input expected
 *   - prompt(...)  → writes text, then sends the prompt marker ">",
 *                    then reads and returns one line of input
 *
 * The ">" marker is ONLY ever sent by prompt methods, never by send methods.
 * GameClient reads lines one at a time and treats a line that is exactly ">"
 * as the signal to read user input — so the ">" MUST be sent via println,
 * not print, otherwise the client's readLine() will block forever.
 */
public class PlayerMessenger {
    private final PrintWriter out;
    private final BufferedReader in;
    private boolean connected = true;

    public PlayerMessenger(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    // ------------------------------------------------------------------ //
    //  SEND  (fire-and-forget, no input expected)
    // ------------------------------------------------------------------ //

    /** Send a single line. */
    public void send(String message) {
        if (!connected) return;
        out.println(message);
        out.flush();
    }

    /** Send several lines joined by newlines. */
    public void sendMulti(String... lines) {
        if (!connected) return;
        out.println(String.join("\n", lines));
        out.flush();
    }

    // ------------------------------------------------------------------ //
    //  PROMPT  (send message, send ">" on its own line, wait for input)
    // ------------------------------------------------------------------ //

    /**
     * Sends {@code message} (if non-empty), then sends ">" on its own line
     * as a prompt marker, blocks until the client replies, and returns the input.
     * Returns {@code null} if the client has disconnected.
     */
    public String prompt(String message) {
        if (!connected) return null;
        if (message != null && !message.isEmpty()) {
            out.println(message);
        }
        out.println(">");   // must be println — client uses readLine() to detect this
        out.flush();
        return readLine();
    }

    /**
     * Sends several lines, then ">" on its own line, and returns the client's reply.
     */
    public String promptMulti(String... lines) {
        if (!connected) return null;
        out.println(String.join("\n", lines));
        out.println(">");   // must be println — client uses readLine() to detect this
        out.flush();
        return readLine();
    }

    // ------------------------------------------------------------------ //
    //  INTERNAL
    // ------------------------------------------------------------------ //

    /** Read one raw line from the socket; marks disconnected on EOF or error. */
    public String readLine() {
        if (!connected) return null;
        try {
            String input = in.readLine();
            if (input == null) {
                connected = false;
                System.err.println("Client disconnected (EOF).");
            }
            return input;
        } catch (IOException e) {
            connected = false;
            System.err.println("Error reading from client: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}