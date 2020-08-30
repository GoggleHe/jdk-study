package org.eaxmple.io.bio;

import org.eaxmple.io.bio.handler.TimeSeverHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用bio结合线程池完成伪异步IO操作
 */
public class FakeAIOTimeServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                executorService.submit(new Thread(new TimeSeverHandler(socket)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
