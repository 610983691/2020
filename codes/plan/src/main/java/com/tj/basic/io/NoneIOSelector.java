package com.tj.basic.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NoneIOSelector {

    private ServerSocketChannel serverchannel;
    private   Selector selector;
    private void initServer(int port) throws IOException {
        serverchannel = ServerSocketChannel.open();
        serverchannel.bind(new InetSocketAddress(port));
        serverchannel.configureBlocking(false);
        Selector selector= Selector.open();
        serverchannel.register(selector, SelectionKey.OP_ACCEPT);//OP_ACCEPT就绪条件：当收到一个客户端的连接请求时，该操作就绪。这是ServerSocketChannel上唯一有效的操作。
    }

    private void handleConnect() throws IOException {
        while(selector.select()>0){//有事件
            Set<SelectionKey> opKeys= selector.selectedKeys();
            Iterator<SelectionKey> it =opKeys.iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                it.remove();
                if(key.isAcceptable()){

                }else if(key.isReadable()){

                }
            }
        }
    }
    /***
     * 使用多路复用器模型
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws  Exception{
        ServerSocketChannel serverchannel = ServerSocketChannel.open();
        serverchannel.bind(new InetSocketAddress(8888));
        serverchannel.configureBlocking(false);
        Selector selector= Selector.open();
        serverchannel.register(selector, SelectionKey.OP_ACCEPT);
    }
}
