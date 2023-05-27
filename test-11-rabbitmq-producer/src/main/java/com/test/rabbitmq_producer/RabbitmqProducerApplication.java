package com.test.rabbitmq_producer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 备注：
 * 1、在 rabbitmq 中，消息的生产者如果是一个单独的程序，该程序在启动时并不会去连接 rabbitmq，而是等到发消息的时候才会连接上 rabbitmq 服务器
 */
@MapperScan("com.test.rabbitmq_producer.mapper")
@EnableSwagger2
@SpringBootApplication
public class RabbitmqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqProducerApplication.class,args);
    }

}
