package com.tj.basic.mylock;

/**
 * @author tongjie
 * @version 1.0.0
 * @ClassName MyCycleBarrier.java
 * @Description 自己实现的一个循环屏障。首先循环屏障的state可以循环使用。
 * 设置一个初始值，每当一个资源准备就绪，状态减一。当资源状态为0时，可以获取到锁。并把状态重置为state的初始值。
 * @createTime 2020年05月07日 22:38:00
 */
public class MyCycleBarrier {
}
