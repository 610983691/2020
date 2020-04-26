package com.tj.basic;

/***
 * 用于描述java值传递,传递的是这个TypeA的引用，这个是个地址。方法外的va会永远指向这个地址是不会变的。
 * 传递的参数va,只是另外创建一个引用指向这个地址，它其实是真实的va的一个拷贝而已，所以是值传递。
 * 同时，通过a.name修改name的值，是因为修改的是这块地址上的变量的“引用”，它和当前对象va是值传递并不冲突。
 */
public class JavaZcd {

    public static void main(String[] args) throws InterruptedException {
//        TypeA va = new TypeA("vala");
//        TypeA vb = new TypeA("valb");
//        synchronized (va){
//            System.out.println(va.hashCode());
//        }
//
//        System.out.println(va);
//        System.out.println(vb.hashCode());
//        System.out.println(vb);
//        TypeA vc = trySwapAType(va);
//        System.out.println("vc is" + vc);
//        System.out.println(va);


    }

    static class ThreadInterruptSelf extends Thread{
        public void run(){
            System.out.println("==");
            while (true){
                Thread.currentThread().interrupt();
            }

        }
    }

    static TypeA trySwapAType(TypeA a) {
        TypeA temp = new TypeA("temp");
        a = temp;
        System.out.println("a in method." + a);
        return temp;
    }

    static class TypeA {
        String name;

        TypeA(String s) {
            name = s;
        }


    }
}
