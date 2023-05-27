package com.test.pub.service;

import com.test.common.response.QueryResponseResult;
import com.test.common.response.ResponseResult;
import com.test.pub.entity.Article;
import com.test.pub.entity.Author;
import java.util.List;

public interface StandardService {

  /**
   * 查询作者列表
   * @param author
   * @return
   */
  QueryResponseResult<Author> queryAuthorList(int page, int size, Author author);

  /**
   * 查询作者及作者写的小说列表
   * @param page
   * @param size
   * @param author
   * @return
   */
  QueryResponseResult<Author> queryAuthorAndArticleList(int page, int size, Author author);

  /**
   * 保存文章信息
   * @param article
   * @return
   */
  ResponseResult saveArticle(Article article);

  /**
   * 批量插入
   * @param datas
   * @return
   */
  ResponseResult saveBatchArticle(List<Article> datas);

  /**
   * 修改文章信息
   * @param article
   * @return
   */
  ResponseResult updateArticle(Article article);

  /**
   * 删除文章信息
   * @param article
   * @return
   */
  ResponseResult deleteArticle(Article article);

  /**
   * 分页查询
   * @param page
   * @param size
   * @param article
   * @return
   */
  QueryResponseResult<Article> queryDataList(int page,int size,Article article);

}
