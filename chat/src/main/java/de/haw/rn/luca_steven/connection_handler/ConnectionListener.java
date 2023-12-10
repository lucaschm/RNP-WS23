package de.haw.rn.luca_steven.connection_handler;

import de.haw.rn.luca_steven.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ConnectionListener {

    private static final int COMMON_HEADER_LENGTH = 8;
    private static boolean isRunning = true;
    private static int idPort = 6789;
    private static LinkedList<String> messageQueue = new LinkedList<>();
    

    public static void main(String[] args) {

        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            
            // listening Socket erstellen
            serverSocketChannel.bind(new InetSocketAddress(idPort));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_CONNECT);

            Logger.log("Server gestartet auf Port " + idPort);

            while (isRunning) {
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    if (key.isAcceptable()) {
                        register(selector, serverSocketChannel);
                    }

                    if (key.isReadable()) {
                        answerWithEcho(key);
                    }

                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        logSocketInfo(client);
    }

    private static void answerWithEcho(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        
        // Header auslesen
        ByteBuffer headerBuffer = ByteBuffer.allocate(COMMON_HEADER_LENGTH);
        int readBytes = client.read(headerBuffer);
        
        if (readBytes == -1) {
           client.close();
            return;
        }

        int messageLength = headerBuffer.getInt(0);
        int crc32 = headerBuffer.getInt(1);

        // eigentliche Nachricht auslesen
        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        readBytes = client.read(messageBuffer);

        if (readBytes == -1) {
            client.close();
            return;
        }

        String string = StandardCharsets.UTF_8.decode(messageBuffer).toString();
        messageQueue.addLast(string);
        
    }

    private static void logSocketInfo(SocketChannel socketChannel) throws IOException {
        InetSocketAddress localAddress = (InetSocketAddress) socketChannel.getLocalAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();

        System.out.println("Connection Info: ");
        System.out.println("Local Address: " + localAddress.getAddress().getHostAddress() + ":" + localAddress.getPort());
        System.out.println("Remote Address: " + remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort());
        System.out.println("Protocol: TCP");

        // Hier können Sie zusätzliche Socket-Optionen loggen
        for (SocketOption<?> option : socketChannel.supportedOptions()) {
            System.out.println(option.name() + ": " + socketChannel.getOption(option));
        }
    }


    // GETTER
    public static LinkedList<String> getMessageQueue() {
        return messageQueue;
    }

}
