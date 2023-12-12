package de.haw.rn.luca_steven.ui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.haw.rn.luca_steven.data_classes.ChatMessage;

//TODO: ganz viele Fehler abfangen
public class UI {

    private BlockingQueue<String> userInputQueue;
    private BlockingQueue<String> userOutputQueue;

    public UI () {
        this.userInputQueue = new LinkedBlockingQueue<String>();
        this.userOutputQueue = new LinkedBlockingQueue<String>();
    }

//INPUT EINLESEN
    //Befehle:
        // connect with...
        // send message to ...
        // disconnect from ...
        // alle teilnehmer auflisten
        // exit (Programm schließen)
    public UserCommand getUserCommand() {
        
        String inputString = userInputQueue.poll();
        UserCommand result = null;

        if (inputString.contains("\"")) {
            result = getUserSendCommand(inputString);
        } else {
            String[] inputParts = inputString.split(" ");

            switch (inputParts[0]) {
                case "connect":
                    result = getUserConnectCommand(inputParts);
                    break;
                case "disconnect":
                    result = getUserDisconnectCommand(inputParts);
                    break;
                case "list":
                    result = getUserListCommand(inputParts);
                    break;
                case "exit":
                    result = getUserExitCommand(inputParts);
                    break;
                default:
                    printError("command does not exist");
                    break;
            }
        }
        return result;
    }

    private UserCommand getUserSendCommand(String inputString) {
            String[] inputParts = inputString.split("\"");
            String messageContent = inputParts[1];

            inputParts = inputParts[0].split(" ");

            if (inputParts.length < 3) {
                printError("send command has to be in this format: send <IP> <Port> \"<message>\"");
                return null;
            }

            String ip = inputParts[1];
            int port = Integer.parseInt(inputParts[2]);

            return new UserCommand(Command.SEND, ip, port, messageContent);
    }

    private UserCommand getUserConnectCommand(String[] inputParts) {
        if (inputParts.length < 3) {
            printError("connect command has to be in this format: connect <IP> <Port>");
            return null;
        }
        
        String ip = inputParts[1];
        int port = Integer.parseInt(inputParts[2]);
        
        return new UserCommand(Command.CONNECT, ip, port, null);
    }

    private UserCommand getUserDisconnectCommand(String[] inputParts) {
        if (inputParts.length < 3) {
            printError("disconnect command has to be in this format: disconnect <IP> <Port>");
            return null;
        }
        
        String ip = inputParts[1];
        int port = Integer.parseInt(inputParts[2]);
        
        return new UserCommand(Command.DISCONNECT, ip, port, null);
    }

    private UserCommand getUserListCommand(String[] inputParts) {
        return new UserCommand(Command.LIST, null, -1, null);
    }

    private UserCommand getUserExitCommand(String[] inputParts) {
        return new UserCommand(Command.EXIT, null, -1, null);
    }

//OUTPUT AUSGEBEN

    // Nachricht von anderem Chatteilnehmer
    public void printChatMessage(ChatMessage chatMessage) {
        
        String sender = chatMessage.getOriginIP() + ":" + chatMessage.getOriginIDPort();
        String messageContent = chatMessage.getContent();
        String outputString = sender + " > " + messageContent;
        
        try {
            userOutputQueue.put(outputString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     
    //TODO printParticipantList
    // teilnehmer Liste ausgeben
    public void printParticipantList() {
        // vom Router Teilnehmertabelle abholen
        // Einträge formatieren und ausgeben
    }

    // Fehler ausgeben
    public void printError(String errorMessage) {
        userOutputQueue.add("Error: [" + errorMessage + "]");
    }
    
    
}
