package message_interpreter;

import data_classes.ChatMessage;

public interface IMessageInterpreter {
    
    public boolean isForMe(ChatMessage message);
}
