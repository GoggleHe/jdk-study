package org.eaxmple.io.aio;

public class AioTimeServer {
    public static void main(String[] args) {
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(8080);

        new Thread(asyncTimeServerHandler).start();
    }
}
