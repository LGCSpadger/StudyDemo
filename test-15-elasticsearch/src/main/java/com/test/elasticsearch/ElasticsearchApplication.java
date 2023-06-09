package com.test.elasticsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.test.elasticsearch.mapper")
@SpringBootApplication
public class ElasticsearchApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElasticsearchApplication.class,args);
  }

}
