package com.test.pub.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.test.common.response.CommonCode;
import com.test.common.response.QueryResponseResult;
import com.test.common.response.QueryResult;
import com.test.common.response.ResponseResult;
import com.test.pub.entity.Article;
import com.test.pub.entity.Author;
import com.test.pub.mapper.StandardMapper;
import com.test.pub.service.StandardService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class StandardServiceImpl implements StandardService, InitializingBean {

  @Autowired
  private StandardMapper standardMapper;

  /**
   * service 初始化执行的方法，可以在该方法中执行建表等操作
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    //创建--文章表
    createTableArticle();
    //创建--作者表
    createTableAuthor();
  }

  private void createTableAuthor() {
    try {
      standardMapper.createTableAuthor();
      log.info("创建--作者表--成功");
    } catch (Exception e) {
      log.info("作者表--已经存在");
    }
  }

  private void createTableArticle() {
    try {
      standardMapper.createTableArticle();
      log.info("创建--文章表--成功");
    } catch (Exception e) {
      log.info("文章表--已经存在");
    }
  }

  @Override
  public QueryResponseResult<Author> queryAuthorList(int page, int size, Author author) {
    PageHelper.startPage(page, size);
    Page<Author> result = standardMapper.queryAuthorList(author);
    List<Author> list = result.getResult();
    long total = result.getTotal();
    QueryResult<Author> queryResult = new QueryResult<>();
    queryResult.setList(list);
    queryResult.setTotal(total);
//    int a = 1 / 0;//模拟异常
    return new QueryResponseResult<Author>(CommonCode.SUCCESS,queryResult);
  }

  @Override
  public QueryResponseResult<Author> queryAuthorAndArticleList(int page, int size, Author author) {
    PageHelper.startPage(page, size);
    Page<Author> result = standardMapper.queryAuthorAndArticleList(author);
    List<Author> list = result.getResult();
    long total = result.getTotal();
    QueryResult<Author> queryResult = new QueryResult<>();
    queryResult.setList(list);
    queryResult.setTotal(total);
    return new QueryResponseResult<Author>(CommonCode.SUCCESS,queryResult);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public ResponseResult saveArticle(Article article) {
    article.setCreateTime(System.currentTimeMillis() / 1000L);
    standardMapper.saveArticle(article);
    return new ResponseResult(CommonCode.SUCCESS);
  }

  @Override
  public ResponseResult saveBatchArticle(List<Article> datas) {
    standardMapper.insertBatch(datas);
    return new ResponseResult(CommonCode.SUCCESS);
  }

  @Override
  public ResponseResult updateArticle(Article article) {
    standardMapper.deleteArticle(article);
    return new ResponseResult(CommonCode.SUCCESS);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public ResponseResult deleteArticle(Article article) {
    standardMapper.deleteArticle(article);
    return new ResponseResult(CommonCode.SUCCESS);
  }

  @Override
  public QueryResponseResult<Article> queryDataList(int page, int size, Article article) {
    PageHelper.startPage(page, size);
    Page<Article> result = standardMapper.queryDataList(article);
    List<Article> list = result.getResult();
    long total = result.getTotal();
    QueryResult<Article> queryResult = new QueryResult<>();
    queryResult.setList(list);
    queryResult.setTotal(total);
//    int a = 1 / 0;//模拟异常
    return new QueryResponseResult<Article>(CommonCode.SUCCESS,queryResult);
  }

}
