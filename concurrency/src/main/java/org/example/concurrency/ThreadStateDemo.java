package org.example.concurrency;

import org.example.concurrency.thread.Runability;

import java.util.concurrent.CountDownLatch;

/**
 *
 **/
public class ThreadStateDemo {
    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(4);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new Runability(countDownLatch));
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
