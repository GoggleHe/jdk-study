package org.example.concurrency.thread;

import java.util.concurrent.CountDownLatch;

/**
 *
 **/
public class Runability implements Runnable {
    private CountDownLatch countDownLatch;

    public Runability(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        synchronized (this) {

            long id = Thread.currentThread().getId();
            System.out.println(id + " run() start");

            Thread.currentThread().interrupt();

            System.out.println(id + "-" + Thread.currentThread().getState().name());
            System.out.println(id + "run() end");
            countDownLatch.countDown();
        }
    }
}
