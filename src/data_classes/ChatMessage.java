package data_classes;
//import java.net.Inet4Address;

import java.security.ProtectionDomain;

public class ChatMessage {
    private String destinationIP;
    private int destinationPort;
    private String sourceIP;
    private int sourcePort;
    private int ttl; //Time-To-Live
    private String content;

    public ChatMessage(String destinationIP,
                       int destinationPort,
                       String sourceIP,
                       int sourcePort,
                       int ttl,
                       String content) {
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.ttl = ttl;
        this.content = content;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getTtl() {
        return ttl;
    }

    public String getContent() {
        return content;
    }


}
