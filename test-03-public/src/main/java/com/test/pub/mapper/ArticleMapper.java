package com.test.pub.mapper;

import com.github.pagehelper.PageInfo;
import com.test.pub.entity.Article;
import com.test.pub.entity.TtuTz;
import com.test.pub.entity.TtuZxl;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (Article)表数据库访问层
 *
 * @author LGCSpadger
 * @since 2021-05-14 20:08:22
 */
public interface ArticleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Article queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Article> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param article 实例对象
     * @return 对象列表
     */
    List<Article> queryAll(Article article);

    /**
     * 新增数据
     *
     * @param article 实例对象
     * @return 影响行数
     */
    int insert(Article article);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Article> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Article> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Article> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<Article> entities);

    /**
     * 修改数据
     *
     * @param article 实例对象
     * @return 影响行数
     */
    int update(Article article);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    List<TtuTz> selectAllTtuTz();

    List<TtuZxl> selectAllTtuZxl();

    //这里如果没有 @Param("tableName") ，就会报错：There is no getter for property named 'tableName' in 'class java.lang.String'
    List<Article> test(@Param("tableName") String tableName);

    List<Map<String,Object>> test01(int pageNum,int pageSize);

    //测试调用存储过程
    List<Map<String,String>> testProceduce(String txfsName);

    List<Map<String,Object>> selectAllAct();

    PageInfo<Article> selectArticlePage(int pageNum, int pageSize);

    PageInfo<Map<String,Object>> selectArticlePageOther(int pageNum, int pageSize);

    List<Article> findAllArticleByPageF(int pageNum,int pageSize);

    List<Map<String,Object>> findAllArticleByPageFOther(int pageNum,int pageSize);

    List<Article> testSql(Map param);

}