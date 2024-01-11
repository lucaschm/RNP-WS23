package de.haw.rn.luca_steven.ui;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 * Diese Klasse nutzt zwei Queues. 
 * Die Eingabe wird nicht fromatiert. Hier werden nur 
 * blanke Strings und null herausgefiltert.
 * 
 * userInputQueue
 * ==============
 * In diese Queue werden alle Eingaben aus der Konsole geschreiben.
 * Wenn der String der Eingabe blank ist, dann wird dieser nicht in die 
 * Queue geschrieben. Wenn also nur Enter gedrückt wird, landet nichts in
 * der Queue.
 * 
 * ... (Kommentar nicht fertig)
 * 
 * 
 */
public class Console implements Runnable {

    private BlockingQueue<String> userInputQueue;
    private BlockingQueue<String> userOutputQueue;
    private Scanner scanner;
    private PrintStream out;
    private static boolean isRunning = true;

    public Console(BlockingQueue<String> userInputQueue, 
                   BlockingQueue<String> userOutputQueue, 
                   Scanner scanner, 
                   PrintStream out) {
        this.userInputQueue = userInputQueue;
        this.userOutputQueue = userOutputQueue;
        this.scanner = scanner;
        this.out = out;

    }

    public void run() {
        
        String input;
        while (isRunning) {
            input = scanner.nextLine();

            if (!input.equals("")) {
                try {
                    userInputQueue.put(input);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                String outputString = userOutputQueue.poll();
                
                if (outputString != null) {
                    out.println(outputString);
                }
            }   
        }
        scanner.close();
    }

    public static void close() {
        isRunning = false;
    }
}
