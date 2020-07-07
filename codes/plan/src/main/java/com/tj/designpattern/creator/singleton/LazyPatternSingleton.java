package com.tj.designpattern.creator.singleton;

/***
 * 线程安全的懒汉模式：
 * 采用synchronized方法
 */
public class LazyPatternSingleton {
    static  LazyPatternSingleton instance ;//静态域
    private LazyPatternSingleton(){

    }

    public synchronized static LazyPatternSingleton getInstance(){
        if(instance==null){
            instance=new LazyPatternSingleton();
        }
        return instance;
    }
}
