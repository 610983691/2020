package com.tj.basic.mylock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author tongjie
 * @version 1.0.0
 * @ClassName MySemaphore.java
 * @Description 本来想写个非重入锁，想了想，semaphore如果设置信号量为1就是一个不可重入的锁了吧。至少我这个semaphore要这样实现。
 * <br>查看semaphore的源码，发现我的samephore实现没有加自旋。看单元测试结果先</br>
 * @createTime 2020年04月29日 22:19:00
 */
public class MySemaphore {
    private final Sync sync;
    private final int threshold;//信号量的阈值
    public MySemaphore(int count){
        if(count < 1){
            throw new IllegalArgumentException("threshold must be greater than 0.");
        }
        sync = new Sync();
        threshold= count;
    }

    /**
     * @param: null
     * @description: 获取n个信号
     * @return:
     * @author: tongjie
     * @date: 2020/4/29
     */

    public void acquire(int n){
        sync.acquireShared(n);
    }
    /**
     * @param: null
     * @description: 释放n个信号
     * @return:
     * @author: tongjie
     * @date: 2020/4/29
     */
    public void release(int n){
        sync.releaseShared(n);
    }

    private class Sync extends AbstractQueuedSynchronizer{


        //实现信号量机制，所以不是独占的。
        @Override
        protected boolean isHeldExclusively() {
            return false;
        }

        /**
         * @param: arg 要获取的信号量个数
         * @description: 获取信号量
         * @return: -1,获取失败。0：当前获取成功，后续的获取会失败。1，当前获取成功，后续的获取也可能成功。
         * @author: tongjie
         * @date: 2020/4/29
         */

        @Override
        protected int tryAcquireShared(int arg) {
            if (arg<1){
                throw new IllegalMonitorStateException("semaphore must be greater than 0.");
            }
            final int c =getState();//当前值
            final int tobeSetVal = c+arg;//将要设置的值
            if(tobeSetVal>threshold){
                return -1;
            }
            if(compareAndSetState(c,tobeSetVal)){
                if(tobeSetVal<threshold){//小于阈值，返回1
                    return 1;
                }else{
                    return 0;//等于阈值，表明后续的申请已经没有可用资源了。返回0
                }
            }
            return -1;
        }

        /**
         * @param: 要释放的信号量个数
         * @description: 释放信号量
         * @return:
         * @author: tongjie
         * @date: 2020/4/29
         */
        @Override
        protected boolean tryReleaseShared(int arg) {
            if (arg<1){
                throw new IllegalMonitorStateException("semaphore must be greater than 0.");
            }
            final int c =getState();//当前值
            if(c<arg){
                throw new IllegalMonitorStateException("semaphore is overflow!");//要释放的数量不能大于当前状态值
            }
            final int tobeSetVal = c-arg;
            if(compareAndSetState(c,tobeSetVal)){
                return true;
            }
            return false;
        }
    }
}
