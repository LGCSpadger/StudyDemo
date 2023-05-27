package com.test.pub;

import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 华为笔试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class ExamTest {

  //字符串最后一个单词的长度
  @Test
  public void test01() {
    Scanner sc = new Scanner(System.in);
    String str = sc.nextLine();
    String[] s = str.split(" ");
    int length = s[s.length - 1].length();
    System.out.println(length);
  }

  //计算某字符出现次数
  //写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
  @Test
  public void test02() {
    Scanner sc = new Scanner(System.in);
    String str =sc.nextLine().toLowerCase();
    String s = sc.nextLine();
    System.out.print(str.length()-str.replaceAll(s,"").length());
  }

}
