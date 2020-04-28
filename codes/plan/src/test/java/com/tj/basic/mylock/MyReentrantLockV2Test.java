package com.tj.basic.mylock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MyReentrantLockV2Test {

    @Test
    void lock() {
        MyReentrantLockV2 lock =new MyReentrantLockV2();
        Runnable runnable=()->{
            System.out.println(Thread.currentThread().getName()+" try  get lock");
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"  get lock");
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName()+"  get lock again");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(Thread.currentThread().getName()+" will release lock");
//            lock.unLock();
        };
        int counts=10;
        Thread[] threads = new Thread[counts];
        for (int i=1;i<=counts;i++){
            threads[i-1] =new Thread(runnable,"thread"+i);
            threads[i-1].setDaemon(false);
        }
        for (int i=1;i<=counts;i++){
            threads[i-1].start();
        }

//        assertEquals(1,lock.getExcludeThreadCounts());
        try {
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(threads.length-1,lock.getWaitingThreaCounts());
    }

    @Test
    void unLock() {
        MyReentrantLockV2 lock =new MyReentrantLockV2();
        Runnable runnableException=()->{
            System.out.println(Thread.currentThread().getName()+" try  release lock");
            lock.unLock();
        };
        Thread thread =new Thread(runnableException,"runnableException");
        //当前线程执行才会抛出异常，否则，异常会在thread抛出 ，导致这里的assetThrows获取不到异常。
        Throwable exception = assertThrows(IllegalMonitorStateException.class,()->thread.run());
        assertEquals("current state is 0,but wanto release more...",exception.getMessage());
    }

    @Test
    void unLock2() throws InterruptedException {
        MyReentrantLockV2 lock =new MyReentrantLockV2();
        Runnable t=()->{
            System.out.println(Thread.currentThread().getName()+" try   lock");
            lock.lock();
            try {

                Thread.sleep(3000);
                lock.unLock();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        };
        Thread thread =new Thread(t,"thread_abcd");
        //当前线程执行才会抛出异常，否则，异常会在thread抛出 ，导致这里的assetThrows获取不到异常。
        thread.start();
        Thread.sleep(1000);
        assertEquals(lock.getCurrentLockThreadName(),thread.getName());
        Thread.sleep(4000);
        System.out.println("current:"+Thread.currentThread().getName());
        System.out.println("locked:"+lock.getCurrentLockThreadName());
        assertNotEquals(lock.getCurrentLockThreadName(),Thread.currentThread());//说明锁释放的时候，如果没有清除current持有的标记 。
    }
}