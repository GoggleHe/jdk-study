package org.example.concurrency;

import org.example.concurrency.thread.ForkJoinTaskImpl;

import java.util.concurrent.ForkJoinTask;

/**
 *
 **/
public class ForkJoinDemo {
    public static void main(String[] args) {
        //Fork/Join方式
        ForkJoinTaskImpl task = new ForkJoinTaskImpl();
        ForkJoinTask<Integer> fork = task.fork();
        Integer integer = fork.join();
        System.out.println(integer);
    }
}
