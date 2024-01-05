package de.haw.rn.luca_steven.ui;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Set;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class Status {

    public static void serverStarted(String ip, int port) {
        String ipPort = ip + ":" + port;
        Logger.log("Server started as " + ipPort);
    }

    public static void messageNotSent() {
        Logger.log("One message not sent (might be a routing_message)");
    }

    public static void clientConnect(String ipAddress, int port) {
        Logger.logFile("Try to connect with " + ipAddress + ":" + port);
    }

    public static void failedToConnect(String string) {
        Logger.log("Failed to connect: " + string);
    }

    public static void connectionSuccess(String ipAddress, int port) {
        Logger.log("Connected to: " + ipAddress + ":" + port);
    }

    public static void waitForConnection(String ipAddress, int port) {
        Logger.log("Wait for connection with: " + ipAddress + ":" + port);
    }

    public static void clientClose(String remoteAdress) {
        Logger.log("Disconnected with " + remoteAdress);
    }

    public static void callMethod(String string) {
        Logger.logFile("method called: " + string);
    }

    public static void addStringToSendQueue(String s, int queueSize, String ip, int port) {
        Logger.logFile("String was added to sendMessageQueue. Size of queue is now "+ queueSize + ". Message:");
        Logger.logFile(s);
    }

    public static void addStringToReceiveQueue(String s, int queueSize) {
        Logger.logFile("String was added to receiveMessageQueue. Size of queue is now "+ queueSize + ". Message:");
        Logger.logFile(s);
    }

    public static void acceptConnection(String remoteAddress) {
        Logger.log("Got connection from: " + remoteAddress);
    }

    public static void validChecksum() {
        Logger.logFile("Message has valid checksum.");
    }

    public static void lostConnection() {
        Logger.log("Lost connection with someone.");
    }

    public static void bufferCreated(ByteBuffer buffer, String string, int length, long checksum) {
        Logger.logFile("Buffer created: ");
        bufferInfo(buffer, string, length, checksum);
    }

    private static void bufferInfo(ByteBuffer buffer, String string, int length, long checksum) {
        Logger.logFile("String:\n" + string);
        Logger.logFile("length:\n" + length);
        Logger.logFile("checksum:\n" + checksum);
        Logger.logFile("buffer capacity:\n" + buffer.capacity());
        Logger.logFile("buffer position:\n" + buffer.position());
    }

    public static void connectionFinised(SocketAddress remoteAddress) {
        Logger.log("Finished connecting to: " + remoteAddress);
    }

    public static void selfConnection() {
        Logger.log("Don't you have friends? Connect with them instead of yourself!");
    }

////// ROUTING /////////

    public static void routerProcess(long timedif) {
        Logger.logRouting("Calculate timedif, to see if share is necessary. timedif: " + timedif);
    }

    public static void shareRoutingInformation(long timestamp) {
        Logger.logRouting("Router will share Routing Information now. Timestamp: " + timestamp);
    }

    public static void routingMessageReceived() {
        Logger.logRouting("Routing message received.");
    }

    public static void forwardMessage() {
        Logger.logRouting("Received chat message for someone else. This message will be forwarded.");
    }

    public static void addRoutingEntry(RoutingEntry newEntry) {
        String destination = newEntry.getDestination();
        int hops = newEntry.getHops();
        String nextHop = newEntry.getNextHop();
        String origin = newEntry.getOrigin();
        Logger.logRouting("Add new routing entry (dst,hops,next,orig): (" + destination + ", " + hops + ", " + nextHop + ", " + origin + ")");
    }

    public static void removeRoutingEntry(RoutingEntry entry) {
        String destination = entry.getDestination();
        int hops = entry.getHops();
        String nextHop = entry.getNextHop();
        String origin = entry.getOrigin();
        Logger.logRouting("Remove routing entry (dst,hops,next,orig): (" + destination + ", " + hops + ", " + nextHop + ", " + origin + ")");
    }

    public static void routingTableChanged(IRoutingTable table) {
        String hint = "Routing-Table has changed:\n";
        String tableString = "|     destination     | hops |       nextHop       |       origin        |\n|---------------------|------|---------------------|---------------------|\n";
        Set<RoutingEntry> allEntries = table.getEntries();
        for (RoutingEntry entry : allEntries) {
            String destination = entry.getDestination();
            int hops = entry.getHops();
            String nextHop = entry.getNextHop();
            String origin = entry.getOrigin();
            tableString += "| " + destination + " | " + hops + "    | " + nextHop + " | " + origin + " |\n";
        }
        Logger.logRoutingTable(hint + tableString);
    }

}
