package de.haw.rn.luca_steven.message_interpreter;

import de.haw.rn.luca_steven.data_classes.ChatMessage;

public class DummyMessageInterpreter implements IMessageInterpreter{

    String hostIP;

    public DummyMessageInterpreter(String hostIP) {
        this.hostIP = hostIP;
    }

    @Override
    public boolean isForMe(ChatMessage message) {
        return hostIP == message.getDestinationIP();
    }
}
