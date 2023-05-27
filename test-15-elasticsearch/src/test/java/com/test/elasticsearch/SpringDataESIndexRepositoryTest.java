package com.test.elasticsearch;

import com.test.elasticsearch.entity.Product;
import com.test.elasticsearch.mapper.ProductRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Elasticsearch 测试类
 * 使用 ElasticsearchRepository 操作es索引库
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class SpringDataESIndexRepositoryTest {

  @Resource
  private ProductRepository productRepository;

  /**
   * 功能：ElasticsearchRepository测试 -- 查询全部文档
   * 方法：
   * Iterable<T> findAll()
   */
  @Test
  public void queryDocumentAll() {
    List<Product> list = new ArrayList<>();
    Iterable<Product> iterable = productRepository.findAll();
    Iterator<Product> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    System.out.println("ElasticsearchRepository测试，findAll()，查询全部文档，结果：" + list);
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 添加文档
   * 方法：
   * <S extends T> S save(S var1)：参数为对象
   * 备注：
   * ① 文档 id 存在则是更新，否则为新增
   */
  @Test
  public void addDocument() {
    Product product = new Product();
    product.setId("1001");
    product.setName("小米 S13 PRO");
    product.setCategory("手机");
    product.setPrice(4866.88);
    product.setUpTime(new Date(1634524735000L));
    product.setDownTime(new Date(2549414335000L));
    product.setImages("H:\\test\\test001.png");
    Product saveResult = productRepository.save(product);
    System.out.println("ElasticsearchRepository测试，save()，添加文档，结果：" + saveResult);
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 更新文档
   * 方法：
   * <S extends T> S save(S var1)：参数为对象
   * 备注：
   * ① 文档 id 存在则是更新，否则为新增
   */
  @Test
  public void updateDocument() {
    Product product = new Product();
    product.setId("8Y2VWocB_PEbInAhr0cj");
    product.setName("小米 S12 PRO");
    product.setCategory("手机");
    product.setPrice(4866.88);
    product.setUpTime(new Date(1634524735000L));
    product.setDownTime(new Date(1981420735000L));
    product.setImages("H:\\test\\集合体系图.png");
    Product saveResult = productRepository.save(product);
    System.out.println("ElasticsearchRepository测试，save()，更新文档，结果：" + saveResult);
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 批量添加文档
   * 方法：
   * <S extends T> Iterable<S> saveAll(Iterable<S> var1)：参数可以是 List<Product>
   */
  @Test
  public void addDocumentBatch() {
    List<Product> list = new ArrayList<>();
    Product product01 = new Product();
    product01.setId("1002");
    product01.setName("小米 S11");
    product01.setCategory("手机");
    product01.setPrice(3966.88);
    product01.setUpTime(new Date(1602729535000L));
    product01.setImages("F:\\pictures\\yy测试下载图片.jpg");
    list.add(product01);
    Product product02 = new Product();
    product02.setId("1003");
    product02.setName("比亚迪 汉EV");
    product02.setCategory("汽车");
    product02.setPrice(268888.00);
    product02.setUpTime(new Date(1623724735000L));
    list.add(product02);
    Product product03 = new Product();
    product03.setId("1004");
    product03.setName("华为 Mate50 PRO");
    product03.setCategory("手机");
    product03.setPrice(6999.00);
    product03.setUpTime(new Date(1665801535000L));
    list.add(product03);
    Iterable<Product> iterable = productRepository.saveAll(list);
    Iterator<Product> iterator = iterable.iterator();
    List<Product> saveAllResponse = new ArrayList<>();
    while (iterator.hasNext()) {
      saveAllResponse.add(iterator.next());
    }
    System.out.println("ElasticsearchRepository测试，saveAll()，批量添加文档，结果：" + saveAllResponse);
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 删除文档
   * 方法：
   * void delete(T var1)：方法中传入的对象必须包含 id 属性值，如果 id 属性值为null，是无法删除的
   */
  @Test
  public void deleteDocument() {
    Product product = new Product();
    product.setId("1001");
    product.setName("小米 S12 PRO");
    product.setCategory("手机");
    System.out.println("ElasticsearchRepository测试，delete()，删除文档，删除前是否存在：" + productRepository.existsById(product.getId() == null ? "1001" : product.getId().toString()));
    productRepository.delete(product);
    System.out.println("ElasticsearchRepository测试，delete()，删除文档，删除后是否存在：" + productRepository.existsById(product.getId() == null ? "1001" : product.getId().toString()));
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 根据文档id删除文档
   * 方法：
   * void deleteById(ID var1)：这里 id 要是 String 类型
   */
  @Test
  public void deleteDocumentById() {
    productRepository.deleteById("1001");
    System.out.println("ElasticsearchRepository测试，deleteById()，根据文档id删除文档，成功删除！！！");
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 批量删除文档
   * 方法：
   * void deleteAll(Iterable<? extends T> var1)：传入的对象集合中的每个对象都必须包含 id 属性值，如果 id 属性值为null，是无法删除的
   */
  @Test
  public void deleteDocumentBatch() {
    List<Product> list = new ArrayList<>();
    Product product01 = new Product();
    product01.setId("1002");
    list.add(product01);
    Product product02 = new Product();
//    product02.setId(1003L);
    product02.setName("比亚迪 汉EV");
    product02.setCategory("汽车");
    list.add(product02);
    Product product03 = new Product();
    product03.setId("1004");
    list.add(product03);
    productRepository.deleteAll(list);
    System.out.println("ElasticsearchRepository测试，deleteAll()，批量删除文档，删除成功！！！");
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 根据文档id查询文档
   * 方法：
   * Optional<T> findById(ID var1)：id 要是 String 类型
   */
  @Test
  public void queryDocumentById() {
    Optional<Product> optional = productRepository.findById("1001");
    if (optional.isPresent()) {
      Product product = optional.get();
      System.out.println("ElasticsearchRepository测试，findById()，根据文档id查询文档，结果：" + product);
    } else {
      System.out.println("ElasticsearchRepository测试，findById()，根据文档id查询文档，没有查到结果：" + optional);
    }
  }

  /**
   * 功能：ElasticsearchRepository测试 -- 根据多个文档id查询文档
   * 方法：
   * Iterable<T> findAllById(Iterable<ID> var1)：参数可以是一个 List<String>
   */
  @Test
  public void queryDocumentAllById() {
    List<Product> result = new ArrayList<>();
    List<String> productIds = Arrays.asList("1001","1003");
    Iterable<Product> iterable = productRepository.findAllById(productIds);
    Iterator<Product> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      result.add(iterator.next());
    }
    System.out.println("ElasticsearchRepository测试，findAllById()，根据多个文档id查询文档，结果：" + result);
  }

  /**
   * 功能：ElasticsearchRepository自定义查询方法测试 -- 根据类型名称查询商品列表
   * 方法：
   * List<Product> findProductsByCategory(String category)：自定义方法
   */
  @Test
  public void findProductsByCategory() {
    List<Product> products = productRepository.findProductsByCategory("手机");
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductsByCategory()，根据类型名称查询商品列表，结果：" + products);
  }

  /**
   * 功能：ElasticsearchRepository自定义查询方法测试 -- 根据商品id查询对应的商品
   * 方法：
   * Product findProductById(Long id)：自定义方法
   */
  @Test
  public void queryDocumentByCategory() {
    Product product = productRepository.findProductById(1001L);
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductById()，根据商品id查询对应的商品，结果：" + product);
  }

  /**
   * 功能：ElasticsearchRepository自定义查询方法测试 -- 查询名称中包含某个关键字的所有商品
   * 方法：
   * List<Product> findProductsByNameContains(String nameKeyword)：自定义方法
   * 备注：
   * ① 这里的查询条件 nameKeyword 不会被分词，查询条件是完全匹配
   */
  @Test
  public void findProductsByNameContains() {
    List<Product> products01 = productRepository.findProductsByNameContains("迪");
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductsByNameContains()，查询名称中包含某个关键字的所有商品，结果：" + products01);
    List<Product> products02 = productRepository.findProductsByNameContains("比亚");
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductsByNameContains()，查询名称中包含某个关键字的所有商品，结果：" + products02);
    List<Product> products03 = productRepository.findProductsByNameContains("比亚迪");
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductsByNameContains()，查询名称中包含某个关键字的所有商品，结果：" + products03);
  }

  /**
   * 功能：ElasticsearchRepository自定义查询方法测试 -- 查询商品id在[id1,id2]区间内的所有商品
   * 方法：
   * Page<Product> findProductsByIdBetween(Long id1,Long id2, Pageable pageable)：自定义方法
   * 备注：
   * ① id1、id2 被包含在内
   * ② Pageable 分页的起始页是 第0页，不是 第1页
   */
  @Test
  public void findProductsByIdBetween() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Product> page = productRepository.findProductsByIdBetween(1001L, 1004L, pageable);
    List<Product> products = page.getContent();
    System.out.println("ElasticsearchRepository自定义查询方法测试，findProductsByIdBetween()，查询商品id在[id1,id2]区间内的所有商品，结果：" + products);
  }

}
