package com.test.pub.task;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    Set<Integer> set = new HashSet<>();
    while (in.hasNextLine()) {
      int a = Integer.parseInt(in.nextLine());
      set.add(a);
    }
    TreeSet<Integer> treeSet = new TreeSet<>(((o1,o2)->o1.compareTo(o2)));
    treeSet.addAll(set);
    System.out.println(treeSet);
  }

}
