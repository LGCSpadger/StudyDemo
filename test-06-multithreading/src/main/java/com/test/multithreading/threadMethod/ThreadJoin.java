package com.test.multithreading.threadMethod;

/**
 * 多线程常用操作方法二：join()
 * 说明：join是加入的意思，所以这个方法也代表了有特殊情况需要插队的意思，其实这个方法的作用是让一个线程优先强制运行完成的意思，在这个加入的线程没有执行完成前，另一个线程也会处于阻塞状态。
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class ThreadJoin {

  public static void main(String[] args) {
    //线程实例化
    Thread thread = new MyThreadJoin();
    //启动线程，线程数量会 +1，新增的线程和主线程之间是异步执行的
    thread.start();
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      //主线程执行到 i=3 时需等待新增的 thread 线程执行完成后才能继续执行
      if (i == 3) {
        try {
          //join() 方法中不设置时间，表示当前线程需要一直等待，等到新增的 thread 线程执行完成后才能继续执行
          thread.join();
          //join() 方法中设置时间100ms，表示当前线程需要等待100ms，如果100ms结束了，新增的 thread 线程无论有没有执行完成，当前线程都可以继续执行
          thread.join(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}

class MyThreadJoin extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 10000; i++) {
      System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}