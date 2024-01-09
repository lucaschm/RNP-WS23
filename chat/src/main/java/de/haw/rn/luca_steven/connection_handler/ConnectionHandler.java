package de.haw.rn.luca_steven.connection_handler;

import de.haw.rn.luca_steven.CRC32Checksum;
import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.MessagePack;
import de.haw.rn.luca_steven.data_classes.routing_table.RoutingEntry;
import de.haw.rn.luca_steven.ui.Status;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ConnectionHandler implements IConnectionHandler{

    private static final int COMMON_HEADER_LENGTH = 8;
    private final String ip;
    private final int idPort;
    private final LinkedList<MessagePack> sendMessageQueue;
    private final LinkedList<String> receiveMessageQueue;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private String errorMessage;

    public ConnectionHandler(String ip, int idPort) {
        this.ip = ip;
        this.idPort = idPort;
        this.sendMessageQueue = new LinkedList<MessagePack>();
        this.receiveMessageQueue = new LinkedList<String>();
        errorMessage = "";
        setup();
    }

    
    //diese Methode sollte regelmäßig wiederholt werden
    @Override
    public void listen() {

        try {
            selector.selectNow();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            //Logger.log(idPort + ": get selectedKeys");
            MessagePack firstPlace = sendMessageQueue.peek(); 
            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if(key.isWritable()) { //4
                    writeMessage(key);
                }

                if (key.isReadable()) { //1
                    readMessage(key);
                }

                if (key.isAcceptable()) { //16
                    register(selector, serverSocketChannel);
                }

                if (key.isConnectable()) {  //lieber else if?
                    continiueConnect(key);
                }
                iter.remove();
            }
            if (firstPlace != null && firstPlace.equals(sendMessageQueue.peek())) {
                    String msg = sendMessageQueue.removeFirst().getMessage();
                    errorMessage = "Error: " + msg + " could not be send.";
            }
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();;
        }
        catch (CancelledKeyException e) {
            // its okay
        }
    }

    @Override
    public String nextString() {
        return receiveMessageQueue.removeFirst();
    }

    public boolean hasNext() {
        return !receiveMessageQueue.isEmpty();
    }

    public boolean hasError() {
        return !errorMessage.equals("");
    }

    public String getError() {
        String result = errorMessage;
        errorMessage = "";
        return result;
    }

    @Override
    public void connect(String ipAddress, int port) {
        try {
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            SocketAddress socketAddr;
            socketAddr = new InetSocketAddress(ipAddress, port);
            Status.clientConnect(ipAddress, port);
            boolean success = client.connect(socketAddr);
            
            if (success) {
                client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                Status.connectionSuccess(ipAddress, port);
            } else {
                client.register(selector, SelectionKey.OP_CONNECT);
                Status.waitForConnection(ipAddress, port);
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

    @Override
    public void disconnect(String ipAddress, int port, boolean all) {
        Set<SelectionKey> selectedKeys = selector.keys();
        Iterator<SelectionKey> iter = selectedKeys.iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            try{
                SelectableChannel channel = key.channel();
                if(!(channel instanceof ServerSocketChannel)) {
                    SocketChannel client = (SocketChannel) channel;
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) client.getRemoteAddress();
                    String remoteIP = inetSocketAddress.getAddress().getHostAddress();
                    int remotePort = inetSocketAddress.getPort();
                    if (remoteIP.equals(ipAddress) && remotePort == port) {
                        client.close();
                        Status.clientClose(remoteIP + ":" + remotePort);
                        key.cancel();
                        return;
                    }
                }

            } catch (IOException e) {
                Status.failedTodisconnect();
            }
        }
    }
    
    /**
     * Map<"remoteIP:remotePort", "localPort">
     */
    public Map<String, String> getAllConnectionsWithLocalPort() {
        Map<String, String> result = new HashMap<>();

        Set<SelectionKey> channelKeys = selector.keys();

        Iterator<SelectionKey> iter = channelKeys.iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            SelectableChannel channel = key.channel();
            try {
                SocketChannel client;
                if(!(channel instanceof ServerSocketChannel)) {
                    client = (SocketChannel) channel;
                    if(client.isConnected()){
                        InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) client.getRemoteAddress();
                        String remoteIP = remoteInetSocketAddress.getAddress().getHostAddress();
                        int remotePort = remoteInetSocketAddress.getPort();

                        InetSocketAddress localInetSocketAddress = (InetSocketAddress) client.getLocalAddress();
                        int localPort = localInetSocketAddress.getPort();

                        String k = remoteIP + ":" + remotePort;

                        result.put(k, "" + localPort);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void sendString(String ip, int port, String s) {
        sendMessageQueue.addLast(new MessagePack(ip, port, s));
        Status.addStringToSendQueue(s, sendMessageQueue.size(), ip, port);
    }

    // HELPER

    private void setup() {
        try {
            //TODO: selector muss wieder geschlossen werden (serversocketchannel auch)
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();

            // listening Socket erstellen
            serverSocketChannel.bind(new InetSocketAddress(idPort));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //Logger.log("Server gestartet auf Port " + idPort);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(e.getMessage());
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        Status.acceptConnection(client.getRemoteAddress().toString());
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
    }

    private void readMessage(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        
        // Header auslesen
        ByteBuffer headerBuffer = ByteBuffer.allocate(COMMON_HEADER_LENGTH);
        headerBuffer.position(0);

        // Es könnte sein, dass die Gegenseite die Verbindung abrupt
        // abgebrochen hat
        if (!clientHealthy(client, key)) {
            return;
        }
        int readBytes;
        try {
            readBytes = client.read(headerBuffer);
        
        
            if (readBytes == -1) {
            client.close();
                return;
            }

            // TODO: Länge sollte als unsigned int interpretiert werden (wegen Absprache)
            int messageLength = headerBuffer.getInt(0);

            // int muss überschrieben werden, damit der Long so ausgelesen werden kann,
            // wie er geschrieben wurde
            headerBuffer.putInt(0,0);
            long crc32 = headerBuffer.getLong(0);

            // eigentliche Nachricht auslesen
            ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
            readBytes = client.read(messageBuffer);

            
            if (readBytes == -1) {
                client.close();
                return;
            }
            messageBuffer.position(0);
            CharBuffer cb = StandardCharsets.UTF_8.decode(messageBuffer);
            String string = cb.toString();

            // Nachricht wird nur wahrgenommen, wenn die Checksumme richtig ist
            boolean isValidChecksum = isValidChecksum(crc32, string);
            Status.bufferConsumed(string, messageLength, crc32, isValidChecksum);
            if (isValidChecksum) {
                receiveMessageQueue.addLast(string);
                Status.addStringToReceiveQueue(string, receiveMessageQueue.size());
            }
            else {
                Status.invalidChecksum();
            }
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("Connection reset by peer") || errorMessage.equals("Connection reset")) {
                Status.lostConnection();
                try {
                    client.close();
                } catch (IOException e1) {
                }
                key.cancel();
            }
            else {
                e.printStackTrace();
            }
        }
        
    }

    private void writeMessage(SelectionKey key) {
        if (sendMessageQueue.isEmpty()) {
            return;
        }

        SocketChannel client = (SocketChannel) key.channel();
        try {
            SocketAddress remoteAddress = client.getRemoteAddress();
            InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
            String remoteIP = inetSocketAddress.getAddress().getHostAddress();
            int remotePort = inetSocketAddress.getPort();
            String remoteIPPort = remoteIP + ":" + remotePort;
            String destinationIPPort = sendMessageQueue.peek().getIPPort();
            if (!remoteIPPort.equals(destinationIPPort)) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String string = sendMessageQueue.removeFirst().getMessage();
        ByteBuffer buffer = getFromattedByteBuffer(string);
        buffer.position(0);
        
        
        try {
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
            buffer.clear();
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("Connection reset by peer") || errorMessage.equals("Connection reset")) {
                Status.lostConnection();
                try {
                    client.close();
                    Status.clientClose(client.getRemoteAddress().toString()); //passiert öfter, unbedingt gute fehlermeldung
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                key.cancel();
            } 
            else {
                e.printStackTrace();
            }
        }
    }

    private void continiueConnect(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            if (client.isConnectionPending()) {
                if (client.finishConnect()) {
                    Status.connectionFinised(client.getRemoteAddress());
                    client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            }  
        } catch (ConnectException e) {
                Status.failedToConnect(e.getMessage());  
        } catch (IOException e) {
            Logger.log("Error in continiueConnect:");
            e.printStackTrace();
        } 
    }

    private ByteBuffer getFromattedByteBuffer(String string) {
        byte[] jsonString = string.getBytes(Charset.forName("UTF-8"));
        int length = jsonString.length;
        long checksum = CRC32Checksum.crc32(string);
        ByteBuffer buffer = ByteBuffer.allocate(COMMON_HEADER_LENGTH + length);
        
        //buffer.putLong(0, checksum);
        buffer.putLong(0, checksum);
        buffer.putInt(0, length);
        buffer.put(8, jsonString);

        
        Status.bufferCreated(string, length, checksum, isValidChecksum(checksum, string));
        return buffer;
    }

    // TODO: testen, ob diese Methode funktioniert
    // Die Frage ist vor allem, ob man einen long mit einem int vergleichen kann
    private boolean isValidChecksum(long checksum, String text) {
        long expectedChecksum = CRC32Checksum.crc32(text);
        return expectedChecksum == checksum;
    }

    /**
     * Überprüft ob die Verbindung zur anderen Seite noch offen ist.
     * Es könnte sein, dass die andere Seite die Verbindung abrupt abgebrochen hat.
     * In diesem Fall von dieser Seite die Verbindung geschlossen werden. 
     * Der key wird dabei auch aus dem Selector entfernt.
     */
    private boolean clientHealthy(SocketChannel client, SelectionKey key) {
        if (!client.isConnected()) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            key.cancel();
            return false;
        }
        else {
            return true;
        }
    }

    public Set<RoutingEntry> ckeckConnections(Set<RoutingEntry> entries) {
        // To check if connections got lost we first collect all healthy connections
        // and then ask for every entry if the corrosponding connection is in this collection
        Set<SelectionKey> keys = selector.keys();
        Set<String> connectedAdresses = new HashSet<String>();
        for (SelectionKey key : keys) {
            if(key.isValid()) {
                SelectableChannel channel = key.channel();
                if(!(channel instanceof ServerSocketChannel)) {
                    SocketChannel client = (SocketChannel) channel;
                    try {
                        connectedAdresses.add(client.getRemoteAddress().toString());
                    } catch(IOException e) {
                        Logger.log(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        Set<RoutingEntry> lostConnections = new HashSet<RoutingEntry>();
        for (RoutingEntry entry : entries) {
            if (!connectedAdresses.contains("/" + entry.getNextHop())) {
                lostConnections.add(entry);
                Status.clientClose(entry.getDestination());
            }
        }
        return lostConnections;
    }

    @Override
    public String getLocalIP() {
        return this.ip;
    }


    @Override
    public int getLocalIDPort() {
        return this.idPort;
    }
}
