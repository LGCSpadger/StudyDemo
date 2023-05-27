package com.test.pub;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * List集合相关测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class ListTest {

  //List集合删除其中某个元素，保留剩下的元素
  @Test
  public void test01() {
    List<String> list = new ArrayList<>();
    list.add("斗破苍穹");
    list.add("画江湖之不良人");
    list.add("秦时明月");
    log.info("移除元素之前：{}",list);
    list.remove(0);
    log.info("移除元素之后：{}",list);
  }

  //
  @Test
  public void test02() {

  }

}
