package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.IConnectionHandler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntrySet;

public class Router {

    IConnectionHandler connections;
    IRoutingTable table;
    JsonParser parser;

    public Router(IConnectionHandler connectionHandler) {
        connections = connectionHandler;
        parser = new JsonParser("127.0.0.1");
        table = new RoutingEntrySet();
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
}
