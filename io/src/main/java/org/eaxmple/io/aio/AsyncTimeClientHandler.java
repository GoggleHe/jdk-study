package org.eaxmple.io.aio;

public class AsyncTimeClientHandler implements Runnable{
    private String host;

    private int port;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

    }
}
