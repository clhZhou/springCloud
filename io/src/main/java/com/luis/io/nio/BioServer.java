package com.luis.io.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author luis
 * @date 2020/11/9
 */
public class BioServer {

    public void lister() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            if (len > 0) {
                String msg = new String(bytes, 0, len);
                System.out.println("收到: " + msg);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new BioServer().lister();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
