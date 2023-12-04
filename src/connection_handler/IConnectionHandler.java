package connection_handler;

import data_classes.ChatMessage;

public interface IConnectionHandler {

    public ChatMessage nextMessage();
}
