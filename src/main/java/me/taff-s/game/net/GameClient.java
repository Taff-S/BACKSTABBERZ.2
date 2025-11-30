import java.io.*;
import java.net.*;

public class GameClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String lineFromServer;
            while ((lineFromServer = serverInput.readLine()) != null) {
                System.out.println("[Server]: " + lineFromServer);

                // Client expects a line that is exactly ">" to start input
                if (lineFromServer.trim().equals(">")) {
                    System.out.print("> ");
                    String userInput = terminalInput.readLine();

                    if (userInput == null || userInput.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting game...");
                        break;
                    }

                    writer.println(userInput);
                }
            }

            System.out.println("Disconnected from server.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

