package com.tj.basic.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 多线程版本的多路复用器实现。设计思路是：
 * 1.一个主线程，用于启动服务器。
 * 2.一个worker线程，用于收发消息。
 * 3.一个client监听线程，用于处理到来的连接。
 */
@Slf4j
public class NIOSelectorMulThread {

    public static void main(String[] args) throws IOException {
         Selector selector = Selector.open();
        LinkedBlockingQueue<SelectionKey> readerQueue = new LinkedBlockingQueue<>(10);
        LinkedBlockingQueue<SocketChannel> acceptQueue = new LinkedBlockingQueue<>(10);
        Thread server = new Server(selector,readerQueue,acceptQueue);


        Thread reader = new Reader(readerQueue);
        Thread acceptor = new ConnectProcessor(acceptQueue,selector);
        server.start();
        reader.start();
        acceptor.start();
        log.info("主线程退出...");
    }

    private static class Server extends Thread{
        private  Selector selector;
        private AtomicInteger acceptCunt = new AtomicInteger(0);
        private AtomicInteger readerCount = new AtomicInteger(0);
        private LinkedBlockingQueue<SelectionKey> readerQueue;
        private LinkedBlockingQueue<SocketChannel>  acceptQueue;
        Server(Selector sel,LinkedBlockingQueue<SelectionKey> reader,LinkedBlockingQueue<SocketChannel> accpt){
            this.selector =sel;
            this.readerQueue =reader;
            this.acceptQueue =accpt;
        }
        @Override
        public void run() {
            init();
            log.info("server inited...");
            while (true){
                try {
                    while (selector.select(0)>0){
                        Set<SelectionKey> set = selector.selectedKeys();
                        Iterator<SelectionKey> it = set.iterator();
                        log.info("set size :{}",set.size());
                        while(it.hasNext()){
                            SelectionKey key  = it.next();
                            it.remove();
                            if(key.isReadable()){
                                log.info("reader key:{},count:{}",key,readerCount.addAndGet(1));
//                                readerQueue.put(key);//BUG ,这里必须把key里边的数据消费掉，否则一直会触发可读事件。采用别的线程来消费时，就会产生BUG
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
                                        buffer.clear();
                                        break;
                                    }else{
                                        log.info("client {} closed.",cli.socket().getPort());
                                        buffer.clear();
                                        if(cli.isOpen()){
                                            cli.close();//关闭连接
                                        }
                                        break;
                                    }
                                }
                            }else if(key.isAcceptable()){
                                log.info("accept key:{},count：{}",key, acceptCunt.addAndGet(1));
                                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                SocketChannel client = server.accept();//必须先accept，否则也会一直触发可连接事件。
                                acceptQueue.put(client);
                            }

                        }
                    }
                } catch (Exception e) {
                    log.error("server err :",e);
                }
            }
        }
        private void init()  {
            try {
                ServerSocketChannel server = ServerSocketChannel.open();
                server.bind(new InetSocketAddress(80));
                server.configureBlocking(false);
                server.register(selector,SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                log.error("server init err.",e);
                throw new IllegalStateException("server init err.",e);
            }
        }
    }

    /***
     * 处理客户端读事件
     */
    private static class Reader extends  Thread{
        private LinkedBlockingQueue<SelectionKey> readerClients;//需要读写的客户端
        Reader(LinkedBlockingQueue<SelectionKey>  c){
            readerClients =c;
        }
        @Override
        public void run() {
            while (true){
                try {
                    SelectionKey key = readerClients.take();
                    log.info("here:{}",key);
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
                            buffer.clear();
                            break;
                        }else{
                            log.info("client {} closed.",cli.socket().getPort());
                            buffer.clear();
                            if(cli.isOpen()){
                                cli.close();//关闭连接
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("reader exception:",e);
                }
            }
        }

    }

    /***
     * 处理连接
     */
    private static class ConnectProcessor extends  Thread{
        private LinkedBlockingQueue<SocketChannel> clients;//需要接收连接的客户端
        private  Selector selector;
        ConnectProcessor(LinkedBlockingQueue<SocketChannel> c, Selector sel){
            clients =c;
            selector= sel;
        }

        public void run(){
            while (true){
                try {
                    SocketChannel client =clients.take();
                    client.configureBlocking(false);
                    log.info("客户端{}连接：",client.socket().getPort());
                    client.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));//把客户端绑定到一个selector上去，监听客户端的可读事件
                } catch (Exception e) {
                    log.error("connect exception:",e);
                }
            }
        }
    }
}
