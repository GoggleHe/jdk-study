package org.eaxmple.io.aio;

public class AioTimeClient {
    public static void main(String[] args) {
        AsyncTimeClientHandler asyncTimeClienthandler = new AsyncTimeClientHandler("127.0.0.1", 8080);

        new Thread(asyncTimeClienthandler).start();
    }
}
