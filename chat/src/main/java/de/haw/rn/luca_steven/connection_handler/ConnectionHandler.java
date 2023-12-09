package de.haw.rn.luca_steven.connection_handler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import javax.json.JsonObject;
import de.haw.rn.luca_steven.Logger;

public class ConnectionHandler implements IConnectionHandler{

    private int idPort; // Der Port, auf dem der Server hört

    public ConnectionHandler() {
        idPort = 8080;
    }

    @Override
    public ChatMessage nextMessage() {
        return null;
    }

    @Override
    public void connect(String ipAddress, int port) {

    }

    @Override
    public void disconnect(String ipAddress, int port) {

    }

    @Override
    public void listen() {
        //thread mit ConnectionListener
    }

    @Override
    public void sendMessage(JsonObject jsonObject) {

    }
}
