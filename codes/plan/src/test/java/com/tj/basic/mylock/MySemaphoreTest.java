package com.tj.basic.mylock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MySemaphoreTest {


    @Test
    void acquire() throws InterruptedException {
        MySemaphore semaphore = new MySemaphore(1);
        Runnable t =()->{
            log.info("myname {} will get semaphore.",Thread.currentThread().getName());
            semaphore.acquire(1);
            log.info("myname {}  get semaphore.",Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("name :"+Thread.currentThread().getName(),e);
            }
            log.info("myname {} will release semaphore",Thread.currentThread().getName());
            semaphore.release(1);
            log.info("myname {}  release semaphore.",Thread.currentThread().getName());
        };
        Thread a = new Thread(t,"ta");
        Thread b = new Thread(t,"tb");
        a.start();
        b.start();
        a.join();
        b.join();
    }

    @Test
    void release() {
    }
}