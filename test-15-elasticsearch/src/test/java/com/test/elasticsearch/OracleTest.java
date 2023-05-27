package com.test.elasticsearch;

import com.test.elasticsearch.mapper.ProductMapper;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Elasticsearch 测试类
 * 使用 ElasticsearchRepository 操作es索引库
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class OracleTest {

  @Resource
  private ProductMapper productMapper;

  /**
   * 功能：ElasticsearchRepository测试 -- 查询全部文档
   * 方法：
   * Iterable<T> findAll()
   */
  @Test
  public void queryDocumentAll() {
    List<Map<String, Object>> list = productMapper.queryAll();
    System.out.println("ElasticsearchRepository测试，findAll()，查询全部文档，结果：" + list);
  }

}
