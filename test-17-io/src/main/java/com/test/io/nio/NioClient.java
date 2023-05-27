package com.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @Author：刘广超
 * @Date：2023-05-15
 * @Description：NIO客户端
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        // 创建SocketChannel并指定ip地址和端口号
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        System.out.println("==============NIO客户端启动================");
        // 非阻塞模式
        socketChannel.configureBlocking(false);
        String hello="你好，靓仔！";
        ByteBuffer buffer = ByteBuffer.wrap(hello.getBytes());
        // 向通道中写入数据
        socketChannel.write(buffer);
        System.out.println("发送消息：" + hello);
        buffer.clear();
        // 将channel注册到Selector并监听READ事件
        socketChannel.register(Selector.open(), SelectionKey.OP_READ, buffer);
        while (true) {
            // 读取服务端数据
            if (socketChannel.read(buffer) > 0) {
                buffer.flip();
                String msg = new String(buffer.array(), 0, buffer.limit());
                System.out.println("收到服务端消息：" + msg);
                break;
            }
        }
        // 关闭输入流
        socketChannel.shutdownInput();
        // 关闭SocketChannel连接
        socketChannel.close();
    }
}