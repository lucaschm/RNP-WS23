package de.haw.rn.luca_steven;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static String sessionName;

    public static void main(String[] args) {

        setSessionName();
        Logger.createLogfiles();

        Scanner reader = new Scanner(System.in);
        
        String ip = getIp(reader);

        System.out.println("WICHTIG: es wurde diese IP-Adresse ermittelt: " + ip);
        System.out.println("In Config.java kann eingestellt werden, wie die IP ermittelt werden soll");
        System.out.println("Hinweis: es könnte sein, dass die Validierung der Checksumme beim Empfangen nicht funktioniert.");
        
        int port = getPort(reader);
        
        Controller chat = new Controller(ip, port);
        chat.run();
    }

    private static void setSessionName() {
        // Get the current timestamp
        LocalDateTime timestamp = LocalDateTime.now();

        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // Format the timestamp using the formatter
        String timestampString = timestamp.format(formatter);

        sessionName = timestampString;
    }

    private static String getIp(Scanner reader) {
        try {
            if (Config.localIPAdress == null) {
                return Inet4Address.getLocalHost().getHostAddress();
            }
            else {
                return Config.localIPAdress;
            }
            
            //return new String(ipArray, StandardCharsets.UTF_8);
        } catch (UnknownHostException e1) {
            try {
                System.out.println("The IP Address for this host was not found. Please give enter:");
                InetAddress ip = Inet4Address.getByName(reader.nextLine());
                return ip.getHostAddress();
            } catch (UnknownHostException e2) {
                System.out.println("The Adress was not found. EXIT");
                throw new RuntimeException(e2);
            }
        }
        
    }

    private static int getPort(Scanner reader) {
        while(true) {
            System.out.println("Bitte gebe den Port ein:");
            try {
                int num = reader.nextInt();
                if (num > 1024) {
                    return num;
                } else {
                    System.out.println("Bitte größer als 1024");
                }
            } catch (InputMismatchException e){
                reader.next();
                System.out.println("Das war keine Zahl");
            } catch (Exception e){
                reader.next();
                System.out.println("Ups, da ist etwas schief gelaufen");
            }
        }

    }
}
