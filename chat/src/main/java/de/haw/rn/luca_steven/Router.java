package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.IConnectionHandler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntrySet;

public class Router {

    IConnectionHandler connections;
    IRoutingTable table;
    JsonParser parser;

    private final int INIT_HOP_COUNT = 1; 

    public Router(IConnectionHandler connectionHandler, JsonParser parser) {
        this.connections = connectionHandler;
        this.parser = parser;
        this.table = new RoutingEntrySet();
    }

    /*
     * If the connection Handler has received a new Message it is either forwarded to the next Server or the Message is returned 
     */
    public ChatMessage processMessage() {
        Message message;
        if (connections.hasNext()) {
            String s = connections.nextString();
            message = parser.convertJsonStringToMessage(s);
        } else {
            return null;
        }
        
        if (!message.isChatMessage()) {
            RoutingMessage rm = (RoutingMessage) message;
            table.mergeWith(rm.getSetOfRoutingEntries(), rm.getFullOriginAddress());
            return null;
        } else {
            ChatMessage cm = (ChatMessage) message;
            if (parser.isForMe(cm)) {
                return cm;
            } else {
                forward(cm);
                return null;
            }
        }      
    }

    private void forward(ChatMessage message) {
        connections.sendString(parser.convertChatMessageToJsonString(message));
    }

    public void connect(String IP, int port) {
        connections.connect(IP, port);
        String ipPort = IP + ":" + port;
        table.addEntry(new RoutingEntry(
            ipPort, 
            INIT_HOP_COUNT,
            ipPort,
            parser.thisClient
        ));
    }

    public void send(String ip, int port, ChatMessage message) {
        connections.sendString(ip);
    }
}
