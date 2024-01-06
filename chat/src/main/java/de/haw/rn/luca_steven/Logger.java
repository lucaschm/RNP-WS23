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
    private static final String logFile = "logs" + File.separator + Main.sessionName + File.separator + "log.md";
    private static final String routingTabLogFile = "logs" + File.separator + Main.sessionName + File.separator + "routingTableLog.md";
    private static final String routingLogFile = "logs" + File.separator + Main.sessionName + File.separator + "routingLog.md";
    private static final String dvLogFile = "logs" + File.separator + Main.sessionName + File.separator + "distanceVectoringLog.md";

    static boolean userUninformedAboutMissingFiles = true;

    public static void log(String logMessage) {
        System.out.println(logMessage);
    }

    public static void error(String errorMessage) {
        //System.out.println(errorMessage);
    }

    public static void logFile(String logMessage) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.append(timestamp() + " > " + logMessage + "\n");
            writer.close();
        } catch (IOException e) {
            return;
        }
    }

    public static void logRouting(String logMessage, boolean withTimestamp) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(routingLogFile, true));
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

    public static void logDistanceVectoring(String logMessage, boolean withTimestamp) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dvLogFile, true));
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

    public static void logRoutingTable(String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(routingTabLogFile, true));
            writer.append(string + "> " + timestamp() + "\n\n");
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

    public static void createLogfiles() {
        try {
            // Create a Path object from the file path
            Path path1 = Paths.get(logFile);
            Path path2 = Paths.get(routingLogFile);
            Path path3 = Paths.get(routingTabLogFile);

            // Create directories if they don't exist
            Files.createDirectories(Paths.get(directory));

            // Create the file using Files.createFile()
            Files.createFile(path1);
            Files.createFile(path2);
            Files.createFile(path3);

            System.out.println("Log files created.");
        } catch (IOException e) {
            System.out.println("Log files not created.");
        }
    }
}