package com.test.elasticsearch;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Elasticsearch 测试类
 * 使用 JdbcTemplate + X-Pack 操作es索引库
 * 备注：
 * ① X-Pack 是收费的 es 插件，可以开启 30 天试用，直接使用会报错：current license is non-compliant for [jdbc]
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class JdbcTemplateEsTest implements InitializingBean {

  private JdbcTemplate jdbcTemplate;

  @Override
  public void afterPropertiesSet() throws Exception {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.elasticsearch.xpack.sql.jdbc.EsDriver");
    dataSource.setUrl("jdbc:es://http://192.168.10.11:9200/?timezone=UTC&page.size=200");
    dataSource.setUsername("spadger");
    dataSource.setPassword("spadger");
    dataSource.setMaxTotal(10);
    dataSource.setMaxIdle(5);
    dataSource.setMaxWaitMillis(300000);
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * 功能：查询全部文档
   * 方法：
   * queryForList(String sql)
   * 备注：
   * ① sql中查询条件所涉及的字段类型不能是text，否则会报错：[name = '萧炎'] cannot operate on first argument field of data type [text]
   */
  @Test
  public void test01() {
//    String sql = "select * from user where birthday = '1997-03-21T09:00:24.000Z'";
    String sql = "select * from user";
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    log.info("使用 JdbcTemplate 操作es索引库测试，查询全部文档，结果数量：{} 条，结果：{}", list.size(), list);
  }

  /**
   * 功能：插入文档
   * 方法：
   * queryForList(String sql)
   */
  @Test
  public void test02() {
    String sql = "insert into user(name,sex,age,birthday,role) values(?,?,?,?,?,?)";
    int lines = jdbcTemplate.update(sql,"雅妃","女",21,new Date(1007800322000L),"女四");
    log.info("使用 JdbcTemplate 操作es索引库测试，插入文档，影响行数：{} ", lines);
  }

  /**
   * 功能：插入文档
   * 方法：
   * queryForList(String sql)
   */
  @Test
  public void test03() {
    String sql = "update user set worth = 111111111 where worth = 100000000";
    int lines = jdbcTemplate.update(sql);
    log.info("使用 JdbcTemplate 操作es索引库测试，插入文档，影响行数：{} ", lines);
  }


}
