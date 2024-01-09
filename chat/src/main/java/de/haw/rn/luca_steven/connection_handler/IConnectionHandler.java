package de.haw.rn.luca_steven.connection_handler;

import java.util.Map;
import java.util.Set;

import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;


public interface IConnectionHandler {
    /**
     * Aus der Queue wird die nächste Nachricht entnommen
     * @return
     */
    public String nextString();

    /**
     * true if another message is ready to be received
     * @return
     */
    public boolean hasNext();

    /**
     * Verbindung mit anderem Client aufbauen
     */
    public void connect(String ipAddress, int port);

    /**
     * Verbindung mit anderem Client abbauen
     */
    public void disconnect(String ipAddress, int port, boolean all);

    /**
     * Listen: Für andere Clients zum Verbindungsaufbau bereitstellen
     */
    public void listen();

    /**
     * Nachricht an einen anderen Client senden
     */
    public void sendString(String ip, int port, String s);

    /**
     * Map<"remoteIP:remotePort", "localPort">
     */
    public Map<String, String> getAllConnectionsWithLocalPort();

    public String getLocalIP();

    public int getLocalIDPort();

    public boolean hasError();

    public String getError();

    public Set<RoutingEntry> ckeckConnections(Set<RoutingEntry> neighbours);
}
