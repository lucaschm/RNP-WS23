package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.IConnectionHandler;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntrySet;
import de.haw.rn.luca_steven.message_interpreter.DummyMessageInterpreter;
import de.haw.rn.luca_steven.message_interpreter.IMessageInterpreter;

public class Router {

    IConnectionHandler connections;
    IMessageInterpreter interpreter;
    IRoutingTable table;

    public Router() {
        connections = new ConnectionHandler(6789);
        interpreter = new DummyMessageInterpreter("127.0.0.1");
        table = new RoutingEntrySet();
    }

    /*
     * If the connection Handler has received a new Message it is either forwarded to the next Server or the Message is returned 
     */
    public ChatMessage processMessage() {
        //ChatMessage message = connections.nextMessage();
        ChatMessage message = new ChatMessage(null, 0, null, 0, 0, null);
        if(interpreter.isForMe(message)) {
            return message;
        }
        else {
            forward(message);
            return null;
        }
    }

    private void forward(ChatMessage message) {
        
    }
}
