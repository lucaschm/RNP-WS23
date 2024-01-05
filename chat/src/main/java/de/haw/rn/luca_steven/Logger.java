package de.haw.rn.luca_steven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            // Use a relative path that works on both Linux and Windows
            String relativePath = "logs" + File.separator + "routingLog.txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(relativePath));
            writer.append(timestamp() + " >\n" + logMessage);
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public static void logRoutingTable(String string) {
        try {
            // Use a relative path that works on both Linux and Windows
            String relativePath = "logs" + File.separator + "routingLog.txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(relativePath));
            writer.append(timestamp() + " > " + string);
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    private static String timestamp() {
        // Get the current timestamp
        LocalDateTime timestamp = LocalDateTime.now();

        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Format the timestamp using the formatter
        String timestampString = timestamp.format(formatter);

        // return the timestamp string
        return timestampString;
    }
}