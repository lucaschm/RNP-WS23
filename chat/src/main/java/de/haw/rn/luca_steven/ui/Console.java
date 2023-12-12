package de.haw.rn.luca_steven.ui;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.UIController;

public class Console {

    private static BlockingQueue<String> userInputQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<String> userOutputQueue = new LinkedBlockingQueue<>();
    private static boolean isRunning = true;

    public static void main(String[] args) {
        new Thread(new UIController(userInputQueue, userOutputQueue)).start();

        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            Logger.log("press Enter to receive Message");
            String input = scanner.nextLine();
            
            try {
                synchronized(userInputQueue) {
                    userInputQueue.put(input);
                }
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            synchronized(userOutputQueue) {
                System.out.println(userOutputQueue.poll());
            }
        }
        scanner.close();
    }
}
