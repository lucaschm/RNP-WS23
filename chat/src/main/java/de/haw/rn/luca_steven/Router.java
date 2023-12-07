import connection_handler.IConnectionHandler;
import data_classes.ChatMessage;
import connection_handler.DummyConnectionHandler;
import message_interpreter.DummyMessageInterpreter;
import message_interpreter.IMessageInterpreter;

public class Router {

    IConnectionHandler connections;
    IMessageInterpreter interpreter;

    public Router() {
        connections = new DummyConnectionHandler();
        interpreter = new DummyMessageInterpreter("127.0.0.1");
        table = new 
    }

    /*
     * If the connection Handler has received a new Message it is either forwarded to the next Server or the Message is returned 
     */
    public ChatMessage processMessage() {
        ChatMessage message = connections.nextMessage();
        if(interpreter.isForMe(message)) {
            return message;
        }
        else {
            forward(message);
            return null;
        }
    }

    private void forward(ChatMessage message) {
        Ro
    }
}
