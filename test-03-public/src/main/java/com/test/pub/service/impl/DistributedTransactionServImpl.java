package com.test.pub.service.impl;

import com.test.pub.entity.Article;
import com.test.pub.mapper.DistributedTransactionTestMapper;
import com.test.pub.mapper.springcloud_db01.DistributedTransactionScdb01Mapper;
import com.test.pub.service.DistributedTransactionServ;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DistributedTransactionServImpl implements DistributedTransactionServ {

  @Autowired
  private DistributedTransactionTestMapper dtTestMapper;

  @Autowired
  private DistributedTransactionScdb01Mapper dtScdb01Mapper;

  @Transactional
  @Override
  public void transaction2PC() {
    Article article = new Article();
    article.setContent("武松打虎");
    article.setAuthor("施耐庵");
    article.setName("水浒传");
    dtTestMapper.saveArticle(article);
    Map product = new HashMap();
    product.put("productName","分布式事务");
    product.put("dbSource","springcloud_db01");
    dtScdb01Mapper.saveProduct(product);
  }

}
