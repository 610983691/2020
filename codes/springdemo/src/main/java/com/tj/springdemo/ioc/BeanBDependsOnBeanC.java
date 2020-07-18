package com.tj.springdemo.ioc;

public class BeanBDependsOnBeanC {

    private BeanC beanc;
    private String name;

   public  BeanBDependsOnBeanC(BeanC bean,String n){
       beanc=bean;
       name =n;
       System.out.println("im bean b init :"+ toString() +"-->i have property beanc is : "+beanc.toString());
    }


}
