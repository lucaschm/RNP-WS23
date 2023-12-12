package de.haw.rn.luca_steven;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
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
        String ipPort = ip + ":" + port;
        Router router = new Router(connectionHandler, ipPort);
        UI ui = new UI();

        while(true) {
            connectionHandler.listen();
            UserCommand com = ui.getUserCommand();
            switch (com.getCommand()) {
                case CONNECT:
                    router.connect(com.getIp(), com.getPort());
                    break;
                case SEND:
                    ChatMessage msg = new ChatMessage(
                        com.getIp(), com.getPort(), 
                        ip, port, 
                        15, com.getMessageContent());
                    router.send(ip, port, msg);
                    break;
                case DISCONNECT:
                    router.disconnect(com.getIp(), com.getPort());
                    break;
                case LIST:
                    ui.printParticipantList(router.getParticipantsSet());
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
