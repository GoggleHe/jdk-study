package org.eaxmple.io.nio;

public class NioTimeServer {
    public static void main(String[] args) {
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer();

        new Thread(multiplexerTimeServer).start();
    }
}
