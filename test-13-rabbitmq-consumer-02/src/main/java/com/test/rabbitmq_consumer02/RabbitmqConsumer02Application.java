package com.test.rabbitmq_consumer02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 备注：
 * 1、在 rabbitmq 中，消息的消费者在服务启动的时候就会连接上 rabbitmq 服务器，开始监听消息队列
 */
@SpringBootApplication
public class RabbitmqConsumer02Application {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumer02Application.class,args);
    }

}
