package com.tj.basic.mylock;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
class ReentrantReadWriteLockTest {


    /**
     * @description: 读写锁是互斥的，并且读锁在尝试获取时，如果前面有血写锁在排队，即使现在锁为读锁。后续线程也不能获取到这个读锁。
     * </br>
     * 参考源码：
     *  final boolean apparentlyFirstQueuedIsExclusive() {
     *      *         Node h, s;
     *      *         return (h = head) != null &&
     *      *             (s = h.next)  != null &&
     *      *             !s.isShared()         &&
     *      *             s.thread != null;
     *      *     }
     *      官方解释是为了避免写锁饥饿。这也解决了我的疑问。(写锁不会因为读锁很多而饥饿！)
     * @return:
     * @author: tongjie
     * @date: 2020/4/28
     */
    @Test
    void lock() throws InterruptedException {
        ReentrantReadWriteLock lock =new ReentrantReadWriteLock(false);
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        int counts=10;
        int wcounts=10;
        CountDownLatch countDownLatch=new CountDownLatch(counts+wcounts);
        Runnable readThread=()->{
           log.info(Thread.currentThread().getName()+" try get rlock");
            readLock.lock();
            log.info(Thread.currentThread().getName()+"  get rlock");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName()+" will release rlock");
            readLock.unlock();
            countDownLatch.countDown();
        };

        Runnable writeThread=()->{
            log.info(Thread.currentThread().getName()+" try get writeLock");
            writeLock.lock();
            log.info(Thread.currentThread().getName()+"  get writeLock");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName()+" will release writeLock");
            writeLock.unlock();
            countDownLatch.countDown();
        };

        Thread[] threads = new Thread[counts];
        for (int i=1;i<=counts;i++){
            threads[i-1] =new Thread(readThread,"read"+i);
            threads[i-1].setDaemon(false);
        }

        Thread[] wthreads = new Thread[counts];
        for (int i=1;i<=wcounts;i++){
            wthreads[i-1] =new Thread(writeThread,"write"+i);
            wthreads[i-1].setDaemon(false);
        }
        for (int i=1;i<=counts;i++){
            threads[i-1].start();
//            wthreads[i-1].start();
//            Thread.sleep(100);
        }

        for (int i=1;i<=counts;i++){
//            threads[i-1].start();
//            Thread.sleep(100);
            wthreads[i-1].start();
        }

        log.info("waiting over...");
        countDownLatch.await();
        log.info("all over...");
    }

}