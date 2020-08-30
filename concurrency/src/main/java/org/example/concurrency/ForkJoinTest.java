package org.example.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {
    private static final long n = 40;
    static class FibonacciTask extends RecursiveTask<Long> {
        private Long index;

        public FibonacciTask(Long index) {
            this.index = index;
        }

        @Override
        protected Long compute() {
            if (index <= 0) {
                return 0L;
            }

            if (index == 1 || index == 2) {
                return 1L;
            }
            FibonacciTask taskLeft = new FibonacciTask(index - 1);
            FibonacciTask taskRight = new FibonacciTask(index - 2);
            invokeAll(taskLeft, taskRight);
            Long joinLeft = taskLeft.join();
            Long joinRight = taskRight.join();
            return joinLeft + joinRight;
        }
    }


    @Test
    void starter() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        FibonacciTask fibonacciTask = new FibonacciTask(n);

        Long invoke = forkJoinPool.invoke(fibonacciTask);

        System.out.println(invoke);

    }

    long fibonacci(long n){
        if (n == 1 || n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    @Test
    void testFibonacci(){
        long fibonacci = fibonacci(n);
        System.out.println(fibonacci);

    }

}
