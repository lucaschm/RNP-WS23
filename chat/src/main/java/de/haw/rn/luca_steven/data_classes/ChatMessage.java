package de.haw.rn.luca_steven.data_classes;

public class ChatMessage extends Message {
    private String destinationIP;
    private int destinationIDPort;
    private String originIP;
    private int originIDPort;
    private int ttl; //Time-To-Live
    private String content;

    public ChatMessage(String destinationIP,
                       int destinationPort,
                       String sourceIP,
                       int sourcePort,
                       int ttl,
                       String content) {
        super(true);
        this.destinationIP = destinationIP;
        this.destinationIDPort = destinationPort;
        this.originIP = sourceIP;
        this.originIDPort = sourcePort;
        this.ttl = ttl;
        this.content = content;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public int getDestinationIDPort() {
        return destinationIDPort;
    }

    public String getOriginIP() {
        return originIP;
    }

    public int getOriginIDPort() {
        return originIDPort;
    }

    public int getTtl() {
        return ttl;
    }

    public String getContent() {
        return content;
    }


}
