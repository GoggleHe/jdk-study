package org.example.concurrency.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

@DisplayName("StampedLock使用示例")
public class StampedLockTest {
    private int counts = 0;

    private int x = 0;
    private int y = 1;

    @DisplayName("乐观读使用示例")
    @Test
    void tryOptimisticRead() {
        StampedLock stampedLock = new StampedLock();
        //成功返回一个标记，若锁已被独占返回0
        long stamp = stampedLock.tryOptimisticRead();

        int varx = x;
        int vary = y;
        //乐观读没有锁，需要校验数据是否已被修改
        if (!stampedLock.validate(stamp)) {
            //若数据已被其他线程修改，改为悲观读锁
            stamp = stampedLock.readLock();
            try {
                varx = x;
                vary = y;
            } finally {
                stampedLock.unlock(stamp);
            }
        }
    }

    @Test
    void test(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        int andIncrement = atomicInteger.getAndIncrement();
        System.out.println(andIncrement);
        System.out.println(atomicInteger.get());

        AtomicInteger atomicInteger2 = new AtomicInteger(0);
        int incrementAndGet = atomicInteger2.incrementAndGet();
        System.out.println(incrementAndGet);
        System.out.println(atomicInteger2.get());
    }
}
