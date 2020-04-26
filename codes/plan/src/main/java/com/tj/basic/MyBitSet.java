package com.tj.basic;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class MyBitSet {
    public static void main(String[] args) throws IOException {
        Random r = new Random();
        Path c = Paths.get("E:\\devtools\\tongjie\\temp\\numbers.txt");
        for (int i = 0; i < 10000000; i++) {//先生成文件
            int rand = r.nextInt();
            if (rand < 0) {
                rand = -rand;
            }
            Files.write(c, (rand + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }

    }
}
