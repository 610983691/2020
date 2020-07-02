package com.tj.basic.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

@Slf4j
public class NoneBlockIo {

    /***
     * 简单实现一个非阻塞的服务。
     * 核心逻辑就是使用ServerSocketChannel，并且设置blocking为false.
     * 1.serverSocketChannel.oper();
     * serverchannel.bind(addr(port));//绑定到本地端口
     *
     * 该模式的关键就在于，accept在有连接到来的时候就返回连接。否则就返回空，整个accept和socket的区别就在于它不会阻塞。
     * 同样read(),write()也是类似的逻辑。
     * 但这也有缺陷，下面看看多路复用模型。
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws  Exception {
        LinkedList<SocketChannel> clients = new LinkedList<SocketChannel>();
        ServerSocketChannel channel =ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(80));//监听本地端口，类似 serversocket(80)
        channel.configureBlocking(false);//非阻塞
        while(true){
            Thread.sleep(1000);
            SocketChannel client = channel.accept();//接收连接，如果有连接到来，就返回非空，否则返回空。可以看到虽然是一个单线程，但是整个程序不会阻塞
            if(client == null){
                log.info("client is null.wait next time");
            }else{
                client.configureBlocking(false);//设置为非阻塞模式
                clients.add(client);//
            }
            ByteBuffer buffer = ByteBuffer.allocate(256);
            //看看下面代码会阻塞吗？
            //每个客户端的读和写都不会阻塞程序的运行。
            clients.forEach(cli ->{
                try {
                    log.info("server will read client {}:",cli.socket().getPort());
                   int readlen = cli.read(buffer);//看看会不会阻塞,当client的configblock为true时，仍然会阻塞
                    if(readlen >0){
                        buffer.flip();
                        byte[] reads = new byte[buffer.limit()];
                        buffer.get(reads);
                        String rcv = new String(reads);
                        log.info("server read client {} msg:{}",cli.socket().getPort(),rcv);
                        buffer.clear();
                        String send = "im server rcv client "+cli.socket().getPort()+",msg:"+rcv;
                        buffer.put(send.getBytes());
                        buffer.flip();
                        cli.write(buffer);
                    }
                } catch (IOException e) {
                    log.info("err is :",e);
                }
            });
        }

    }
}
