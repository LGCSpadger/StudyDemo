package com.test.mybatis.aop;

import com.google.gson.Gson;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AOP 切面类
 * 作用：请求拦截，在一次请求的前后做特殊处理，相当于请求拦截器
 * 注意：AOP 切面类和请求拦截器不能同时存在于一个工程中，一个工程中只能二选一，否则会报错
 */
@Aspect//这个注解的作用是:将一个类定义为一个切面类
@Component//这个注解的作用：把切面类加入到IOC容器中
@Order(1)//这个注解的作用是:标记切面类的处理优先级,i值越小,优先级别越高.PS:可以注解类,也能注解到方法上
@Slf4j
public class AspectDemo {

    private Gson gson = new Gson();

    /**
     * 申明一个切点，实现接口的拦截处理，里面是excution表达式
     */
    @Pointcut("execution(* com.test.mybatis..*.*(..))")
    private void ctrlIntercept() {
    }

    /**
     * 请求method前打印内容
     * @Before(value = "ctrlIntercept()")//这个注解的作用是:在切点前执行方法,内容为指定的切点
     * @param joinPoint
     */
    @Before(value = "ctrlIntercept()")//这个注解的作用是:在切点前执行方法,内容为指定的切点
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            //打印请求内容
            log.info("================ 一次请求内容 =========== start ===========");
            log.info("请求地址：" + request.getRequestURI().toString());
            log.info("请求方式：" + request.getMethod());
            log.info("请求类方法：" + joinPoint.getSignature());
            log.info("请求方法参数：" + Arrays.toString(joinPoint.getArgs()));
            log.info("================ 一次请求内容 =========== end ===========");
        }
    }

    @Around(value = "ctrlIntercept()")//这个注解的作用是:在切点前执行方法,内容为指定的切点
    public void doAround(ProceedingJoinPoint jointPoint) {
        log.info("================ Around ===========");
    }

    // 切点方法执行后运行，不管切点方法执行成功还是出现异常
    @After(value = "ctrlIntercept()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("================ After ===========");
    }

    //在方法执行完结后打印返回内容
    //这个注解的作用是:在切入点,return后执行,如果想对某些方法的返回参数进行处理,可以在这操作
    @AfterReturning(returning = "o", pointcut = "ctrlIntercept()")
    public void methodAfterReturing(Object o) {
        log.info("-------------- 一次请求的返回内容 -------------- start --------------");
        log.info("Response内容：" + gson.toJson(o));
        log.info("-------------- 一次请求的返回内容 -------------- end --------------");
    }

}