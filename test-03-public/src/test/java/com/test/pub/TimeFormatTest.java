package com.test.pub;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.test.pub.entity.Article;
import com.test.pub.entity.UserPo;
import com.test.pub.mapper.StandardMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试类--时间格式化
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class TimeFormatTest {

  //时间加减--月份加减
  @Test
  public void test01() throws ParseException {
    List<String> list = new ArrayList<>();
    String ym = "202205";
    String format = "yyyyMM";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date parse = sdf.parse(ym);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parse);
    int beNum = 12;
    for (int i = 1; i < 12; i++) {
      calendar.add(Calendar.MONTH, -1);
      String result = sdf.format(calendar.getTime());
      list.add(result);
    }
    Collections.sort(list);
    System.out.println(list);
  }

  //时间加减--日期加减
  @Test
  public void test02() throws ParseException {
    List<String> list = new ArrayList<>();
    String ym = "20230529";
    String format = "yyyyMMdd";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date parse = sdf.parse(ym);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parse);
    int beNum = 12;
    for (int i = 1; i < 12; i++) {
      calendar.add(Calendar.DATE, -1);
      String result = sdf.format(calendar.getTime());
      list.add(result);
    }
    Collections.sort(list);
    System.out.println(list);
  }

  //
  @Test
  public void testT() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    System.out.println(list);
//    list.add(0,100);
    list.set(0,100);
    System.out.println(list);
  }

}
