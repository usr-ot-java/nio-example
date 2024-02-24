package ru;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static ru.ServerNIO.HOST;
import static ru.ServerNIO.PORT;

public class ClientNIO {
    public static void main(String[] args) throws Exception {
        String[] messages = {"I like non-blocking servers", "Hello non-blocking world!", "One more message..", "exit"};
        System.out.println("Starting client...");

        try(SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT))) {
            for (String msg : messages) {
                System.out.println("Prepared message: " + msg);

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(msg.getBytes());
                buffer.flip();
                int bytesWritten = socketChannel.write(buffer);
                System.out.printf("Sending Message: %s\n written bufferBytes: %d%n", msg, bytesWritten);
            }
        }
    }
}
