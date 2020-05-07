package com.tj.basic.mylock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MySemaphoreTest {


    @Test
    void acquire() throws InterruptedException {
        MySemaphore semaphore = new MySemaphore(1);
        Runnable ta =()->{
            log.info("myname {} will get semaphore.",Thread.currentThread().getName());
            semaphore.acquire(1);
            log.info("myname {}  get semaphore.",Thread.currentThread().getName());
            try {
                assertEquals(false,Thread.currentThread().isInterrupted());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("name :"+Thread.currentThread().getName(),e);
            }
            log.info("myname {} will release semaphore",Thread.currentThread().getName());
            semaphore.release(1);
            log.info("myname {}  release semaphore.",Thread.currentThread().getName());
        };

        Runnable tb =()->{
            log.info("myname {} will get semaphore.",Thread.currentThread().getName());
            semaphore.acquire(1);
            log.info("myname {}  get semaphore.",Thread.currentThread().getName());
            try {
                assertEquals(true,Thread.currentThread().isInterrupted());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("name :"+Thread.currentThread().getName(),e);
            }
            log.info("myname {} will release semaphore",Thread.currentThread().getName());
            semaphore.release(1);
            log.info("myname {}  release semaphore.",Thread.currentThread().getName());
        };
        Thread a = new Thread(ta,"ta");
        Thread b = new Thread(tb,"tb");
        a.start();
        Thread.sleep(100);//让a先运行
        b.start();
        a.join();
        b.join();
    }

    @Test
    void release() {
    }
}