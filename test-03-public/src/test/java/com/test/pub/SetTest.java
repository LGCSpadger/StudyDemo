package com.test.pub;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Set集合相关测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class SetTest {

  //判断set集合是否包含
  @Test
  public void test01() {
    Set<String> set = new HashSet<>(Arrays.asList("2G基站退服告警","OML故障告警","站点ABIS控制链路断","小区中断告警","GSM小区退出服务告警"));
    boolean contains01 = set.contains("2G基站退服告警");
    boolean contains02 = set.contains("测试");
    log.info("是否包含01：" + contains01);
    log.info("是否包含02：" + contains02);
  }

  //TreeSet集合：能自动排序、去重
  @Test
  public void test02() {
    TreeSet set = new TreeSet();
    set.add(10);
    set.add(2);
    set.add(8);
    set.add(5);
    Iterator iterator = set.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
  }

}
