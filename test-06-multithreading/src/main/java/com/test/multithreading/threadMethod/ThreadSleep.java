package com.test.multithreading.threadMethod;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 多线程常用操作方法一：sleep()
 * 说明：sleep() 方法其实就是线程的休眠方法，我们可以让正在执行的线程休眠一段时间。
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class ThreadSleep {

  public static void main(String[] args) {
    //使用 Lambda 表达式实例化线程，并启动线程，新增的线程和主线程之间是异步执行的
    new Thread(()->{
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      for (int i = 0; i < 5 ; i++) {
        System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i + "，打印时间：" + sdf.format(new Date()));
        try {
          System.out.println("打印一次，睡眠1000毫秒！");
          Thread.sleep(1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    },"runnable").start();
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}
