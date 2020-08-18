package org.example.concurrency.thread;

import java.util.concurrent.ForkJoinTask;

/**
 *
 **/
public class ForkJoinTaskImpl extends ForkJoinTask<Integer> {


    @Override
    public Integer getRawResult() {
        System.out.println("getRawResult");
        return 100;
    }

    @Override
    protected void setRawResult(Integer value) {
        System.out.println("setRawResult = " + value);

    }

    @Override
    protected boolean exec() {
        System.out.println("exec");
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
