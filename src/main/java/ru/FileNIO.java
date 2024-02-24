package ru;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileNIO {
    public static void main(String[] args) throws Exception {
        try (FileInputStream fis = new FileInputStream("input.txt");
            FileOutputStream fos = new FileOutputStream("output.txt")) {
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();

            ByteBuffer inBuffer = ByteBuffer.allocate(128);
            ByteBuffer outBuffer = ByteBuffer.allocate(128);
            int rd = inChannel.read(inBuffer);
            while (rd != -1) {
                System.out.printf("Rd = %d%n", rd);

                inBuffer.flip();
                outBuffer.put(inBuffer);
                inBuffer.flip();

                outBuffer.flip();
                outChannel.write(outBuffer);
                outBuffer.flip();
                rd = inChannel.read(inBuffer);
            }
        }
    }
}
