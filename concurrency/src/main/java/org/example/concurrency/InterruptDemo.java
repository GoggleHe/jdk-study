package org.example.concurrency;

import org.example.concurrency.thread.InterruptRunability;

/**
 *
 **/
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptRunability());


        thread.start();

        thread.interrupt();
        thread.join();

    }
}
