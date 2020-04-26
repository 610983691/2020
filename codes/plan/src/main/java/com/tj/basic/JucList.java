package com.tj.basic;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.util.HashMap;
import java.util.Map;

public class JucList {
    final static Object obj = new Object();
    final static Boolean b = Boolean.valueOf(false);

    public static void main(String[] args) {
        System.out.println(ObjectSizeCalculator.getObjectSize(obj));
        System.out.println(ObjectSizeCalculator.getObjectSize(b));
        Map<String, Boolean> MAP = new HashMap<String, Boolean>();
        MAP.put("", false);

    }

}
