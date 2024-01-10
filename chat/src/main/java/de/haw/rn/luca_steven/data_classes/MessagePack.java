package de.haw.rn.luca_steven.data_classes;

public class MessagePack {
    String ip;
    int port;
    String message;
    int localPort;


    public MessagePack(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        this.message = message;
        this.localPort = -1;
    }

    public MessagePack(String message, int localPort) {
        this.ip = "";
        this.port = -1;
        this.message = message;
        this.localPort = localPort;
    }

    public String getIPPort() {
        return ip + ":" + port;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLocalPort() {
        return this.localPort;
    }
}
    
