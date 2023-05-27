package com.test.multithreading.threadImplWay;

/**
 * 多线程实现方式一：继承Thread类
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class MainThread1 {

  public static void main(String[] args) {
    //线程实例化
    Thread thread = new MyThread();
    //启动线程，线程数量会 +1，新增的线程和主线程之间是异步执行的
//    thread.start();
    //主线程执行 Thread 类中的 run() 方法，线程数量不会 +1
    thread.run();
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}

class MyThread extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}