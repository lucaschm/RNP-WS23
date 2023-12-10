package de.haw.rn.luca_steven.connection_handler;
import java.util.LinkedList;;

public class ConnectionHandler implements IConnectionHandler{

    private int idPort; // Der Port, auf dem der Server hört

    public ConnectionHandler() {
        idPort = 8080;
    }

    @Override
    public String nextMessage() {
        return ConnectionListener.getMessageQueue().pop();
    }

    @Override
    public void connect(String ipAddress, int port) {

    }

    @Override
    public void disconnect(String ipAddress, int port) {

    }

    @Override
    public void listen() {
        //passiert automatisch
    }

    @Override
    public void sendMessage(String message) {
        //TODO: hier könnte man evtl einfach in eine Queue schreiben. Der 
        // ConnectionListener kann diese Queueu dann abarbeiten 
    }
}
