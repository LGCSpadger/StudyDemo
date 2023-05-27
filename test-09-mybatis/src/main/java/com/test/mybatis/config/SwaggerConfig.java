package com.test.mybatis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        // 设置swagger-ui.html页面上的一些元素信息。
        .apiInfo(metaData())
        .select()
        // 扫描的包路径
        .apis(RequestHandlerSelectors.basePackage("com.test.mybatis.controller"))
        // 定义要生成文档的Api的url路径规则
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder()
        // 标题
        .title("SpringBoot集成Swagger2")
        // 描述
        .description("xxx项目接口文档")
        // 文档版本
        .version("1.0.0")
        .build();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("doc.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}

