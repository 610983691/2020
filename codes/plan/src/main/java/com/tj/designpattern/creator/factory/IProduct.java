package com.tj.designpattern.creator.factory;

/***
 * 产品接口
 */
public interface IProduct {
   default void sayHello(){
       System.out.println("hello ,im:"+this.toString());
   }
}
