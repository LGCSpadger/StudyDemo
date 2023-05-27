package com.test.multithreading.threadImplWay;

/**
 * 多线程实现方式二：实现Runnable接口 -- 使用 Lambda 表达式简化代码开发
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class MainThread2_Simple {

  public static void main(String[] args) {
    //使用 Lambda 表达式实例化线程，并启动线程，新增的线程和主线程之间是异步执行的
    new Thread(()->{
      for (int i = 0; i < 5 ; i++) {
        System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i);
      }
    },"runnable").start();
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}