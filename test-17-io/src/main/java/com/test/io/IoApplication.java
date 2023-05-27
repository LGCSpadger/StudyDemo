package com.test.io;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.test.io.mapper")
@SpringBootApplication
public class IoApplication {

  public static void main(String[] args) {
    SpringApplication.run(IoApplication.class,args);
  }

}
