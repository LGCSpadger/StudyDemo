//package com.test.elasticsearch.service.impl;
//
//import com.test.common.response.CommonCode;
//import com.test.common.response.QueryResponseResult;
//import com.test.common.response.QueryResult;
//import com.test.common.response.ResponseResult;
//import com.test.elasticsearch.entity.Product;
//import com.test.elasticsearch.mapper.ProductRepository;
//import com.test.elasticsearch.service.ProductService;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ProductServiceImpl implements ProductService {
//
//  @Autowired
//  private ElasticsearchRestTemplate esTemplate;
//
//  @Autowired
//  private ProductRepository productRepository;
//
//  /**
//   * 指定索引新增一条数据（文档）
//   * @param product
//   * @return
//   */
//  @Override
//  public ResponseResult save(Product product) {
//    productRepository.save(product);
//    return new ResponseResult(CommonCode.SUCCESS);
//  }
//
//  /**
//   * 根据品牌名称查询商品列表
//   * @param brand 品牌名称
//   * @return
//   */
//  @Override
//  public QueryResponseResult<Product> findProductsByBrand(String brand) {
//    List<Product> list = productRepository.findProductsByBrand(brand);
//    QueryResult<Product> queryResult = new QueryResult<>();
//    queryResult.setList(list);
//    queryResult.setTotal(list.size());
//    return new QueryResponseResult<Product>(CommonCode.SUCCESS,queryResult);
//  }
//}
