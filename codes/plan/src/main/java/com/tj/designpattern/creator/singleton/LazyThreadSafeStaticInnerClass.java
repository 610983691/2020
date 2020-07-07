package com.tj.designpattern.creator.singleton;

/***
 * 类加载时机：
 * 1.类被实例化。
 * 2.调用类的静态方法或静态变量。
 * 3.通过反射获取类（class.forname("com.class")）
 * 4.初始化一个类时，父类未被初始化则会先初始化父类
 * 5.JVM虚拟机启动时，先初始化MAIN方法所在的类。
 */
public  class LazyThreadSafeStaticInnerClass {

    //静态内部类不会在外部类加载的时候被加载

    private static class Holder{
        private static LazyThreadSafeStaticInnerClass INSTANCE = new LazyThreadSafeStaticInnerClass();
    }

    private LazyThreadSafeStaticInnerClass(){
        System.out.println("LazyThreadSafeStaticInnerClass inited");
    }

    public static LazyThreadSafeStaticInnerClass getInstance(){
        //调用类的域变量时，触发内部类的加载。类加载线程安全，因此只会调用new 一次。
        return Holder.INSTANCE;////因为在JVM进行类加载的时候他会保证数据是同步的，所以该方式实现的单例也是线程安全的。
    }
    public static void print(){
        System.out.println("LazyThreadSafeStaticInnerClass print anything");
    }


}