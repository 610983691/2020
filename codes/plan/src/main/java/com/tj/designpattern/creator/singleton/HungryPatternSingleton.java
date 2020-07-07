package com.tj.designpattern.creator.singleton;


/***
 * 类加载时机：
 * 1.类被实例化。
 * 2.调用类的静态方法或静态变量。(用final修饰的常量被使用时，无法触发类加载动作。可以通过调用field1，field2字段测试)
 * 3.通过反射获取类（class.forname("com.class")）
 * 4.初始化一个类时，父类未被初始化则会先初始化父类
 * 5.JVM虚拟机启动时，先初始化MAIN方法所在的类。
 */
public class HungryPatternSingleton {
    /***
     * 饿汉模式单例：

     * 之所有是饿汉：因为在类加载的时候就已经被实例化了。而不是真正在调用的时候在实例化
     */
        private static  HungryPatternSingleton instance = new HungryPatternSingleton();

        private HungryPatternSingleton(){
            System.out.println("HungryPatternSingleton init.");
        }
        public static HungryPatternSingleton getInstance(){
            return instance;
        }
    public static void print(){
       System.out.println("HungryPatternSingleton print anything");
    }
    public  static String field1;

    public static final  String field2="field2";
}
