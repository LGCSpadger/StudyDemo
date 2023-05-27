package com.test.multithreading.threadMethod;

/**
 * 多线程常用操作方法三：interrupted()
 * 说明：interrupt代表中断的意思，所以我们可以使用interrupt()中断或者打断一个线程的运行，但是这个中断并非让线程直接停止的意思，而是一种其它线程的打断通知，会使线程的打断标记置为true，最终是否停止线程的运行还需要你根据线程的打断标记决定。我们一般使用两阶段终止模式来更好地终止一个线程的运行而不是直接使用线程的stop()方法简单粗暴地停止线程，stop()方法已经被官方标记为过时方法，我们一般不建议使用。
 *
 * @author 刘广超
 * @date 2023-04-20
 */
public class ThreadInterrupted {

  public static void main(String[] args) {
    //线程实例化，指定线程名称为 Thread-t
    Thread thread = new Thread(new MyRunnableInterrupted(),"Thread-t");
    //启动线程，线程数量会 +1，新增的线程和主线程之间是异步执行的
    thread.start();
    //主线程先睡眠1s
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //执行主线程代码
    for (int i = 0; i < 5; i++) {
      //主线程执行到 i=3 时需等待新增的 thread 线程执行完成后才能继续执行
      if (i == 3) {
        //主线程想要中断 Thread-t 线程执行
        thread.interrupt();
      }
      System.out.println("我是主线程：" + Thread.currentThread().getName() + "，打印了：" + i);
    }
  }

}

class MyRunnableInterrupted implements Runnable{

  public void run() {
    while(true){
      Thread current = Thread.currentThread();
      //如果打断标记为true,则让 Thread-t 线程自己结束任务
      if(Thread.interrupted()){
        break;
      }
      //Thread-t 线程每隔200ms执行一次线程任务
      System.out.println("线程名称：" + current.getName() + "，线程状态：" + current.getState());
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
        current.interrupt();
      }
    }
  }

}