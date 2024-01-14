package de.haw.rn.luca_steven.ui;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class UI {

    private BlockingQueue<String> userInputQueue;
    private BlockingQueue<String> userOutputQueue;
    Thread consoleThread;

    //REGEX PARTS
    private static final String IP_ADDRESS = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
    private static final String PORT = "(\\d{1,5})";
    //private static final String PORT = "\\b(?:[1-9]\\d{3,4}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])\\b";
    private static final String BLANK = "\\s+";
    private static final String COLON = ":";
    private static final String CONNECT = "(connect|c)";
    private static final String DISCONNECT = "(disconnect|d)";
    private static final String SEND = "(send|s)";
    private static final String MESSAGE = ".*";
    private static final String LIST = "(list|l)";
    private static final String EXIT = "(exit|e)";

    public UI () {
        this.userInputQueue = new LinkedBlockingQueue<String>();
        this.userOutputQueue = new LinkedBlockingQueue<String>();
        Scanner scanner = new Scanner(System.in);

        consoleThread = new Thread(new Console(userInputQueue, userOutputQueue, scanner, System.out));
        consoleThread.start();
    }

//INPUT EINLESEN

    public UserCommand getUserCommand() {
        
        String inputString = userInputQueue.poll();
        
        return getUserCommandNew(inputString);
    }

    private UserCommand getUserCommandNew(String inputString) {
        UserCommand result = null;

        if (inputString == null) {
            return null;
        }

        // REGULAR EXPRESSIONS
        String connectWithBlank = "^" + CONNECT + BLANK + IP_ADDRESS + BLANK + PORT + "$";
        String connectWithColon = "^" + CONNECT + BLANK + IP_ADDRESS + COLON + PORT + "$";
        String disconnectWithBlank = "^" + DISCONNECT + BLANK + IP_ADDRESS + BLANK + PORT + "$";
        String disconnectWithColon = "^" + DISCONNECT + BLANK + IP_ADDRESS + COLON + PORT + "$";
        String sendWithBlank = "^" + SEND + BLANK + IP_ADDRESS + BLANK + PORT + MESSAGE + "$";
        String sendWithColon = "^" + SEND + BLANK + IP_ADDRESS + COLON + PORT + MESSAGE + "$";
        String list = "^" + LIST + "$";
        String exit = "^" + EXIT + "$";

        if (matches(inputString, connectWithBlank)) {
            String[] inputParts = inputString.split(BLANK);
            String ip = inputParts[1];
            int port = Integer.parseInt(inputParts[2]);
            result = new UserCommand(Command.CONNECT, ip, port, null);
        }
        else if (matches(inputString, connectWithColon)) {
            String[] inputParts = inputString.split(BLANK);
            inputParts = inputParts[1].split(COLON);
            String ip = inputParts[0];
            int port = Integer.parseInt(inputParts[1]);
            result = new UserCommand(Command.CONNECT, ip, port, null);
        }
        else if (matches(inputString, disconnectWithBlank)) {
            String[] inputParts = inputString.split(BLANK);
            String ip = inputParts[1];
            int port = Integer.parseInt(inputParts[2]);
            result = new UserCommand(Command.DISCONNECT, ip, port, null);
        }
        else if (matches(inputString, disconnectWithColon)) {
            String[] inputParts = inputString.split(BLANK);
            inputParts = inputParts[1].split(COLON);
            String ip = inputParts[0];
            int port = Integer.parseInt(inputParts[1]);
            result = new UserCommand(Command.DISCONNECT, ip, port, null);
        }
        else if (matches(inputString, sendWithBlank)) {
            String[] inputParts = inputString.split(BLANK);
            String ip = inputParts[1];
            int port = Integer.parseInt(inputParts[2]);
            inputString = inputString.replace(inputParts[0], "");
            inputString = inputString.replace(inputParts[1], "");
            inputString = inputString.replace(inputParts[2], "");
            inputString = inputString.trim();
            result = new UserCommand(Command.SEND, ip, port, inputString);
        }
        else if (matches(inputString, sendWithColon)) {
            String[] inputParts = inputString.split(BLANK);
            String[] inputParts2 = inputParts[1].split(COLON);
            String ip = inputParts2[0];
            int port = Integer.parseInt(inputParts2[1]);
            inputString = inputString.replace(inputParts[0], "");
            inputString = inputString.replace(inputParts[1], "");
            inputString = inputString.trim();
            result = new UserCommand(Command.SEND, ip, port, inputString);
        }
        else if (matches(inputString, list)) {
            result = new UserCommand(Command.LIST, null, -1, null);
        }
        else if (matches(inputString, exit)) {
            result = new UserCommand(Command.EXIT, null, -1, null);
        }
        else {
            Status.unknownCommand();
        }
        
        return result;
    }

    private boolean matches(String inpuString, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inpuString);
        return matcher.matches();
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
            Logger.logErrorStacktrace(e);
        }
    }
     
    // teilnehmer Liste ausgeben
    public void printParticipantList(Set<RoutingEntry> set) {

        String outputString = "";
        for (RoutingEntry participant : set) {
            outputString += participant.getDestination() + " [" + participant.getHops() + "]\n";
        }
        System.out.println(outputString);
        Status.participantListPrinted(outputString);
    }

    // Fehler ausgeben
    public void printError(String errorMessage) {
        System.out.println("Error: [" + errorMessage + "]");
    }

    public void teardown() {
        try {
            Console.close();
            consoleThread.join(10);
        } catch (InterruptedException e) {
            Status.unexpectedError("Unable to close console thread.");
            Logger.logErrorStacktrace(e);
        }
    }
    
    
}
