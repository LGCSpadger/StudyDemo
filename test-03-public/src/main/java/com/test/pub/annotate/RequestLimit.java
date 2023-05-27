package com.test.pub.annotate;

import java.lang.annotation.*;

/**
 * 自定义注解 -- 接口限流防刷
 *
 * @author 刘广超
 * @date 2023-04-19 09:09
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

  /**
   * 在  seconds 秒内 , 最大只能请求 maxCount 次
   * @return
   */
  int seconds() default 1;
  /**
   * 最大请求数量
   * @return
   */
  int maxCount() default 1;

}