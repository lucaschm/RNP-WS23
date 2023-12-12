package de.haw.rn.luca_steven;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.ui.Command.*;
import de.haw.rn.luca_steven.ui.UI;
import de.haw.rn.luca_steven.ui.UserCommand;

public class Controller {
    String ip;
    int port;

    public Controller(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        
        ConnectionHandler connectionHandler = new ConnectionHandler(port);
        JsonParser parser = new JsonParser(ip);
        Router router = new Router(connectionHandler, parser);
        UI ui = new UI();

        while(true) {
            connectionHandler.listen();
            UserCommand com = ui.getUserCommand();
            switch (com.getCommand()) {
                case CONNECT:
                    router.connect(ip, port);
                    break;
                case SEND:
                    router.send(ip, port, message);
                    break;
                case DISCONNECT:
                    
                    break;
                case LIST:
                
                break;
                case EXIT:
                
                break;
                default:
                    break;
            }

            // String messageString = connectionHandler.nextMessage();
            // ChatMessage chatMessage = jsonParser.convertStringToChatMessage(messageString);
            //TODO: ChatMessage an Router weitergeben
        }
    }
}
