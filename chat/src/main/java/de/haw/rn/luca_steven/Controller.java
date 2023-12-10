package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;

public class Controller {

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler(6789);
        JsonParser jsonParser = new JsonParser();

        int i = 0;
        while(true) {
            //Logger.log(6789 + ": main while iteration " + i++);
            connectionHandler.listen();

            // String messageString = connectionHandler.nextMessage();
            // ChatMessage chatMessage = jsonParser.convertStringToChatMessage(messageString);
            //TODO: ChatMessage an Router weitergeben
        }
    }
}
