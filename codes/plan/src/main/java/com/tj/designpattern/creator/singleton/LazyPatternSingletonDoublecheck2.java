package com.tj.designpattern.creator.singleton;

/**
 * 线程安全的单例：采用双重校验
 */
public  class  LazyPatternSingletonDoublecheck2{
    private static   LazyPatternSingletonDoublecheck2  instance ;//不带volatile关键字的静态域
    private LazyPatternSingletonDoublecheck2(){

    }
    //doublecheck 方式的
    //因为是静态方法，所以需要在类上加synchronized关键字
    public static LazyPatternSingletonDoublecheck2 getInstance(){
        if(instance==null){
            synchronized (LazyPatternSingletonDoublecheck2.class){
                if(instance==null){
                    instance=new LazyPatternSingletonDoublecheck2();
                }
            }
        }
        return instance;
    }
}