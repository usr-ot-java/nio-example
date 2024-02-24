package ru;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerNIO {
    public static final int PORT = 8080;
    public static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(HOST, PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("While iteration");
            selector.select(); // Blocking call to wait for at least 1 event
            var keys = selector.selectedKeys();
            for (SelectionKey selectionKey : keys) {
                if (selectionKey.isAcceptable()) {
                    register(selector, serverSocket);
                }
                if (selectionKey.isReadable()) {
                    answer(selectionKey);
                }
            }
        }
    }

    public static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        System.out.println("Registered client");
        SocketChannel client = serverSocket.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
    }

    public static void answer(SelectionKey selectionKey) throws IOException {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        int r = client.read(byteBuffer);
        if (r == -1) {
            System.out.println("Closing channel");
            client.close();
        } else {
            byteBuffer.flip();
            StringBuilder sb = new StringBuilder();
            while (byteBuffer.hasRemaining()) {
                sb.append((char) byteBuffer.get());
            }
            String data = sb.toString();
            if (data.trim().equals("exit")) {
                System.out.println("Exiting. Bye client!");
                client.close();
            } else {
                System.out.println("Received message:" + data);
            }
        }
    }
}
