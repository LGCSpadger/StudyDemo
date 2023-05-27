//package com.test.elasticsearch.controller;
//
//import com.test.common.response.QueryResponseResult;
//import com.test.common.response.ResponseResult;
//import com.test.elasticsearch.entity.Product;
//import com.test.elasticsearch.service.ProductService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/elasticsearchApi")
//@Api(value = "/elasticsearchApi", description = "es相关接口Api")
//public class ElasticsearchController {
//
//  @Autowired
//  private ProductService productService;
//
//  @ApiOperation("指定索引新增一条数据（文档）")
//  @PostMapping("/save")
//  public ResponseResult save(@RequestBody Product product) {
//    log.info("es索引库--新增文档--参数：{}",product);
//    ResponseResult result = productService.save(product);
//    return result;
//  }
//
//  @ApiOperation("根据品牌名称查询商品列表")
//  @GetMapping("/findProductsByBrand")
//  public ResponseResult findProductsByBrand(@RequestParam("brand") String brand) {
//    log.info("es索引库--根据品牌名称查询商品列表--品牌名称：{}",brand);
//    QueryResponseResult<Product> result = productService.findProductsByBrand(brand);
//    return result;
//  }
//
//}
