package com.tj.designpattern.creator.singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * 单例模式：
 * 私有的构造方法
 *指向自己实例的私有静态引用
 *以自己实例为返回值的静态的公有的方法
 */
@Slf4j
public class Singleton {


    private static LazyThreadSafeStaticInnerClass hello;


  private   class  Threada extends Thread{
        @Override
        public void run() {
            LazyPatternSingletonDoublecheck2.getInstance();
        }
    }



    public  static void  main(String[] arg) throws Exception{
        Singleton singleton = new Singleton();
//        Threada t1 =singleton.new Threada();
//        Threada t2 =singleton.new Threada();
//        t1.start();
//        t2.start();
        try {
//            HungryPatternSingleton.print();//调用类的静态方法时，类会被初始化，这个时候，静态域会被调用
//            LazyThreadSafeStaticInnerClass.print();//调用类的亭台方法，类被加载。但是由于是内部类实现的，内部类这个时候没被加载，所以没有触发初始化构造方法。
            System.out.println(HungryPatternSingleton.field1);
            System.out.println(HungryPatternSingleton.field2);
            System.out.println(SingletonEnum.instance);
//            HungryPatternSingleton.getInstance();
//            LazyThreadSafeStaticInnerClass.getInstance();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
