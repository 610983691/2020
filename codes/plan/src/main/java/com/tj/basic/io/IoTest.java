package com.tj.basic.io;


import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class IoTest {


    public static void main(String[] args){
        testRead();
    }
    public static  void testRead(){

        try {
            ByteBuffer buf =ByteBuffer.allocate(10);
            FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\tongjie\\Desktop\\test.txt"));
            int bytesread=0;
            while ((bytesread =fileChannel.read(buf))!=-1){
               log.info("read bytes {} ,msg is ：{}",bytesread,new String(Arrays.copyOf(buf.array(),bytesread),"UTF-8"));
                buf.clear();
            }

        System.out.println();
        }catch (Exception e){
            log.error("err,",e);
        }



    }
}
