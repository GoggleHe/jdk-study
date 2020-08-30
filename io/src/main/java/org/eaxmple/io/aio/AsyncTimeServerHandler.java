package org.eaxmple.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
    private int port;

    private AsynchronousServerSocketChannel serverSocketChannel;

    private CountDownLatch countDownLatch;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("AIO Server init succeed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);

        System.out.println("doAccept() start");
        doAccept();
        System.out.println("doAccept() end");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("run.end");
    }

    private void doAccept() {
        serverSocketChannel.accept(this, new AcceptCompletionHandler());
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
