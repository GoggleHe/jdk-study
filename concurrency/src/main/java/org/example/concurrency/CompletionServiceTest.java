package org.example.concurrency;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletionServiceTest {

    @Test
    void starter(){
        Callable<Integer> callable = () -> {
            TimeUnit.SECONDS.sleep(3);
            return 123;
        };
        Callable<Integer> callable2 = () -> {
            TimeUnit.SECONDS.sleep(4);
            return 2;
        };
        Callable<Integer> callable3 = () -> {
            TimeUnit.SECONDS.sleep(5);
            return 3;
        };


        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ExecutorCompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(executorService);
        executorCompletionService.submit(callable);
        executorCompletionService.submit(callable2);
        executorCompletionService.submit(callable3);
        List<Future<Integer>> list = new ArrayList<>();
        try {
            for (int i = 0; i < 3; i++) {
                Future<Integer> take = executorCompletionService.take();
                list.add(take);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        for (Future<Integer> future : list) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
