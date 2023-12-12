package de.haw.rn.luca_steven.ui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.ChatMessage;

public class UI {

    private BlockingQueue<String> userInputQueue;
    private BlockingQueue<String> userOutputQueue;

    public UI (BlockingQueue<String> userInputQueue, BlockingQueue<String> userOutputQueue) {
        this.userInputQueue = userInputQueue;
        this.userOutputQueue = userOutputQueue;
    }

//INPUT EINLESEN
    //Befehle:
        // connect with...
        // send message to ...
        // disconnect from ...
        // alle teilnehmer auflisten
        // exit (Programm schließen)
    public UserCommand getUserCommand() {
        return null;
        //return userInputQueue.poll();
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
        
    // teilnehmer Liste ausgeben
    public void printParticipantList() {
        // vom Router Teilnehmertabelle abholen
        // Einträge formatieren und ausgeben
    }

    // Fehler ausgeben
    // public void printError() {

    // }
    
    
}
