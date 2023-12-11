package de.haw.rn.luca_steven.data_classes;

import java.util.HashSet;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class RoutingMessage extends Message {
    private static final String IP = "ip";
    private static final String ID_PORT = "id_port";
    private static final String HOPS = "hops";

    private String originIP;
    private int originSourcePort;
    private int originIDPort;
    private JsonArray table;

    public RoutingMessage(String ip,
                       int sourcePort,
                       int idPort,
                       JsonArray table) {
        super(false);
        this.originIP = ip;
        this.originSourcePort = sourcePort;
        this.originIDPort = idPort;
        this.table = table;
    }

    public String getOriginIP() {
        return originIP;
    }

    public int getOriginSourcePort() {
        return originSourcePort;
    }

    public int getOriginIDPort() {
        return originIDPort;
    }

    public JsonArray getJsonTable() {
        return table;
    }

    public Set<RoutingEntry> getSetOfRoutingEntries() {
        Set<RoutingEntry> set = new HashSet<>();
        for (JsonValue tableEntry : table) {
            JsonObject jsonObject = tableEntry.asJsonObject();
            
            String destinationIP = jsonObject.getString(IP);
            int destinationIDPort = Integer.parseInt(jsonObject.getString(ID_PORT));
            int hops = Integer.parseInt(jsonObject.getString(HOPS));

            String destination = destinationIP + ":" + destinationIDPort;
            hops = hops + 1;
            String nextHop = originIP + ":" + originIDPort;
            String origin = nextHop;

            set.add(new RoutingEntry(destination, hops, nextHop, origin));
        }
        return set;
    }

}

