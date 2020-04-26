package com.tj.basic.mylock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author tongjie
 * @version 1.0.0
 * @ClassName MyReentrantLockV2.java
 * @Description 第一版实现失败了，发现其实是由于lock的时候是调用的tryAccquire。因此这里在V1版本基础上实现V2版本。
 * @createTime 2020年04月26日 23:06:00
 */
public class MyReentrantLockV2 extends MyReentrantLock{

    public MyReentrantLockV2(){
        sync = new Sync();
    }

    private final Sync sync;

    /**
     * @description: 不实现Lock接口，自己对外发布一个lock接口
     * @return:
     * @author: tongjie
     * @date: 2020/4/26
     */
    public void lock(){
        sync.lock();
    }

    /**
     * @description: 简单粗暴的实现
     * @return:
     * @author: tongjie
     * @date: 2020/4/26
     */
    public void unLock(){
        sync.release(1);
    }


    public int getWaitingThreaCounts(){
        return sync.getQueuedThreads().size();
    }

    /***
     * 使用AQS提供的模板来实现一个锁。不共享，独占。一般子类的同步器只实现共享或者只实现独占，而不是一个Sync同时实现共享和独占。
     * 核心：state值的控制来控制锁。
     * 本例也参考JDK的reentrantLock
     * 我自己的设计思路如下：
     * 1.state 初始0,表示无锁。
     * 2.state>1表示加锁。
     * 3.子类实现tryrelease和tryAccquire方法
     */
    private class Sync extends AbstractQueuedSynchronizer {


       final void lock(){
            if (sync.compareAndSetState(0,1)){//x先尝试加锁
                sync.setExclusiveOwnerThread(Thread.currentThread());
            }else{
                acquire(1);
            }
        }

        @Override
        /**
         * @Description:该锁的实现是独占的
         * @return: true
         * @author: tongjie
         * @date: 2020/4/26
         */
        protected boolean isHeldExclusively() {
            return true;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if(arg<0){
                throw new IllegalArgumentException("arg<0");
            }
            final int state = getState();
            if (state==0){
                compareAndSetState(state,state+arg);
                return true;
            }else if(state>0&&Thread.currentThread()==getExclusiveOwnerThread()){
                compareAndSetState(state,state+arg);//似乎可以使用setstate?因为是同一个线程。
                return true;
            }else{
                return false;
            }
        }

        /**
         * @description: 自己实现的锁释放
         * @return:
         * @author: tongjie
         * @date: 2020/4/26
         */

        @Override
        protected boolean tryRelease(int arg) {
            if(arg<0){
                throw new IllegalArgumentException("arg<0");
            }
            final int curstate = getState();
            if (curstate ==0){
                throw new IllegalMonitorStateException("current state is 0,but wanto release more...");
            }
            if (Thread.currentThread()!=getExclusiveOwnerThread()){
                //未持有锁的线程尝试释放，不允许
                throw new IllegalMonitorStateException("current thread dont have lock,but try release...");
            }
            if (curstate-arg<0){
                throw new IllegalArgumentException("curstate-arg<0 ,arg is too big.");
            }
            setState(curstate-arg);
            return true;
        }
    }
}
