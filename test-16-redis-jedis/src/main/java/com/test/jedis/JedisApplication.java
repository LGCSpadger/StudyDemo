package com.test.jedis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})//排除SpringBoot启动的时候自动注入数据源的功能
public class JedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(JedisApplication.class,args);
  }

}
