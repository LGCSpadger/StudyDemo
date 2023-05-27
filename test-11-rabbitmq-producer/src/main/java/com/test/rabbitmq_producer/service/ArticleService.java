package com.test.rabbitmq_producer.service;

import com.github.pagehelper.PageInfo;
import com.test.rabbitmq_producer.entity.Article;

/**
 * 文章信息管理表(Article)表服务接口
 *
 * @author liu.gc
 * @since 2022-12-19 14:04:20
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
     * 分页查询
     *
     * @param article 筛选条件
     * @param page 当前页
     * @param size 页大小
     * @return 查询结果
     */
    PageInfo<Article> queryByPage(Article article, int page, int size);

    /**
     * 新增数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    Article insert(Article article);

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

}
