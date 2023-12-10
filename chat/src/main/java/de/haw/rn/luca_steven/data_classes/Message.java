package de.haw.rn.luca_steven.data_classes;

public class Message {

    private boolean isChatMessage;

    public Message(boolean isChatMessage) {
        this.isChatMessage = isChatMessage;
    }

    public boolean isChatMessage() {
        return isChatMessage;
    } 
}
