package de.haw.rn.luca_steven.data_classes;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class RoutingMessage extends Message {
    private String ip;
    private int sourcePort;
    private int idPort;
    private JsonArray table;

    public RoutingMessage(String ip,
                       int sourcePort,
                       int idPort,
                       JsonArray table) {
        super(false);
        this.ip = ip;
        this.sourcePort = sourcePort;
        this.idPort = idPort;
        this.table = table;
    }

    public String getIp() {
        return ip;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getIdPort() {
        return idPort;
    }

    public JsonArray getJsonTable() {
        return table;
    }

}

