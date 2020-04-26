package com.tj.basic.mylock;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyReentrantLockTest {


    @Test
    void lock() {
        MyReentrantLock lock =new MyReentrantLock();
        Runnable runnable=()->{
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

        assertEquals(threads.length-1,lock.getWaitingThreaCounts());
    }

    @Test
    void unLock() {
    }
}