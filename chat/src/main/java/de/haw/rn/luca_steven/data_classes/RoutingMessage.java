package de.haw.rn.luca_steven.data_classes;

//import javax.json.JsonObject;

public class RoutingMessage {
    private String IP;
    private int sourcePort;
    private int IDPort;
    //private JsonObject table;

    public RoutingMessage(String IP,
                       int sourcePort,
                       int IDPort) {
        this.IP = IP;
        this.sourcePort = sourcePort;
        this.IDPort = IDPort;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getIDPort() {
        return IDPort;
    }

}

