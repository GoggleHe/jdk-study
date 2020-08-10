package org.example.concurrency.thread;

/**
 *
 **/
public class InterruptRunability implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            System.out.println("start");

            System.out.println("end");
        }
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
