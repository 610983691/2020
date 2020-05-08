package com.tj.basic.mylock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author tongjie
 * @version 1.0.0
 * @ClassName MyCountDownLaunch.java
 * @Description 自己实现一个倒数计数器。首先计数器需要有countdown,await方法。await会阻塞，直到countdown减数等于0.
 * @createTime 2020年05月07日 20:30:00
 */
public class MyCountDownLaunch {
    private final Sync sync;
    public  MyCountDownLaunch(int count){
        sync= new Sync(count);
    }

    public void countDown(){
        sync.releaseShared(1);
    }

    public void await(){
        sync.acquireShared(1);
    }
    /**
     * @description: TODO
     * @author: tongjie
     * @date: 2020/5/7
     */
    private  class Sync extends AbstractQueuedSynchronizer {

        private Sync(int count){
            setState(count);
        }
        @Override
        protected boolean isHeldExclusively() {
            return false;
        }

        /**
         * @param: null
         * @description: state>0,表明还有countdown未完成。state==0,countdown个数刚好等于构造方法传参。state<0，表明已经成功执行过一次await()方法。
         * @return:
         * @author: tongjie
         * @date: 2020/5/7
         */

        @Override
        protected int tryAcquireShared(int arg) {
            final int c =getState();
            if(c==0){
                if(compareAndSetState(c,c-arg)){//true，表明设置成功
                    return 0;
                }
                throw new IllegalStateException("state ==0.but the await method can only be execute once.");
            }else if(c>0){
                return -1;
            }else{//小于0，其实这样做这个“锁”就是不可重入锁，因为不管是否是当前线程，都会导致state<0
                throw new IllegalStateException("state <0 .the await method can only be execute once.");
            }

        }

        /**
         * @param: null
         * @description: 释放锁，countdown一次，state状态减1.
         * @return:
         * @author: tongjie
         * @date: 2020/5/7
         */

        @Override
        protected boolean tryReleaseShared(int arg) {
            final int current = getState();
            final int wantSet = current-arg;
            if(wantSet<0){
                throw new IllegalStateException("state must be great than 0.");
            }
            if(current<=0){//当前已经是0 了，还想释放，那就是非法的。
                throw new IllegalStateException("state must be great than 0.");
            }else{
                for(;;){//这里必须要循环，否则的话多线程竞争导致compareandset失败，导致状态未设置正确引起未知的bug.
                    if(compareAndSetState(current,wantSet)){//一直循环，直到操作成功
                        return true;
                    }
                }
            }
        }
    }
}
