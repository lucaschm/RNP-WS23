package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;

public class Controller {

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler();

        while (true) {
            connectionHandler.listen();
        }
    }
}
