package de.haw.rn.luca_steven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import de.haw.rn.luca_steven.ui.UI;

/**
 * Nur zum testen
 */
public class UIController implements Runnable {

    private BlockingQueue<String> userInputQueue;
    private BlockingQueue<String> userOutputQueue;

    public UIController(BlockingQueue<String> userInputQueue, BlockingQueue<String> userOutputQueue) {
        this.userInputQueue = userInputQueue;
        this.userOutputQueue = userOutputQueue;
    }

    @Override
    public void run() {
        UI ui = new UI(userInputQueue, userOutputQueue);

        int i = 0;
        while (true) {

            //Logger.log(ui.getUserInput());
            ui.printChatMessage("Loop: " + i);

            try {
            Thread.sleep(1000);
            } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            i++;
            
        }
    }
}
