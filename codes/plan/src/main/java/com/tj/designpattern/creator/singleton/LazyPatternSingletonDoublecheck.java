package com.tj.designpattern.creator.singleton;


/**
 * 线程安全的单例：采用双重校验
 */
public  class  LazyPatternSingletonDoublecheck{
    static  volatile LazyPatternSingletonDoublecheck  instance ;//静态域
    private LazyPatternSingletonDoublecheck(){

    }
    //doublecheck 方式的
    //因为是静态方法，所以需要在类上加synchronized关键字
    public static LazyPatternSingletonDoublecheck getInstance(){
        if(instance==null){
            synchronized (LazyPatternSingletonDoublecheck.class){
                if(instance==null){
                    instance=new LazyPatternSingletonDoublecheck();
                }
            }
        }
        return instance;
    }
}
