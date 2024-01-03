package de.haw.rn.luca_steven;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger {
    static long millis = System.currentTimeMillis();

    public static void log(String logMessage) {
        System.out.println(logMessage);
    }

    public static void error(String errorMessage) {
        //System.out.println(errorMessage);
    }

    public static void logFile(String logMessage) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/networker/log.txt"));
            writer.append(logMessage);
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public static void logRouting(String logMessage) {
        try {
            long newMillis = System.currentTimeMillis();
            long diff = newMillis - millis;
            millis = newMillis;

            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/networker/routingLog.txt"));
            writer.append("current" + newMillis);
            writer.append("diff" + diff);
            writer.append(logMessage);
            writer.close();
        } catch (IOException e) {
            return;
        }
    }
}