package com.test.pub.service;

import com.github.pagehelper.PageInfo;
import com.test.pub.entity.Article;
import com.test.pub.entity.TtuTz;
import com.test.pub.entity.TtuZxl;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * (Article)表服务接口
 *
 * @author LGCSpadger
 * @since 2021-05-14 20:08:18
 */
public interface ArticleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Article queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Article> queryAllByLimit(int offset, int limit);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    List<Article> queryAll(Article article);

    /**
     * 新增数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    Article insert(Article article);

    int insertBatch(List<Article> entities);

    /**
     * 修改数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    Article update(Article article);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    void exportDataToExcel(HttpServletResponse response,String filePath,String fileName,String sheetName);

    List<TtuTz> queryAllTtuTz();

    List<TtuZxl> queryAllTtuZxl();

    List<Article> test(String tableName);

    PageInfo<Map<String,Object>> test01(int pageNum,int pageSize);

    PageInfo<Article> getArticlePage(int pageNum, int pageSize);

    PageInfo<Map<String,Object>> getArticlePageOther(int pageNum, int pageSize);

    List<Article> getAllArticleByPageF(int pageNum,int pageSize);

}