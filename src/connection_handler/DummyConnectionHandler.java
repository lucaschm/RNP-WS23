package connection_handler;

import data_classes.ChatMessage;

public class DummyConnectionHandler implements IConnectionHandler{

    int nextMessageCounter;
    
    public DummyConnectionHandler() {
        nextMessageCounter = 0;
    }

    public ChatMessage nextMessage() {
        ++nextMessageCounter;
        String content = "Hello there, this is Test Message Number " + nextMessageCounter;

        return new ChatMessage(
            "127.0.0.1", 
            8080, 
            "127.0.0.1", 
            6060, 
            1,
            content
            );
    }
}
