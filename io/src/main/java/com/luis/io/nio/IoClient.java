package com.luis.io.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author luis
 * @date 2020/11/9
 */
public class IoClient {

    public void client() throws IOException {
        Socket socket = new Socket("localhost",8080);
        OutputStream outputStream = socket.getOutputStream();
        String UUID = java.util.UUID.randomUUID().toString();
        outputStream.write(UUID.getBytes());
        System.out.println("发送: "+UUID);
    }

    public static void main(String[] args) {
        try {
            new IoClient().client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
