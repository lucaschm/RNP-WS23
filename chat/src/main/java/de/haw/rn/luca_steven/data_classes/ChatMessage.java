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

    public String getFullDestinationAddress() {
        return destinationIP + ":" + destinationIDPort;
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

    public void decrementTtl() {
        ttl--;
    }

    public String getContent() {
        return content;
    }

    public boolean isForMe(String myIPPort) {
        String myIPPortAlias = "127.0.0.1" + ":" + myIPPort.split(":")[1];
        if (getFullDestinationAddress().equals(myIPPortAlias)) {
            return true;
        }
        return myIPPort.equals(getFullDestinationAddress());
    }

}
