package de.haw.rn.luca_steven.data_classes;

public class MessagePack {
    String ip;
    int port;
    String message;


    public MessagePack(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
            this.message = message;
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
}
    
