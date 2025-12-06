package me.taff_s.game.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PlayerMessenger {
    private final PrintWriter out;
    private final BufferedReader in;
    private boolean connected = true;

    public PlayerMessenger(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void send(String message) {
        if (!connected) return;
        try {
            out.println(message);
            out.flush();
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
            connected = false;
        }
    }

    public String prompt(String message) {
        send(message);
        send(">"); // Prompt marker
        return readLine();
    }

    public String promptMulti(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        sb.append(">");
        send(sb.toString());
        return readLine();
    }

    public String readLine() {
        if (!connected) return null;
        try {
            String input = in.readLine();
            if (input == null) {
                connected = false;
                System.err.println("Client disconnected.");
            }
            return input;
        } catch (IOException e) {
            connected = false;
            System.err.println("Error reading input: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
