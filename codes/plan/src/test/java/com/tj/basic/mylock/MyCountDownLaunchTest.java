package com.tj.basic.mylock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MyCountDownLaunchTest {

    /**
     * @param: null
     * @description: 该测试表明，当countdown的数量大于初始值时会抛出异常
     * @return:
     * @author: tongjie
     * @date: 2020/5/7
     */

    @Test
    void countDown() throws InterruptedException {
        int threadCounts=10;
        int countDownLatchv=8;
        MyCountDownLaunch countDownLaunch=new MyCountDownLaunch(countDownLatchv);
        Thread[] threads = new Thread[threadCounts];
        for (int i = 0; i < threadCounts; i++) {
            threads[i]  = new Thread(()->{
                countDownLaunch.countDown();
            });
        }
        for (int i = 0; i < countDownLatchv; i++) {
            threads[i].start();
        }
        Thread.sleep(1000);
        //这样才是抛异常的正确用法吧
        Throwable exception = assertThrows(IllegalStateException.class, ()->countDownLaunch.countDown());
        assertEquals("state must be great than 0.", exception.getMessage());
    }

    @Test
    void await() {
        int threadCounts=10;
        MyCountDownLaunch countDownLaunch=new MyCountDownLaunch(threadCounts);
        Thread[] threads = new Thread[threadCounts];
        long begin=System.currentTimeMillis();
        for (int i = 0; i < threadCounts; i++) {
            threads[i]  = new Thread(()->{
                try {
                    Thread.sleep(1000);//线程休息1秒
                } catch (InterruptedException e) {//do nothing
                    e.printStackTrace();
                }
                log.info("thread name {},",Thread.currentThread().getName(),"will count down.");//肯定会全部输出这个之后输出cost
                countDownLaunch.countDown();
            });
        }
        for (int i = 0; i < threadCounts; i++) {
            threads[i].start();
        }
        countDownLaunch.await();
        long end=System.currentTimeMillis();
        long cost = end -begin;//花费的毫秒数，应该是大约等于线程执行的时间（大于1秒，算上上下文切换）
        log.info("cost ={}",cost);
        assertTrue(cost>1000);

    }

    @Test
    void awaittest2() {
        int threadCounts=10;
        MyCountDownLaunch countDownLaunch=new MyCountDownLaunch(threadCounts);
        Thread[] threads = new Thread[threadCounts];
        long begin=System.currentTimeMillis();
        for (int i = 0; i < threadCounts; i++) {
            threads[i]  = new Thread(()->{
                try {
                    Thread.sleep(1000);//线程休息1秒
                } catch (InterruptedException e) {//do nothing
                    e.printStackTrace();
                }
                log.info("thread name {},",Thread.currentThread().getName(),"will count down.");//肯定会全部输出这个之后输出cost
                countDownLaunch.countDown();
            });
        }
        for (int i = 0; i < threadCounts; i++) {
            threads[i].start();
        }
        countDownLaunch.await();
        //测试再次await，系统会抛出异常
        Throwable exception = assertThrows(IllegalStateException.class, ()->countDownLaunch.await());
        Throwable exception2 = assertThrows(IllegalStateException.class, ()->countDownLaunch.await());
        if (exception==null){
            log.info("exception2.getMessage():{}",exception2.getMessage());
            assertTrue(testHelpAssetrOr(exception2.getMessage()));
        }else{
            log.info("exception.getMessage():{}",exception.getMessage());
            assertTrue(testHelpAssetrOr(exception.getMessage()));
        }

    }

    private boolean testHelpAssetrOr(String msg){
        if("state ==0.but the await method can only be execute once.".equals(msg)||("state <0 .the await method can only be execute once.".equals(msg))){
            return true;
        }
        return false;
    }
}