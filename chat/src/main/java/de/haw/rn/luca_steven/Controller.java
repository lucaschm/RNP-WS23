package de.haw.rn.luca_steven;


import de.haw.rn.luca_steven.connection_handler.ConnectionHandler;
import de.haw.rn.luca_steven.connection_handler.exceptions.MessageNotSendException;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.ui.UI;
import de.haw.rn.luca_steven.ui.UserCommand;
import de.haw.rn.luca_steven.ui.Status;

public class Controller {
    private static final long ITERATION_LIMIT = 1_000_000;
    private static final int SLEEP_TIME = 10;
    private String ip;
    private int port;
    private long superloopIterations;
    private long startTime;
    private long finishTime;

    public Controller(String ip, int port) {
        this.ip = ip;
        this.port = port;
        superloopIterations = 0;
    }

    public void run() {
        
        ConnectionHandler connectionHandler = new ConnectionHandler(ip, port);
        String ipPort = ip + ":" + port;
        Router router = new Router(connectionHandler, ipPort);
        UI ui = new UI();
        Status.serverStarted(ip, port);
        
        startTime = System.currentTimeMillis();
        while(true) {
            monitorLoopSpeed();

            // sleep for less cpu load and calmer fans
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Logger.logBasics("Program got no sleep");
            }

            connectionHandler.listen();
            ChatMessage receivedMsg = null;
            try {
                receivedMsg = router.process();
            
                if (receivedMsg != null) {
                    Status.chatMessageReceived(receivedMsg);
                    ui.printChatMessage(receivedMsg);
                }
                UserCommand com = ui.getUserCommand();
                if (com != null) {
                    switch (com.getCommand()) {
                    case CONNECT:
                        Status.commandConnect(com);
                        router.connect(com.getIP(), com.getPort());
                    break;
                    case SEND:
                        Status.commandSend(com);
                        ChatMessage msg = new ChatMessage(
                            com.getIP(), com.getPort(), 
                            ip, port, 
                            15, com.getMessageContent());
                        router.send( msg);
                    break;
                    case DISCONNECT:
                        Status.commandDisconnect(com);
                        router.disconnect(com.getIP(), com.getPort());
                    break;
                    case LIST:
                        Status.commandList();
                        ui.printParticipantList(router.getParticipantsSet());
                    break;
                    case EXIT:
                        Status.commandExit();
                    
                    break;
                    default:
                    break;
                    }
                } 
            
            } catch (MessageNotSendException e) {
                Status.messageNotSent();
            }
        }
    }

    private void monitorLoopSpeed() {
        superloopIterations++;

        if (superloopIterations >= ITERATION_LIMIT) {
            finishTime = System.currentTimeMillis();
            long timediff = finishTime - startTime;
            Status.controllerSpeed(timediff, superloopIterations);
            startTime = System.currentTimeMillis();
            superloopIterations = 0;
        }
    }
}
