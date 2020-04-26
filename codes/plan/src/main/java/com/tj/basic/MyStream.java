package com.tj.basic;

import java.util.Optional;
import java.util.stream.Stream;

public class MyStream {
    public static void main(String[] args) {

//        Stream<String> st = Stream.of("one", "two", "three", "four");
//        st.filter(e -> e.length() > 3).forEach(String::toUpperCase);
//        st.forEach(System.out::println);
        String one = "one";
        String two = "two";
        String three = "three";
        String four = "four";
        String two_2 = new String("two");
        Optional<String> rst = Stream.of(one, two, three, four, two_2)
                .findAny();
        System.out.println(rst.get());


    }
}

