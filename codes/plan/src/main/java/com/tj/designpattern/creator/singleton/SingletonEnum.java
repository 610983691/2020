package com.tj.designpattern.creator.singleton;

public enum  SingletonEnum {

    instance("123");
    private String s;
     SingletonEnum(String s1){
        s=s1;
    }


    public void pring(){
        System.out.println(instance);
    }

    @Override
    public String toString() {
       return this.s;
    }
}
