package com.test.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 连接mongodb数据库时需要在 @SpringBootApplication 注解中添加 exclude = DataSourceAutoConfiguration.class
 * @SpringBootApplication 注解中 exclude = DataSourceAutoConfiguration.class 的作用是 排除自动注入数据源的配置（取消数据库配置）
 * 原因：DataSourceAutoConfiguration.class会自动查找application.yml或者properties文件里的spring.datasource.*相关属性并自动配置单数据源。
 * 如果想在项目中使用多数据源或者使用mongodb这种非关系型数据库的话就需要排除它，手动指定数据源。
 */
@EnableSwagger2
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MongoDBAppication {

  public static void main(String[] args) {
    SpringApplication.run(MongoDBAppication.class,args);
  }

}
