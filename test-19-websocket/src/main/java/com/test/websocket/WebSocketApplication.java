package com.test.websocket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.test.websocket.mapper")
@EnableSwagger2
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})//排除SpringBoot启动的时候自动注入数据源的功能
public class WebSocketApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketApplication.class,args);
  }

}
