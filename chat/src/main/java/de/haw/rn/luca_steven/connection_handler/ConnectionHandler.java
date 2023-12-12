package de.haw.rn.luca_steven.connection_handler;

import de.haw.rn.luca_steven.Logger;
import de.haw.rn.luca_steven.data_classes.ChatMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ConnectionHandler implements IConnectionHandler{

    private static final int COMMON_HEADER_LENGTH = 8;
    private final int idPort;
    private final LinkedList<String> messageQueue;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public ConnectionHandler(int idPort) {
        this.idPort = idPort;
        this.messageQueue = new LinkedList<>();
        setup();
    }

    
    //diese Methode sollte regelmäßig wiederholt werden
    @Override
    public void listen() {

        try {
            //while (isRunning) {
                selector.select();
                //Logger.log(idPort + ": selector select");

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                //Logger.log(idPort + ": get selectedKeys");

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
                //Logger.log(idPort + ": while(iter.hasNext())");
            //}
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();;
        }
    }

    @Override
    public String nextString() {
        return messageQueue.removeFirst();
    }

    public boolean hasNext() {
        return !messageQueue.isEmpty();
    }

    @Override
    public void connect(String ipAddress, int port) {
        try {
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            SocketAddress socketAddr;
            socketAddr = new InetSocketAddress(ipAddress, port);
            boolean success = client.connect(socketAddr);
            
            if (success) {
                client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                Logger.log("connected to: " + client.getRemoteAddress());
            } else {
                client.register(selector, SelectionKey.OP_CONNECT);
                Logger.log("wating to connected to: " + client.getRemoteAddress());
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

    @Override
    public void disconnect(String ipAddress, int port) {

    }

    @Override
    public void sendString(String ip, int port, String s) {
        messageQueue.addLast(s);
        //Logger.log(message + " was added to queue size: " + messageQueue.size());
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

            Logger.log("Server gestartet auf Port " + idPort);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(e.getMessage());
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        Logger.log("got connection from: " + client.getRemoteAddress());
    }

    private void readMessage(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        
        // Header auslesen
        ByteBuffer headerBuffer = ByteBuffer.allocate(COMMON_HEADER_LENGTH);
        int readBytes = client.read(headerBuffer);
        
        if (readBytes == -1) {
           client.close();
            return;
        }

        //TODO: Länge muss als unsigned int interpretiert werden
        int messageLength = headerBuffer.getInt(0);
        int crc32 = headerBuffer.getInt(1);

        // eigentliche Nachricht auslesen
        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        readBytes = client.read(messageBuffer);

        if (readBytes == -1) {
            client.close();
            return;
        }
        messageBuffer.position(0);
        String string = StandardCharsets.UTF_8.decode(messageBuffer).toString();
        Logger.log(idPort + ": String received: " + string);
        messageQueue.addLast(string);
        
    }

    private void writeMessage(SelectionKey key) {
        if (messageQueue.isEmpty()) {
            return;
        }

        SocketChannel client = (SocketChannel) key.channel();
        try {
            SocketAddress remoteAddress = client.getRemoteAddress();
            InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
            String remoteIP = inetSocketAddress.getAddress().getHostAddress();
            int remotePort = inetSocketAddress.getPort();
            String remoteIPPort = remoteIP + ":" + remotePort;
            String destinationIPPort = messageQueue.peek();//TODO
            if (!remoteIPPort.equals(destinationIPPort)) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String string = messageQueue.removeFirst();
        ByteBuffer buffer = getFromattedByteBuffer(string);
        //Logger.log("limit: " + buffer.limit());
        buffer.position(0);
        
        
        try {
            int success = 0;
            
            while (buffer.hasRemaining()) {
                success =  client.write(buffer);
            }
            Logger.log("written=" + success + " " + " Bytes to " + client.getRemoteAddress());
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continiueConnect(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            if (client.isConnectionPending()) {
                client.finishConnect();
                Logger.log("finished connecting to: " + client.getRemoteAddress());
            }
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            Logger.log("Error in continiueConnect:");
            e.printStackTrace();
        }
    }

    private void logSocketInfo(SocketChannel socketChannel) throws IOException {
        InetSocketAddress localAddress = (InetSocketAddress) socketChannel.getLocalAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();

        Logger.log("Connection:");
        System.out.println("Local Address: " + localAddress.getAddress().getHostAddress() + ":" + localAddress.getPort());
        System.out.println("Remote Address: " + remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort());
        System.out.println("Protocol: TCP");

        //Hier können Sie zusätzliche Socket-Optionen loggen
        for (SocketOption<?> option : socketChannel.supportedOptions()) {
           System.out.println(option.name() + ": " + socketChannel.getOption(option));
        }
    }

    private ByteBuffer getFromattedByteBuffer(String string) {
        int length = string.length();
        int checksum = 0; //TODO: checksummenberechnung
        ByteBuffer buffer = ByteBuffer.allocate(COMMON_HEADER_LENGTH + length);
        
        buffer.putInt(length); //TODO: Länge muss als unsigned int betrachtet werden
        buffer.putInt(checksum);

        byte[] stringBytes = string.getBytes(Charset.forName("UTF-8"));
        buffer.put(stringBytes);

        return buffer;
    }
}
