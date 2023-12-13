package de.haw.rn.luca_steven;

import java.util.Map;
import java.util.Set;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.IConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.exceptions.MessageNotSendException;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntrySet;

public class Router {

    private final int ROUTING_SHARE_INTERVAL = 100;

    IConnectionHandler connections;
    IRoutingTable table;
    JsonParser parser;
    String localIPPort;
    private long timestamp;

    private final int INIT_HOP_COUNT = 0; 
    private final int NEW_CONNECTION_HOP_COUNT = 1;

    public Router(IConnectionHandler connectionHandler, String ipPort) {
        this.connections = connectionHandler;
        this.parser = new JsonParser();
        this.table = new RoutingEntrySet();
        this.localIPPort = ipPort;
        table.addEntry(new RoutingEntry(
            localIPPort, 
            INIT_HOP_COUNT,
            localIPPort,
            ""
        ));
        timestamp = System.currentTimeMillis();
    }

    public ChatMessage process() throws MessageNotSendException {
        ChatMessage result = processMessage();
        if (connections.hasError()) {
            throw new MessageNotSendException(connections.getError());
        }

        if (Math.abs(System.currentTimeMillis() - timestamp) > ROUTING_SHARE_INTERVAL) {
            shareRoutingInformation();
            timestamp = System.currentTimeMillis();
        }        
        return result;
    }

    /*
     * If the connection Handler has received a new Message it is either forwarded to the next Server or the Message is returned 
     */
    public ChatMessage processMessage() throws MessageNotSendException {
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
                send(cm);
                return null;
            }
        }      
    }

    public void shareRoutingInformation() {

        Set<RoutingEntry> neighbours = table.getNeighbours();

        // vom ConnectionHandler alle Connections holen (inklusive eigenen Source Port)
        Map<String,String> connectionMap = connections.getAllConnectionsWithLocalPort();
        String localIP = connections.getLocalIP();
        String idPort = "" + connections.getLocalIDPort();

        // für alle Nachbarn
        for (RoutingEntry entry : neighbours) {
            String oneNeighbour = entry.getOrigin();
            String nextHop = entry.getNextHop();
            
            // hole angepasste RoutingTabelle
            Set<RoutingEntry> splitHorizonTable = table.getEntriesWithout(oneNeighbour);

            // formatiere aus der Tabelle ein Json
            //TODO: hier wurde nur gehofft, dass 'connectionMap.get(nextHop)' den richtigen source_port liefert
            String sourcePort = connectionMap.get(nextHop);

            if (sourcePort != null) {
                
                String tableJsonString = parser.buildRoutingTableJsonString(splitHorizonTable, localIP, connectionMap.get(nextHop), idPort); 
        
                // schicke es an den Nachbarn
                String ip = nextHop.split(":")[0];
                int port = Integer.parseInt(nextHop.split(":")[1]);
                connections.sendString(ip, port, tableJsonString);
            }
        }
    }

    public void connect(String IP, int port) {
        connections.connect(IP, port);
        String remoteIPPort = IP + ":" + port;
        table.addEntry(new RoutingEntry(
            remoteIPPort, 
            NEW_CONNECTION_HOP_COUNT,
            remoteIPPort,
            localIPPort
        ));
    }

    public void send(ChatMessage message) throws MessageNotSendException{
        String nextHop = table.findNextHop(message.getFullDestinationAddress());
        if(nextHop == null) {
            throw new MessageNotSendException("Der host " + message.getFullDestinationAddress() + "ist nicht in der Routing Tabelle drin");
        }
        String[] ipPort = nextHop.split(":", 2);
        String s = parser.convertChatMessageToJsonString(message);
        connections.sendString(ipPort[0], Integer.parseInt(ipPort[1]), s);
    }

    public void disconnect(String IP, int port) {
        connections.disconnect(IP, port);
        table.deleteAllFor(IP + ":" + port);
    }

    public Set<String> getParticipantsSet() {
        return table.getAllUniqueDestinations();
    }
}
