package com.test.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.test.common.utils.TimeFormatUtil;
import com.test.pub.entity.Article;

import java.util.*;
import java.util.stream.Collectors;

import com.test.pub.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 临时测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TemporaryTest {

  @Test
  public void test01() {
    List<String> list = Arrays.asList("烧烤","方便面","螺蛳粉","牛肉粉丝汤","牛肉面");
    Random rd = new Random();
    int index = rd.nextInt(list.size());//[0,list.size())
    System.out.println(list.get(index));
  }

  @Test
  public void test02() {
    Map<String,String> params = new HashMap<>();
    params.put("base_sql","{\"sql_1\":SELECT nename,alarmtitle,count(1) FROM (SELECT distinct nename,alarmtitle,日期 FROM (${base_sql})) group by nename,alarmtitle having count(1)>2\"}");
    Gson gson = new Gson();
    Map<String,String> json = gson.fromJson(params.get("alarmcountcondition") + "", Map.class);
    System.out.println(JSON.toJSONString(json));
    Map<String,String> json01 = gson.fromJson(params.get("base_sql") + "", Map.class);
    System.out.println(JSON.toJSONString(json01));
  }

  /**
   * 反射的基本用法
   * @throws ClassNotFoundException
   */
  @Test
  public void test03() throws ClassNotFoundException {
    Article article = new Article();
    Class<? extends Article> aClass = article.getClass();
    Class<?> cls = Class.forName("com.test.pub.entity.Article");
    System.out.println(cls);
    System.out.println(cls.getName());
    System.out.println(cls.getSimpleName());
    System.out.println(cls.getPackage());
    System.out.println(cls.getPackage().getName());
  }

  @Test
  public void test04() {
    HashMap map = new HashMap<>();
    map.put(1234,"java");
    System.out.println(map.get("1234"));//输出：null
    System.out.println(map.get(1234));//输出：java
  }

  @Test
  public void test05() {
    // 创建并初始化 List
    List<Person> list = new ArrayList<Person>() {{
      add(new Person(1, 30, 80, "北京", null, null));
      add(new Person(2, 20, 90, "西安", null, null));
      add(new Person(3, 40, 100, "上海", null, null));
      add(new Person(4, 50, 80, "无锡", null, null));
      add(new Person(5, 50, 90, "南京", null, null));
    }};
    String rank1 = "getAge";
    // 使用 Stream 排序
    list = list.stream().sorted(Comparator.comparing(Person::getAge).reversed())
            .collect(Collectors.toList());
    int rank = 0;
    // 打印 list 集合
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        list.get(i).setAgeRank(++rank);
      } else {
        if (list.get(i).getAge() == list.get(i-1).getAge()) {
          list.get(i).setAgeRank(rank);
        } else {
          list.get(i).setAgeRank(++rank);
        }
      }
    }
    rank = 0;
    // 使用 Stream 排序
    list = list.stream().sorted(Comparator.comparing(Person::getScore).reversed())
            .collect(Collectors.toList());
    // 打印 list 集合
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        list.get(i).setScoreRank(++rank);
      } else {
        if (list.get(i).getScore() == list.get(i-1).getScore()) {
          list.get(i).setScoreRank(rank);
        } else {
          list.get(i).setScoreRank(++rank);
        }
      }
    }
    list.forEach(p -> {
      System.out.println(p);
    });

  }


}
