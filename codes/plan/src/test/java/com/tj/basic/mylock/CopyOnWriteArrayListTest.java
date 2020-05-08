package com.tj.basic.mylock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author tongjie
 * @version 1.0.0
 * @ClassName CopyOnWriteArrayListTest.java
 * @Description TODO
 * @createTime 2020年05月08日 21:44:00
 */
@Slf4j
public class CopyOnWriteArrayListTest {

    @Test
    public void testCopy() throws InterruptedException {
        CopyOnWriteArrayList<Integer> copy = new CopyOnWriteArrayList<Integer>();
        copy.add(1);
        Thread read =new Thread(()->{
            try {
                Thread.sleep(200);//读线程仍然
                log.info("read begin get size.");
                final int size = copy.size();
                log.info("read get size=={}",size);
                Thread.sleep(800);
                log.info("after write,read size=={}", copy.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"read");

        Thread write =new Thread(()->{
            try {
                Thread.sleep(400);
                log.info("write begin add element 2.");
                copy.add(2);
                log.info("write end add element 2.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"write");
        read.start();
        write.start();
        read.join();
        log.info("main over.");
    }
}
