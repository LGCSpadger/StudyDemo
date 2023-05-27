package com.test.pub.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * Spring Task 定时任务配置类
 * 在 Spring Task 中，一个定时任务类中的多个定时任务默认是串行的，比如一个定时任务类 ChooseTask 中有两个定时任务 test01、test02，因为默认只有一个线程，
 * 所以 test01 和 test02会按顺序执行，上一个定时任务执行完之后才会执行下一个定时任务
 * 想要让多个定时任务异步（同时）执行，就需要该配置类来增加线程池中的线程数量
 * @author Administrator
 * @version 1.0
 **/
@Configuration
//@EnableScheduling//开启任务调度（在启动类上加了该注解，在异步任务调度的配置类中就不需要加该注解了，在配置类中加了，就不需要在启动类中加）
public class AsyncTaskConfig implements SchedulingConfigurer, AsyncConfigurer {

    //线程池线程数量
    private int corePoolSize = 5;

    @Bean
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();//初始化线程池
        scheduler.setPoolSize(corePoolSize);//线程池容量
        return scheduler;
    }

    @Override
    public Executor getAsyncExecutor() {
        Executor executor = taskScheduler();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler());
    }

}
