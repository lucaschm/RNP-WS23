package de.haw.rn.luca_steven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    static long millis = System.currentTimeMillis();

    // Use a relative path that works on both Linux and Windows
    private static final String directory = "logs" + File.separator + Main.sessionName;
    private static final String basics = "logs" + File.separator + Main.sessionName + File.separator + "basics.md";
    private static final String routingTabLogFile = "logs" + File.separator + Main.sessionName + File.separator + "routingTable.md";
    private static final String routingLogFile = "logs" + File.separator + Main.sessionName + File.separator + "routing.md";
    private static final String outRoutInfo = "logs" + File.separator + Main.sessionName + File.separator + "outgoingRoutingInformation.md";
    private static final String inRoutInfo = "logs" + File.separator + Main.sessionName + File.separator + "incomingRoutingInformation.md";
    private static final String outBuffer = "logs" + File.separator + Main.sessionName + File.separator + "outgoingBuffer.md";
    private static final String inBuffer = "logs" + File.separator + Main.sessionName + File.separator + "incomingBuffer.md";

    static boolean userUninformedAboutMissingFiles = true;

    public static void log(String logMessage) {
        System.out.println(logMessage);
    }

    public static void error(String errorMessage) {
        //System.out.println(errorMessage);
    }

    public static void logFile(String filepath, String logMessage, boolean withTimestamp) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
            if (withTimestamp) {
                writer.append(timestamp() + " > " + logMessage + "\n");
            } 
            else {
                writer.append(logMessage + "\n");
            }
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public static void logBasics(String logMessage) {
        logFile(basics, logMessage, true);
    }

    public static void logRouting(String logMessage, boolean withTimestamp) {
        logFile(routingLogFile, logMessage, withTimestamp);
    }

    public static void logRoutingInfoOut(String logMessage, boolean withTimestamp) {
        logFile(outRoutInfo, logMessage, withTimestamp);
    }

    public static void logRoutingInfoIn(String logMessage, boolean withTimestamp) {
        logFile(inRoutInfo, logMessage, withTimestamp);
    }

    public static void logRoutingTable(String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(routingTabLogFile, true));
            writer.append(string + "> " + timestamp() + "\n\n");
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public static void logBufferOut(String string) {
        logFile(outBuffer, string, true);
    }
    
    public static void logBufferIn(String string) {
        logFile(inBuffer, string, true);
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

    public static void createLogDirectory() {
        try {
            // Create directories if they don't exist
            Files.createDirectories(Paths.get(directory));
            System.out.println("Log directory created.");
        } catch (IOException e) {
            System.out.println("Log directory not created.");
        }
    }
}