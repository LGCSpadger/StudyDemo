package com.test.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.test.redis.mapper")
@SpringBootApplication
public class RedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedisApplication.class,args);
  }

}
