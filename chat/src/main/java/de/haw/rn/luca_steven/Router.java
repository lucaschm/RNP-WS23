package de.haw.rn.luca_steven;

import java.util.Map;
import java.util.Set;

import de.haw.rn.luca_steven.connection_handler.IConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.exceptions.DoubleConnectionException;
import de.haw.rn.luca_steven.connection_handler.exceptions.MessageNotSendException;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.Message;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingTableMapImpl;
import de.haw.rn.luca_steven.ui.Status;

public class Router {

    private final int ROUTING_SHARE_INTERVAL = Config.routingShareInterval; //in ms

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
        this.table = new RoutingTableMapImpl();
        this.localIPPort = ipPort;
        table.addEntry(new RoutingEntry(
            localIPPort, 
            INIT_HOP_COUNT,
            localIPPort,
            ipPort  
        ));   
        timestamp = System.currentTimeMillis();
    }

    public ChatMessage process() throws MessageNotSendException {
        Set<RoutingEntry> lostConnections = connections.ckeckConnections(table.getAllButSelfEntry());
        table.delete(lostConnections);
        ChatMessage result = processMessage();
        if (connections.hasError()) {
            throw new MessageNotSendException(connections.getError());
        }
        long timedif = System.currentTimeMillis() - timestamp;
        Status.routerProcess(timedif);
        if (Math.abs(timedif) > ROUTING_SHARE_INTERVAL) {
            Status.shareRoutingInformation(timestamp);
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

        // Wenn der JsonParser den String nicht lesen kann, kann die Nachricht null sein
        if (message == null) {
            return null;
        }
        
        if (!message.isChatMessage()) {
            RoutingMessage rm = (RoutingMessage) message;
            Status.routingMessageReceived(rm);
            table.mergeWith(rm.getSetOfRoutingEntries(), rm.getFullOriginAddress());
            
            return null;
        } else {
            ChatMessage cm = (ChatMessage) message;
            if (cm.isForMe(localIPPort)) {
                return checkTTL(cm);
            } else {
                Status.forwardMessage();
                cm = checkTTL(cm);
                if (cm != null) {
                    cm.decrementTtl();
                    send(cm);
                }
                return null;
            }
        }      
    }

    private ChatMessage checkTTL(ChatMessage cm) {
        if (cm.getTtl() <= 0) {
            Status.messageWithExpiredTTL();
            return null;
        }
        else {
            return cm;
        }
    }

    public void shareRoutingInformation() {

        Set<RoutingEntry> neighbours = table.getNeighbours();

        // vom ConnectionHandler alle Connections holen (inklusive eigenen Source Port)
        Map<String,String> connectionMap = connections.getAllConnectionsWithLocalPort();
        String localIP = connections.getLocalIP();
        String idPort = "" + connections.getLocalIDPort();


        if (neighbours.size() == 0) {
            Status.nothingToShare();
        }

        // für alle Nachbarn
        for (RoutingEntry entry : neighbours) {
            String nextHop = entry.getNextHop();
            
            // hole angepasste RoutingTabelle
            Set<RoutingEntry> splitHorizonTable = table.getEntriesWithout(entry);

            // formatiere aus der Tabelle ein Json
            String sourcePort = connectionMap.get(nextHop);

            if (sourcePort != null) {
                
                String tableJsonString = parser.buildRoutingTableJsonString(splitHorizonTable, localIP, sourcePort, idPort); 
        
                // schicke es an den Nachbarn
                String ip = nextHop.split(":")[0];
                int port = Integer.parseInt(nextHop.split(":")[1]);
                connections.sendString(ip, port, tableJsonString);
                Status.routingInformationSent(splitHorizonTable, ip, port);
            }
        }
    }

    public void connect(String IP, int port) {
        String remoteIPPort = IP + ":" + port;
        if (remoteIPPort.equals(localIPPort)){
            Status.selfConnection();
            return;
        }
        connections.connect(IP, port);
            table.addEntry(new RoutingEntry(
            remoteIPPort, 
            NEW_CONNECTION_HOP_COUNT,
            remoteIPPort,
            localIPPort
        ));
        // TODO: maybe a sleep is needed, so the 
        // neighbor can react
        shareRoutingInformation();
        
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
        String ipPort = IP + ":" + port;

        // Überprüfen, ob versucht wird mit sich selbst zu disconnecten
        if (localIPPort.equals(ipPort)) {
            //das muss verhindert werden
            Status.selfDisconnect();
            return;
        }

        // wenn es ein Nachbar ist, sollte vom Next Hop disconnected werden
        if (table.isNeighbor(ipPort)) {
            ipPort = table.findNextHop(ipPort);
        }
        String[] split = ipPort.split(":");
        String newIP = split[0];
        String newport = split[1];

        connections.disconnect(newIP, Integer.parseInt(newport), true);
        table.deleteAllFor(ipPort);
    }

    public void disconnectAll() {
        Status.disconnectAll();
        Set<RoutingEntry> allEntries = table.getAllButSelfEntry();
        for (RoutingEntry entry : allEntries) {
            String ip = entry.getDestinationIP();
            int port = entry.getDestinationIDPort();
            disconnect(ip, port);
        }
    }

    public Set<RoutingEntry> getParticipantsSet() {
        return table.getAllButSelfEntry();
    }
}
