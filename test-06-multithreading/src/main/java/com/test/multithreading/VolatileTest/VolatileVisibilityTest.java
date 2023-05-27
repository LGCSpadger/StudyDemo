package com.test.multithreading.VolatileTest;

/**
 *
 */
public class VolatileVisibilityTest {

  //变量 initFlag 没有 volatile 关键字修饰，main()运行结果：线程2结束了，initFlag 变成 true 了，但是线程1仍然感知不到 initFlag 的改变，所以线程1不会结束
  private static boolean initFlag = false;

  //变量 initFlag 有 volatile 关键字修饰，main()运行结果：线程2结束了，initFlag 变成 true 了，线程1感知到 initFlag 的改变，所以线程1也立即结束了
//  private static volatile boolean initFlag = false;

  public static void main(String[] args) {
    //线程1
    new Thread(() -> {
      System.out.println("waiting data...");
      while (!initFlag) {

      }
      System.out.println("======================success");
    }).start();

    //线程2
    new Thread(() -> prepareData()).start();
  }

  public static void prepareData() {
    System.out.println("prepare data...");
    initFlag = true;
    System.out.println("prepare data end...");
  }

}
