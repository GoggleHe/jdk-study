package org.eaxmple.io.nio;

public class NioTimeClient {
    public static void main(String[] args) {
        TimeClientHandler timeClientHandler = new TimeClientHandler("192.168.189.1", 8080);

        new Thread(timeClientHandler).start();
    }
}
