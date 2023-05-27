package com.test.pub;

import com.test.pub.entity.Article;
import com.test.pub.mapper.StandardMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 文章表测试类
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class ArticleTest {

  @Autowired
  private StandardMapper standardMapper;

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  /**
   * 文章表 article 入数据 -- 单条插入
   */
  @Test
  public void test01() {
    String str = "常情况下，一张表中可能有多个字段都需要创建索引，但是索引太多不仅无法提高性能，反而会降低查询性能。此时可以考虑多个字段建立一个组合索引在Linux中，不同用户是有不同目录"
        + "访问权限的，所以首先创建一个目录，作为这个ftp用户"
        + "十年间，我国的自贸区快速向内陆省份覆盖，如今，不沿边，不靠海的内陆省份，正成为我国高水平对外开放的新高地，为高质量发展注入强劲动力。能不能驾驭好世界第二大经济体，"
        + "能不能保持经济社会持续健康发展，从根本上讲取决于党在经济社会发展中的领导核心作用发挥得好不好。”加强党对经济工作的全面领导是我国经济发展的根本保证，这一科学判断，植根于新时代"
        + "伟大变革的壮阔实践，来源于坚定不移做好自己事情的果敢勇毅，成为习近平经济思想原创性贡献中至关重要的内容全面从严治党是新时代党的自我革命的伟大实践，开辟了百年大党自我革命的新境界。"
        + "党的十八大以来，以习近平同志为核心的党中央以前所未有的勇气和定力推进全面从严治党，推动新时代全面从严治党取得了历史性、开创性成就，产生了全方位、深层次影响";
    Random rd = new Random();
    char[] charArray = str.replaceAll("[\\pP‘’“”]", "").toCharArray();
    Set<Character> set = new HashSet<>();
    for (int i = 0; i < charArray.length; i++) {
      set.add(charArray[i]);
    }
    List<Character> list = new ArrayList<>();
    list.addAll(set);
    long startTime = System.currentTimeMillis();
    for (int j = 0; j < 300000; j++) {
      int i1 = rd.nextInt(10) + 1;//[1,10]
      StringBuilder name = new StringBuilder();
      for (int i = 0; i < i1; i++) {
        int i2 = rd.nextInt(list.size());
        name.append(list.get(i2));
      }
      int i3 = i1 * 2;
      StringBuilder artDesc = new StringBuilder();
      for (int i = 0; i < i3; i++) {
        int i4 = rd.nextInt(list.size());
        artDesc.append(list.get(i4));
      }
      int i5 = i1 < 2 ? 2 : i1 / 2;
      StringBuilder author = new StringBuilder();
      for (int i = 0; i < i5; i++) {
        int i6 = rd.nextInt(list.size());
        author.append(list.get(i6));
      }
      int i7 = rd.nextInt(str.length());
      int i8 = rd.nextInt(str.length());
      String content = "";
      if (i7 <= i8) {
        content = str.substring(i7,i8);
      } else {
        content = str.substring(i8,i7);
      }
      Article article = new Article();
      article.setName(name.toString());
      article.setArtDesc(artDesc.toString());
      article.setAuthor(author.toString());
      article.setCreateTime(System.currentTimeMillis() / 1000L);
      article.setContent(content);
      article.setType(i1);
      log.info("第 " + j + "条：{}",article);
      standardMapper.saveArticle(article);
    }
    long endTime = System.currentTimeMillis();
    log.info("程序执行时间：{}",endTime - startTime," 毫秒");
  }

  /**
   * 文章表 article 入数据 -- 批量插入
   */
  @Test
  public void test02() {
    String str = "常情况下，一张表中可能有多个字段都需要创建索引，但是索引太多不仅无法提高性能，反而会降低查询性能。此时可以考虑多个字段建立一个组合索引在Linux中，不同用户是有不同目录"
        + "访问权限的，所以首先创建一个目录，作为这个ftp用户"
        + "十年间，我国的自贸区快速向内陆省份覆盖，如今，不沿边，不靠海的内陆省份，正成为我国高水平对外开放的新高地，为高质量发展注入强劲动力。能不能驾驭好世界第二大经济体，"
        + "能不能保持经济社会持续健康发展，从根本上讲取决于党在经济社会发展中的领导核心作用发挥得好不好。”加强党对经济工作的全面领导是我国经济发展的根本保证，这一科学判断，植根于新时代"
        + "伟大变革的壮阔实践，来源于坚定不移做好自己事情的果敢勇毅，成为习近平经济思想原创性贡献中至关重要的内容全面从严治党是新时代党的自我革命的伟大实践，开辟了百年大党自我革命的新境界。"
        + "党的十八大以来，以习近平同志为核心的党中央以前所未有的勇气和定力推进全面从严治党，推动新时代全面从严治党取得了历史性、开创性成就，产生了全方位、深层次影响";
    Random rd = new Random();
    char[] charArray = str.replaceAll("[\\pP‘’“”]", "").toCharArray();
    Set<Character> set = new HashSet<>();
    for (int i = 0; i < charArray.length; i++) {
      set.add(charArray[i]);
    }
    List<Character> list = new ArrayList<>();
    list.addAll(set);
    long startTime = System.currentTimeMillis();
    List<Article> datas = new ArrayList<>();
    for (int j = 0; j < 10000; j++) {
      int i1 = rd.nextInt(10) + 1;//[1,11)
      StringBuilder name = new StringBuilder();
      for (int i = 0; i < i1; i++) {
        int i2 = rd.nextInt(list.size());
        name.append(list.get(i2));
      }
      int i3 = i1 * 2;
      StringBuilder artDesc = new StringBuilder();
      for (int i = 0; i < i3; i++) {
        int i4 = rd.nextInt(list.size());
        artDesc.append(list.get(i4));
      }
      int i5 = i1 < 2 ? 2 : i1 / 2;
      StringBuilder author = new StringBuilder();
      for (int i = 0; i < i5; i++) {
        int i6 = rd.nextInt(list.size());
        author.append(list.get(i6));
      }
      int i7 = rd.nextInt(str.length());
      int i8 = rd.nextInt(str.length());
      String content = "";
      if (i7 <= i8) {
        content = str.substring(i7,i8);
      } else {
        content = str.substring(i8,i7);
      }
      Article article = new Article();
      article.setName(name.toString());
      article.setArtDesc(artDesc.toString());
      article.setAuthor(author.toString());
      article.setCreateTime(System.currentTimeMillis() / 1000L);
      article.setContent(content);
      article.setType(i1);
      log.info("第 " + j + "条：{}",article);
      datas.add(article);
      if (datas.size() == 1000) {
        //单条插入，循环插入1000条数据（实测：3741ms）
        startTime = System.currentTimeMillis();
//        for (int i = 0; i < datas.size(); i++) {
//          standardMapper.saveArticle(datas.get(i));
//        }
        log.info("单条插入，循环插入1000条数据--耗时：{}",System.currentTimeMillis() - startTime);
        //批量插入1000条数据（实测：150ms）
        startTime = System.currentTimeMillis();
        standardMapper.insertBatch(datas);
        log.info("批量插入1000条数据--耗时：{}",System.currentTimeMillis() - startTime);
        datas.clear();
      }
    }
    long endTime = System.currentTimeMillis();
    log.info("程序执行时间：{}",endTime - startTime," 毫秒");
  }

  @Test
  public void test03() {
    List<String> list = new ArrayList<>();
    list.add("aaa");
    list.add("bbb");
    System.out.println(list.size());
    list.clear();
    System.out.println(list.size());
  }

}
