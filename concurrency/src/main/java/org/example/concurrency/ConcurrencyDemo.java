package org.example.concurrency;

import org.example.concurrency.thread.CustomThread;
import org.example.concurrency.thread.ForkJoinTaskImpl;
import org.example.concurrency.thread.Runability;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

@DisplayName("并发案例")
public class ConcurrencyDemo {

    @Nested
    @DisplayName("多线程")
    class MultiThread {

        @DisplayName("创建线程的几种方式")
        @Test
        void shouldStartThread() {
            //继承Thread类方式
            CustomThread thread = new CustomThread();
            thread.start();

            //实现Runable接口方式
            Runnable runnable = () -> System.out.println("Runable 线程 执行了");
            new Thread(runnable).start();

            //实现Callable接口方式
            Callable<String> callable = () -> "Callable 返回了";
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<String> future = executorService.submit(callable);
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        @DisplayName("ForkJoin")
        @Test
        void forkJoinPool() throws ExecutionException, InterruptedException {
            //Fork/Join方式
            ForkJoinTaskImpl task = new ForkJoinTaskImpl();
            ForkJoinTask<Integer> fork = task.fork();
            Integer integer = fork.join();
            System.out.println(integer);

        }

        @DisplayName("线程的join方法")
        @Test
        void threadState() {
            CountDownLatch countDownLatch = new CountDownLatch(100);
            for (int i = 0; i < 100; i++) {
                new Thread(new Runability(countDownLatch)).start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }
}
