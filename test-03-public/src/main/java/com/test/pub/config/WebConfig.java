package com.test.pub.config;

import com.test.pub.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 自定义拦截器配置类
 * 1、拦截所有请求接口，进行接口限流防刷处理
 * 2、配置 Swagger 信息
 *
 * @author 刘广超
 * @date 2023-04-19
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private MyInterceptor interceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 对所有访问路径，都通过MyInterceptor类型的拦截器进行拦截
    registry.addInterceptor(interceptor).addPathPatterns("/**")
        .excludePathPatterns("/login", "/index.html", "/user/login", "/css/**", "/images/**", "/js/**", "/fonts/**");
    //excludePathPatterns() 放行登录页，登陆操作，静态资源
  }

  /**
   * Swagger2 配置
   * @return
   */
  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        // 设置swagger-ui.html页面上的一些元素信息。
        .apiInfo(metaData())
        .select()
        // 扫描的包路径
        .apis(RequestHandlerSelectors.basePackage("com.test.pub.controller"))
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