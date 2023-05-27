package com.test.ftp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.test.ftp.mapper")
@SpringBootApplication
public class FtpApplication {

  public static void main(String[] args) {
    SpringApplication.run(FtpApplication.class,args);
  }

}
