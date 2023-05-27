package com.test.common.utils;

public class TestDemo {

  private static int num01 = 2;
  private static double num02 = 2L;

  public static void demoFun(int teNum) {
    int result = teNum * teNum;
    System.out.println(result);
  }

  public static void demoFun(double teNum) {
    double result = teNum * teNum;
    System.out.println(result);
  }

  public static void main(String[] args) {
    demoFun(num01);
    demoFun(num02);
  }

}
