package com.test.pub.interceptor;

import com.test.pub.annotate.RequestLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * 自定义拦截器
 *
 * @author 刘广超
 * @date 2023-04-19 09:09
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

  @Resource
  RedisTemplate<String, Object> redisTemplate;

  /**
   * 访问控制器方法前执行
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("系统当前时间：{}，执行方法名称：{}，请求接口地址：{}",System.currentTimeMillis(),"preHandle",request.getRequestURL());
    //1. 判断请求是否属于方法的请求
    if (handler instanceof HandlerMethod) {
      //2. 取出当前方法的对象
      HandlerMethod handler1 = (HandlerMethod) handler;
      //3. 获取方法中的注解,看是否有该注解
      RequestLimit accessLimit = handler1.getMethodAnnotation(RequestLimit.class);
      //3.1 : 不包含此注解,则不进行操作
      if (accessLimit != null) {
        //3.2 ： 判断请求是否受限制
        if (isLimit(request, accessLimit)) {
          render(response, "{\"code\":\"30001\",\"message\":\"接口请求过快！\"}");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 访问控制器方法后执行
   */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    log.info("系统当前时间：{}，执行方法名称：{}，请求接口地址：{}",System.currentTimeMillis(),"postHandle",request.getRequestURL());
  }

  /**
   * postHandle方法执行完成后执行，一般用于释放资源
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    log.info("系统当前时间：{}，执行方法名称：{}，请求接口地址：{}",System.currentTimeMillis(),"afterCompletion",request.getRequestURL());
  }

  //判断请求是否受限
  public boolean isLimit(HttpServletRequest request, RequestLimit accessLimit) {
    // 受限的redis 缓存key ,因为这里用浏览器做测试，我就用sessionid 来做唯一key,如果是app ,可以使用 用户ID 之类的唯一标识。
    String limitKey = request.getServletPath() + request.getSession().getId();
    // 从缓存中获取，当前这个请求访问了几次
    Integer redisCount = (Integer) redisTemplate.opsForValue().get(limitKey);
    if (redisCount == null) {
      //初始 次数
      redisTemplate.opsForValue().set(limitKey, 1, accessLimit.seconds(), TimeUnit.SECONDS);
      System.out.println("写入redis --");
    } else {
      System.out.println("intValue-->" + redisCount.intValue());
      if (redisCount.intValue() >= accessLimit.maxCount()) {
        return true;
      }
      // 次数自增
      redisTemplate.opsForValue().increment(limitKey);
    }
    return false;
  }

  private void render(HttpServletResponse response, String cm) throws Exception {
    response.setContentType("application/json;charset=UTF-8");
    OutputStream out = response.getOutputStream();
    out.write(cm.getBytes("UTF-8"));
    out.flush();
    out.close();
  }

}