package org.example.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 **/

public class CompletableFutureTest {

    @Test
    void starter() {
        Runnable runnable = () -> {
            System.out.println("线程执行了！");
        };
        //默认线程池执行无返回方法
        CompletableFuture.runAsync(runnable);

        Supplier<String> supplier = () -> {
            System.out.println("返回了个东西");
            return "hello ";
        };
        //默认线程池执行有返回方法
        CompletableFuture.supplyAsync(supplier);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        //自定义线程池执行无返回方法
        CompletableFuture.runAsync(runnable, threadPoolExecutor);
        //自定义线程池执行有返回方法
        CompletableFuture.supplyAsync(supplier, threadPoolExecutor);


    }

    @Test
    void allOf() {
        long start = System.currentTimeMillis();
        Runnable runnable = () -> {
            System.out.println("线程1执行了！");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(runnable);
        Runnable runnableException = () -> {
            System.out.println("线程2执行了！");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(runnableException);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(voidCompletableFuture1, voidCompletableFuture);
        try {
            completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    void anyOf() {
        long start = System.currentTimeMillis();
        Runnable runnable = () -> {
            System.out.println("线程1执行了！");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        CompletableFuture voidCompletableFuture1 = CompletableFuture.runAsync(runnable);
        Runnable runnableException = () -> {
            System.out.println("线程2执行了！");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(runnableException);
        CompletableFuture completableFuture = CompletableFuture.anyOf(voidCompletableFuture1, voidCompletableFuture);
        try {
            completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    void then() {
        Runnable runnable = () -> {
            System.out.println("线程1执行了！");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1执行完了！");
        };
        CompletableFuture completableFuture = CompletableFuture.runAsync(runnable);
        Runnable runnable2 = () -> {
            System.out.println("线程2执行了！");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2执行完了！");
        };
        completableFuture.thenRun(runnable2);
        completableFuture.join();

    }

}
