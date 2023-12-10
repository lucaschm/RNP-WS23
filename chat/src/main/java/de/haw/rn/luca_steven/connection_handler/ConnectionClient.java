package de.haw.rn.luca_steven.connection_handler;

import de.haw.rn.luca_steven.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionClient implements Runnable {

    private String destinationIP;  // Die Adresse des Servers
    private int destinationIDPort; // Der Port, auf dem der Server hört

    public void run() {

        try (Socket socket = new Socket(destinationIP, destinationIDPort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                Logger.log("Echo: " + in.readLine());
            }
        } catch (IOException e) {
            Logger.log("I/O-Fehler beim Verbinden zum Server: " + e.getMessage());
            System.exit(1);
        }
    }
}
