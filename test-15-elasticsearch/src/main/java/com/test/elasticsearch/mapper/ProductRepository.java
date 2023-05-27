package com.test.elasticsearch.mapper;

import com.test.elasticsearch.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * ElasticsearchRepository<Product, String> 中的 Product 为索引对应的实体类，String 为索引的id数值类型
 */
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

  /**
   * 根据类型名称查询商品列表
   * @param category 品牌名称
   * @return
   */
  List<Product> findProductsByCategory(String category);

  /**
   * 根据商品id查询对应的商品
   * @param id  商品id
   * @return
   */
  Product findProductById(Long id);

  /**
   * 查询名称中包含某个关键字的所有商品
   * @param nameKeyword 会进行完整匹配，不会被分词
   * @return
   */
  List<Product> findProductsByNameContains(String nameKeyword);

  /**
   * 查询商品id在[id1,id2]区间内的所有商品
   * @param id1 商品id
   * @param id2 商品id
   * @param pageable  分页参数
   * @return
   */
  Page<Product> findProductsByIdBetween(Long id1,Long id2, Pageable pageable);

}
