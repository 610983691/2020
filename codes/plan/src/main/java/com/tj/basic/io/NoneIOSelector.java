package com.tj.basic.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/***
 * 使用一个线程来管理多个连接，每个连接有事件发生时，线程会逐个处理这些事件。
 *
 */
@Slf4j
public class NoneIOSelector {

    private ServerSocketChannel serverchannel;
    private   Selector selector;
    private void initServer(int port) throws IOException {
        serverchannel = ServerSocketChannel.open();
        serverchannel.bind(new InetSocketAddress(port));
        serverchannel.configureBlocking(false);
         selector= Selector.open();
        SelectionKey key= serverchannel.register(selector, SelectionKey.OP_ACCEPT);//OP_ACCEPT就绪条件：当收到一个客户端的连接请求时，该操作就绪。这是ServerSocketChannel上唯一有效的操作。
        log.info("服务器端，有连接到达的事件key:{}",key);
    }

    private void start() throws IOException {
            while(selector.select()>0){//阻塞直到有事件到来
                Set<SelectionKey> opKeys= selector.selectedKeys();
                Iterator<SelectionKey> it =opKeys.iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    log.info("has key:{}",key);
                    if(key.isAcceptable()){
                        handleAccept(key);
                    }else if(key.isReadable()){
                        handleRead(key);
                    }
                    it.remove();
                }
            }


    }

    /***
     * 处理连接事件
     * @param key
     * @throws IOException
     */
    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();//得到客户端
        SocketChannel cli = channel.accept();
        log.info("cli {} connected.",cli.socket().getPort());
        cli.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024*1024*10);//故意放大点，看看会不会被回收，每个buffer的大小是10MB
        SelectionKey key2 =cli.register(selector,SelectionKey.OP_READ,buffer);
        log.info("注册客户端读事件key:{}",key2);
    }

    /***
     * 处理读事件
     * @param key
     * @throws IOException
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel cli = (SocketChannel) key.channel();
        ByteBuffer buffer =(ByteBuffer) key.attachment();
        int readlen ;
        while (true){
            readlen=cli.read(buffer);
            if(readlen>0){
                buffer.flip();
                byte[] readbytes = new byte[buffer.limit()];
                buffer.get(readbytes);
                buffer.clear();
                String rcvmsg= new String(readbytes,"UTF-8");
                log.info("server read cli {},msg:{}",cli.socket().getPort(),rcvmsg);
                String send = "server rcv cli +"+cli.socket().getPort()+"-->:"+rcvmsg;
                buffer.put(send.getBytes("UTF-8"));
                buffer.flip();
                cli.write(buffer);//回写回客户端
                buffer.clear();
            }else if(readlen==0){
                break;
            }else{
                log.info("client {} closed.",cli.socket().getPort());
                if(cli.isOpen()){
                    cli.close();//关闭连接
                }
                break;
            }
        }
    }

    /***
     * 使用多路复用器模型
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws  Exception{
        NoneIOSelector sel = new NoneIOSelector();
        sel.initServer(80);
        sel.start();
    }
}
