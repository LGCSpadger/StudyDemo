package com.test.pub.mapper;

import com.github.pagehelper.Page;
import com.test.pub.entity.Article;
import com.test.pub.entity.Author;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StandardMapper {

  /**
   * 创建 作者表
   */
  void createTableAuthor();

  /**
   * 创建 文章表
   */
  void createTableArticle();

  /**
   * 查询作者列表
   * @param author
   * @return
   */
  Page<Author> queryAuthorList(Author author);

  /**
   * 查询作者及作者写的小说列表
   * @param author
   * @return
   */
  Page<Author> queryAuthorAndArticleList(Author author);

  /**
   * 保存文章
   * @param article
   * @return
   */
  int saveArticle(Article article);

  /**
   * 批量新增数据
   *
   * @param datas List<Article> 实例对象列表
   * @return 影响行数
   */
  int insertBatch(@Param("datas") List<Article> datas);

  int insertBatchForOracle(@Param("datas") List<Article> datas);

  /**
   * 删除文章
   * @param article
   * @return
   */
  int deleteArticle(Article article);

  /**
   * 更新文章
   * @param article
   * @return
   */
  int updateArticle(Article article);

  /**
   * 文章列表分页查询
   * @param article
   * @return
   */
  Page<Article> queryDataList(Article article);

}
