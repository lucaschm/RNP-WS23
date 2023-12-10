package de.haw.rn.luca_steven.message_interpreter;

import de.haw.rn.luca_steven.data_classes.ChatMessage;

public interface IMessageInterpreter {
    
    public boolean isForMe(ChatMessage message);
}
