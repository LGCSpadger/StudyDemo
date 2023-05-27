package com.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author：刘广超
 * @Date：2023-05-15
 * @Description：NIO服务端
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        //创建一个选择器selector
        Selector selector= Selector.open();
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        //必须得设置成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //将channel注册到selector并设置监听事件为ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("===========NIO服务端启动============");
        while(true){
            //超时等待
            if(selector.select(1000)==0){
                System.out.println("===========NIO服务端超时等待============");
                continue;
            }
            // 有客户端请求被轮询监听到，获取返回的SelectionKey集合
            Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
            //迭代器遍历SelectionKey集合
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                // 判断是否为ACCEPT事件
                if (key.isAcceptable()){
                    // 处理接收请求事件
                    SocketChannel socketChannel=((ServerSocketChannel) key.channel()).accept();
                    //非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 注册到Selector并设置监听事件为READ
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("成功连接客户端");
                }
                //判断是否为READ事件
                if (key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();

                    try {
                        // 获取以前设置的附件对象，如果没有则新建一个
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if (buffer == null) {
                            buffer = ByteBuffer.allocate(1024);
                            key.attach(buffer);
                        }
                        // 清空缓冲区
                        buffer.clear();
                        // 将通道中的数据读到缓冲区
                        int len = socketChannel.read(buffer);
                        if (len > 0) {
                            buffer.flip();
                            String message = new String(buffer.array(), 0, len);
                            System.out.println("收到客户端消息：" + message);
                        } else if (len < 0) {
                            // 接收到-1，表示连接已关闭
                            key.cancel();
                            socketChannel.close();
                            continue;
                        }
                        // 注册写事件，下次向客户端发送消息
                        socketChannel.register(selector, SelectionKey.OP_WRITE, buffer);
                    } catch (IOException e) {
                        // 取消SelectionKey并关闭对应的SocketChannel
                        key.cancel();
                        socketChannel.close();
                    }
                }
                //判断是否为WRITE事件
                if (key.isWritable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //获取buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    String hello = "你好，坤坤！";
                    //清空buffer
                    buffer.clear();
                    //buffer中写入消息
                    buffer.put(hello.getBytes());
                    buffer.flip();
                    //向channel中写入消息
                    socketChannel.write(buffer);
                    buffer.clear();
                    System.out.println("向客户端发送消息：" + hello);
                    // 设置下次读写操作，向 Selector 进行注册
                    socketChannel.register(selector, SelectionKey.OP_READ, buffer);
                }
                // 移除本次处理的SelectionKey,防止重复处理
                iterator.remove();
            }
        }

    }
}