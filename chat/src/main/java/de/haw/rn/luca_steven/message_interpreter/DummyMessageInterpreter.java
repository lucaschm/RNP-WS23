package message_interpreter;

import data_classes.ChatMessage;

public class DummyMessageInterpreter implements IMessageInterpreter{

    String hostIP

    public DummyMessageInterpreter(String hostIP) {
        this.hostIP = hostIP;
    }
    public boolean isForMe(ChatMessage message) {
        return hostIP == message.getDestinationIP();
    }
}
