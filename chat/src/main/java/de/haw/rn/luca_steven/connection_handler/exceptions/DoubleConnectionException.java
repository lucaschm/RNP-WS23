package de.haw.rn.luca_steven.connection_handler.exceptions;

public class DoubleConnectionException extends Exception {
    String IP;
    int port;

    public DoubleConnectionException(String IPPort) {
        String[] IPPortA = IPPort.split(":", 2);
        this.IP = IPPortA[0];
        this.port = Integer.parseInt(IPPortA[1]);
    }

    public String getIP() {
        return this.IP;
    }

    public int getPort() {
        return this.port;
    }
}
