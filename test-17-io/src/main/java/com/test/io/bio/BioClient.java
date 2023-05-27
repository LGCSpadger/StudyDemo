package com.test.io.bio;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class BioClient {

    public static void main(String[] args) throws IOException {
        List<String> names= Arrays.asList("帅哥","靓仔","坤坤");
        //通过循环创建多个多个client
        for (String name:names){
            //创建socket并根据IP地址与端口连接服务端
            Socket socket=new Socket("127.0.0.1",8888);
            System.out.println("===========BIO客户端启动================");
            //从socket中获取字节输出流
            OutputStream outputStream=socket.getOutputStream();
            //通过输出流向服务端传递信息
            String hello="你好，"+name+"!";
            outputStream.write(hello.getBytes());
            //清空流，关闭socket输出
            outputStream.flush();
            socket.shutdownOutput();

            //从socket中获取字节输入流
            InputStream inputStream=socket.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            //读取服务端消息
            String msg;
            while((msg=bufferedReader.readLine())!=null){
                System.out.println("收到服务端消息："+msg);
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        }
    }
}