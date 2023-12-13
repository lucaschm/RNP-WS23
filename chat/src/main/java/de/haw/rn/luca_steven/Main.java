package de.haw.rn.luca_steven;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        Scanner reader = new Scanner(System.in);
        
        String ip = getIp(reader);        
        int port = getPort(reader);
        
        
        Controller chat = new Controller(ip, port);
        chat.run();
    }

    private static String getIp(Scanner reader) {
        try {
            //TODO hardcoded ip adresse
            return Inet4Address.getByName("141.22.27.103").getHostAddress();
            //return new String(ipArray, StandardCharsets.UTF_8);
        } catch (UnknownHostException e1) {
            try {
                Logger.log("The IP Address for this host was not found. Please give enter:");
                InetAddress ip = Inet4Address.getByName(reader.nextLine());
                return ip.getHostAddress();
            } catch (UnknownHostException e2) {
                Logger.log("The Adress was not found. EXIT");
                throw new RuntimeException(e2);
            }
        }
        
    }

    private static int getPort(Scanner reader) {
        Logger.log("Bitte gebe den Port ein:");
        return reader.nextInt();

    }
}
