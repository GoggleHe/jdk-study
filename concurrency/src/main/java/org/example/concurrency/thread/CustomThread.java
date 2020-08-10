package org.example.concurrency.thread;

/**
 *
 **/
public class CustomThread extends Thread {

    @Override
    public void run() {
        System.out.println("CustomThread 执行");
    }

}
