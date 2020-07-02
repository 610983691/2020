package com.tj.basic.io;

import com.sun.security.ntlm.Client;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BlockIoMulThread {
    /***
     * 也是一个阻塞式 的IO实现。但该实现可以同时处理多个连接。
     * 核心逻辑：
     * 1.server.bind(port) ,服务端监听端口。
     * 2.erver.accept(),服务器等待接收连接，每个连接会返回一个对应的client socket.
     * 3.将client socket 丢给一个线程去处理。
     * 4.线程接受客户端消息，并返回固定格式的响应消息。
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8888);
        log.info("server start on port:{}",8888);
        AtomicInteger ati = new AtomicInteger();
        while (true){
            log.info("server wait client connect");
            Worker worker = new Worker(server.accept());//
            worker.start();
            log.info("server start a thread to procees");
            log.info("worker count:{}",ati.incrementAndGet());//不一定是当前存活的数量
        }
    }

    /***
     * 每个连接进来都开辟一个线程去处理
     */
    private static class Worker extends Thread{
        Socket client ;
        Worker(Socket Client){
            this.client =Client;
        }
        public void run(){
            try {
                log.info("thread start process client {}:",client.getPort());
                BufferedReader reader = new BufferedReader(new InputStreamReader( client.getInputStream()));
                OutputStreamWriter writer =  new OutputStreamWriter(client.getOutputStream());
                byte[] buf = new byte[256];//1024字节缓冲，我们这里就定义客户端不能输入超过256字节
                while (client.isConnected()){//read,可能永远不会退出
                    String rcvmsg = reader.readLine();//无法处理连接断开退出事件
                    log.info("server thead {} rcv client {} msg is:{}",this.getName(),client.getPort(),rcvmsg);
                    String sendMsg = "im server.i rcv client "+client.getPort()+" input-->:"+rcvmsg;
                    writer.write(sendMsg);
                    writer.flush();
                }
                log.info("thread {} process client {} over:",this.getName(),client.getPort());
            }catch (Exception e){
                log.error("err .",e);
            }
        }
    }
}
