package com.tj.basic.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * 该用例用来测试一个阻塞式的IO，server在accept等待连接和接受输入时都会阻塞。
 * 核心逻辑就是：1.server.bind(port),服务器启动监听。
 * 2.client = server.accept(),服务器等待接收连接。
 * 3.client.getinputstream。read()。读取客户端消息。
 * 4.client.getoutputsream.write(msg).给客户端响应消息。
 * 整个2.3.4三个步骤都会阻塞。
 * 而且读写消息，都需要从内核空间拷贝数据到用户空间。
 */
@Slf4j
public class BlockIo {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(80);
        log.info("server start.",server);
        while (true){
            log.info("wait connect.");
            Socket client =server.accept();//会阻塞，直到客户端连接
            log.info("client {} connected.",client.getPort());
            InputStream clientInput = client.getInputStream();//获取客户端的输入,这里测试直接使用浏览器发送HTTP请求。
            OutputStream clientOutput = client.getOutputStream();//获取客户端的输入,这里测试直接使用浏览器发送HTTP请求。(已改为socketTest.jar，一个TCP/udp连接测试工具)
            byte[] buf = new byte[1024*10];
            int readlen =0;
            while ((readlen= clientInput.read(buf))!=-1){
                String rcv = new String(Arrays.copyOf(buf,readlen),"GBK");
                log.info("read:{}",rcv);
                String send = "hello,im server.i know you say-->:"+rcv;
                clientOutput.write(send.getBytes());//将输入原样输出,这里会发现客户端浏览器无法获取输出，因为这里的输出不是HTTP协议？应该有个TCP工具就好了，等下去找一个。
                clientOutput.flush();
                log.info("client {} write over.",client.getPort());

            }
        }

    }
}
