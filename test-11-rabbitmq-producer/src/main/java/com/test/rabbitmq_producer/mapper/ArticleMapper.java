package com.test.rabbitmq_producer.mapper;

import com.github.pagehelper.Page;
import com.test.rabbitmq_producer.entity.Article;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 文章信息管理表(Article)表数据库访问层
 *
 * @author liu.gc
 * @since 2022-12-19 11:38:08
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
     * @param article 查询条件
     * @return 对象列表
     */
    Page<Article> queryAllByLimit(Article article);

    /**
     * 统计总行数
     *
     * @param article 查询条件
     * @return 总行数
     */
    long count(Article article);

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
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
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

}
