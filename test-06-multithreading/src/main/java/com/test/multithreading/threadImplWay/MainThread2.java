package com.test.multithreading.threadImplWay;

/**
 * 多线程实现方式二：实现Runnable接口
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class MainThread2 {

  public static void main(String[] args) {
    //线程实例化
    Thread thread = new Thread(new MyRunnable());
    //启动线程，线程数量会 +1，新增的线程和主线程之间是异步执行的
    thread.start();
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}

class MyRunnable implements Runnable {

  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}