package com.test.multithreading.threadImplWay;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 多线程实现方式三：实现Callable接口
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class MainThread3 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    //创建FutureTask实例，传入MyCallable实例
    FutureTask<String> task = new FutureTask<String>(new MyCallable());
    //线程实例化
    Thread thread = new Thread(task);
    //启动线程，线程数量会 +1，新增的线程和主线程之间是异步执行的
    thread.start();
//    String result = null;
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
//      result = task.get();
    }
//    System.out.println(result);
  }

}

class MyCallable implements Callable<String> {

  public String call() {
    for (int i = 0; i < 5; i++) {
      System.out.println("线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
    return "MyCallable线程执行完成!";
  }

}