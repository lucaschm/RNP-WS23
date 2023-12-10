package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;

public class Controller {

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler(6789);

        int i = 0;
        while(true) {
            //Logger.log(6789 + ": main while iteration " + i++);
            connectionHandler.listen();
        }
    }
}
