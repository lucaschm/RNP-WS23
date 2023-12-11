package de.haw.rn.luca_steven;

import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;

public class ClientController {

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler(6788);

        connectionHandler.connect("localhost", 6789);
        connectionHandler.listen();
        
        for(int i = 0;i<100;i++) {
            //Logger.log(6788 + ": main while iteration " + i++);
            
            connectionHandler.sendMessage("'Test String: " + i +"'");
            connectionHandler.sendMessage("'another String: " + i +"'");
            connectionHandler.listen();
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
