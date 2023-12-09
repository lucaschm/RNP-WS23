package de.haw.rn.luca_steven.connection_handler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import javax.json.JsonObject;

public class ConnectionHandler implements IConnectionHandler{
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

    }

    @Override
    public void sendMessage(JsonObject jsonObject) {

    }
}
