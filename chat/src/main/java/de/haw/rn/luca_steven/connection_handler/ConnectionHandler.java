package connection_handler;
import data_classes.ChatMessage;

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
