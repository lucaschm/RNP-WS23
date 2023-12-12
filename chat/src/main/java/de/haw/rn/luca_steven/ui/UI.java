package de.haw.rn.luca_steven.ui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
    public String getUserInput() {
        synchronized(userInputQueue) {
            return userInputQueue.poll();
        }
        
    }

//OUTPUT AUSGEBEN

    // Nachricht von anderem Chatteilnehmer
    public void printChatMessage(String string) {
        try {
            synchronized(userOutputQueue) {
                userOutputQueue.put(string);
            }
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        
    // teilnehmer Liste ausgeben
    public void printParticipantList() {

    }

    // Fehler ausgeben
    public void printError() {

    }
    
    
}
