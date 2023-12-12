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
    String localIPPort;

    private final int INIT_HOP_COUNT = 1; 

    public Router(IConnectionHandler connectionHandler, String ipPort) {
        this.connections = connectionHandler;
        this.parser = new JsonParser();
        this.table = new RoutingEntrySet();
        this.localIPPort = ipPort;
    }

    public ChatMessage process() {
        ChatMessage result = processMessage();
        shareRoutingInformation();
        return result;
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
            if (cm.isForMe(localIPPort)) {
                return cm;
            } else {
                forward(cm);
                return null;
            }
        }      
    }

    public void shareRoutingInformation() {

        // für alle Nachbarn (hops = 1)
            // hole angepasste RoutingTabelle
            // formatiere aus der Tabelle ein Json
            // schicke es an den Nachbarn
    }

    private void forward(ChatMessage message) {
        String nextHop = table.findNextHop(message.getFullDestinationAddress());
        String[] ipPort = nextHop.split(":", 1);
        connections.sendString(ipPort[0], Integer.parseInt(ipPort[1]), parser.convertChatMessageToJsonString(message));
    }

    public void connect(String IP, int port) {
        connections.connect(IP, port);
        String remoteIPPort = IP + ":" + port;
        table.addEntry(new RoutingEntry(
            remoteIPPort, 
            INIT_HOP_COUNT,
            remoteIPPort,
            localIPPort
        ));
    }

    public void send(String ip, int port, ChatMessage message) {
        String s = parser.convertChatMessageToJsonString(message);
        connections.sendString(ip, port, s);
    }

    public void disconnect(String Ip, int port) {
        
    }
}
