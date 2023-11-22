/**
    Importieren der notwendigen Pakete:
        Importieren Sie java.io.* und java.net.*, 
        welche die grundlegenden Netzwerk- und Input/Output-Klassen in Java enthalten.

    ServerSocket erstellen: 
        Erstellen Sie ein Objekt der Klasse ServerSocket. 
        Dies repräsentiert den Server im Netzwerk. 
        Sie müssen einen Port angeben, 
        auf dem der Server auf eingehende Verbindungen hören soll.

    Auf Verbindungen warten: 
        Der Server muss auf eingehende Verbindungen warten. 
        Dies geschieht durch Aufrufen der accept()-Methode des ServerSocket-Objekts. 
        Diese Methode blockiert, bis eine Verbindung hergestellt wird.

    Kommunikation mit dem Client: 
        Nachdem eine Verbindung hergestellt wurde, 
        können Sie Daten über Input- und Output-Streams austauschen. 
        Jede akzeptierte Verbindung ist ein Socket-Objekt, 
        das seine eigenen Input- und Output-Streams hat.

    Schließen der Verbindungen: 
        Nach der Kommunikation sollten Sie sowohl den Client-Socket 
        als auch den Server-Socket schließen, um Ressourcen freizugeben.
**/

import java.io.*;
import java.net.*;

public class Chat {
    int port = 8080;
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println(serverSocket);
            int i = 
            while(true){
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println(clientSocket);
                }
            }
        }
        System.out.println("Bye Bye");
    }
}