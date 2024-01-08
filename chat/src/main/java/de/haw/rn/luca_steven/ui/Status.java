package de.haw.rn.luca_steven.ui;

import java.net.SocketAddress;
import java.util.Set;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.ChatMessage;
import de.haw.rn.luca_steven.data_classes.RoutingMessage;
import de.haw.rn.luca_steven.data_classes.routing_table.IRoutingTable;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;

public class Status {

    public static void controllerSpeed(long timediff, long superloopIterations) {
        Logger.logBasics("The Controller needed " + timediff + " milliseconds for " + superloopIterations + " superloop iterations.");
    }

    public static void serverStarted(String ip, int port) {
        String ipPort = ip + ":" + port;
        Logger.log("Server started as " + ipPort);
        Logger.logBasics("Server started as " + ipPort);
    }

    public static void messageNotSent() {
        Logger.log("One message not sent (might be a routing_message)");
        Logger.logBasics("One message not sent (might be a routing_message)");
    }

    public static void clientConnect(String ipAddress, int port) {
        Logger.logBasics("Try to connect with " + ipAddress + ":" + port);
    }

    public static void failedToConnect(String string) {
        Logger.log("Failed to connect: " + string);
        Logger.logBasics("Failed to connect: " + string);
    }

    public static void connectionSuccess(String ipAddress, int port) {
        Logger.log("Connected to: " + ipAddress + ":" + port);
        Logger.logBasics("Connected to: " + ipAddress + ":" + port);
    }

    public static void waitForConnection(String ipAddress, int port) {
        Logger.log("Wait for connection with: " + ipAddress + ":" + port);
        Logger.logBasics("Wait for connection with: " + ipAddress + ":" + port);
    }

    public static void clientClose(String remoteAdress) {
        Logger.log("Disconnected with " + remoteAdress);
        Logger.logBasics("Disconnected with " + remoteAdress);
    }

    public static void callMethod(String string) {
        Logger.logBasics("method called: " + string);
    }

    public static void addStringToSendQueue(String s, int queueSize, String ip, int port) {
        //Logger.logFile("String was added to sendMessageQueue. Size of queue is now "+ queueSize + ". Message:\n```json\n" + s + "\n```");
    }

    public static void addStringToReceiveQueue(String s, int queueSize) {
        //Logger.logFile("String was added to receiveMessageQueue. Size of queue is now "+ queueSize + ". Message:\n```json\n" + s + "\n```");
    }

    public static void acceptConnection(String remoteAddress) {
        Logger.log("Got connection from: " + remoteAddress);
        Logger.logBasics("Got connection from: " + remoteAddress);
    }

    public static void lostConnection() {
        Logger.log("Lost connection with someone.");
        Logger.logBasics("Lost connection with someone.");
    }

    public static void connectionFinised(SocketAddress remoteAddress) {
        Logger.log("Finished connecting to: " + remoteAddress);
        Logger.logBasics("Finished connecting to: " + remoteAddress);
    }

    public static void selfConnection() {
        Logger.log("Don't you have friends? Connect with them instead of yourself!");
        Logger.logBasics("Stupid user just tried to connect with himself. But no worries I prevented him from doing this.");
    }

    public static void unexpectedError(String message) {
        Logger.log(message);
    }

/////// BUFFER /////////

    public static void bufferCreated(String string, int length, long checksum, boolean isValid) {
        String bufferInfo = bufferInfo(string, length, checksum, isValid);
        Logger.logBufferOut("Buffer created:\n" + bufferInfo);
    }

    public static void bufferConsumed(String string, int length, long checksum, boolean isValid) {
        String bufferInfo = bufferInfo(string, length, checksum, isValid);
        Logger.logBufferIn("Buffer consumed:\n" + bufferInfo);
    }

    private static String bufferInfo(String string, int length, long checksum, boolean isValid) {
        String info = 
        "Length: " + length + "\n" +
        "Checksum: " + checksum + "\n" +
        "checksumIsValid: " + isValid + "\n" +
        "String:\n```json\n" + string + "\n```\n";

        return info;
    }

    public static void invalidChecksum() {
        Logger.logBasics("There was a message with an incorrect Checksum.");
    }

////// ROUTING /////////

    public static void routerProcess(long timedif) {
        //Logger.logRoutingInfoOut("timedif calculated: " + timedif, true);
    }

    public static void shareRoutingInformation(long timestamp) {
        Logger.logRoutingInfoOut("Router will share Routing Information now. Timestamp: " + timestamp, true);
    }

    public static void nothingToShare() {
        Logger.logRoutingInfoOut("There is nothing to share (no neighbours).", true);
    }

    public static void routingInformationSent(Set<RoutingEntry> splitHorizonTable, String ip, int port) {
        Logger.logRoutingInfoOut("\n" + tableToString(splitHorizonTable) + 
        "> This table was sent to " + ip + ":" + port + "\n", false);
    }

    public static void routingMessageReceived(RoutingMessage rm) {
        String info = "Routing message received from " + rm.getFullOriginAddress();
        Set<RoutingEntry> routingTable = rm.getSetOfRoutingEntries();
        Logger.logRouting(info, true);
        Logger.logRoutingInfoIn(info + " Table:\n\n" + tableToString(routingTable) + "\n", true);
    }

    public static void forwardMessage() {
        Logger.logRouting("Received chat message for someone else. This message will be forwarded.", true);
    }

    public static void addRoutingEntry(RoutingEntry newEntry) {
        String destination = newEntry.getDestination();
        int hops = newEntry.getHops();
        String nextHop = newEntry.getNextHop();
        String origin = newEntry.getOrigin();
        Logger.logRouting("Add new routing entry (dst,hops,next,orig): (" + destination + ", " + hops + ", " + nextHop + ", " + origin + ")", true);
    }

    public static void removeRoutingEntry(RoutingEntry entry) {
        String destination = entry.getDestination();
        int hops = entry.getHops();
        String nextHop = entry.getNextHop();
        String origin = entry.getOrigin();
        Logger.logRouting("Remove routing entry (dst,hops,next,orig): (" + destination + ", " + hops + ", " + nextHop + ", " + origin + ")", true);
    }

    public static void routingTableChanged(IRoutingTable table) {
        Logger.logRoutingTable(tableToString(table.getEntries()));
    }

    private static String tableToString(Set<RoutingEntry> table) {
        String tableString = "|     destination     | hops |       nextHop       |       origin        |\n|---------------------|------|---------------------|---------------------|\n";
        for (RoutingEntry entry : table) {
            String destination = entry.getDestination();
            int hops = entry.getHops();
            String nextHop = entry.getNextHop();
            String origin = entry.getOrigin();
            tableString += "| " + destination + " | " + hops + "    | " + nextHop + " | " + origin + " |\n";
        }
        return tableString;
    }

/////////// USER COMMANDS //////////
    public static void commandConnect(UserCommand com) {
        Logger.logBasics("User wants to `connect` with " + com.getIP() + ":" + com.getPort());
    }

    public static void commandSend(UserCommand com) {
        Logger.logBasics("User wants to `send` a message to " + com.getIP() + ":" + com.getPort() + ": " + com.getMessageContent());
    }

    public static void commandDisconnect(UserCommand com) {
        Logger.logBasics("User wants to `disconnect` with " + com.getIP() + ":" + com.getPort());
    }

    public static void commandList() {
        Logger.logBasics("User wants to `list` participants");
    }

    public static void commandExit() {
        Logger.logBasics("User wants to `exit`");
    }

    public static void chatMessageReceived(ChatMessage receivedMsg) {
        Logger.logBasics("Chat message received from " + receivedMsg.getOriginIP() + ":" + receivedMsg.getDestinationIDPort() + ": " + receivedMsg.getContent());
    }

    public static void participantListPrinted(String outputString) {
        Logger.logBasics("\n" + outputString);
    }
}
