package com.test.io.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) throws IOException {
        //定义一个ServerSocket服务端对象，并为其绑定端口号
        ServerSocket server = new ServerSocket(8888);
        System.out.println("===========BIO服务端启动================");
        //对BIO来讲，每个Socket都需要一个Thread
        while (true) {
            //监听客户端Socket连接
            Socket socket = server.accept();
            new BioServerThread(socket).start();
        }

    }

    /**
     * BIO Server线程
     */
    static class BioServerThread extends Thread{
        //socket连接
        private Socket socket;
        public BioServerThread(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            try {
                //从socket中获取输入流
                InputStream inputStream=socket.getInputStream();
                //转换为
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String msg;
                //从Buffer中读取信息，如果读取到信息则输出
                while((msg=bufferedReader.readLine())!=null){
                    System.out.println("收到客户端消息："+msg);
                }

                //从socket中获取输出流
                OutputStream outputStream=socket.getOutputStream();
                PrintStream printStream=new PrintStream(outputStream);
                //通过输出流对象向客户端传递信息
                printStream.println("你好，吊毛！");
                //清空输出流
                printStream.flush();
                //关闭socket
                socket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}