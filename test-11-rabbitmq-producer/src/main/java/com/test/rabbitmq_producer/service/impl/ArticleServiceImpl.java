package com.test.rabbitmq_producer.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.rabbitmq_producer.entity.Article;
import com.test.rabbitmq_producer.mapper.ArticleMapper;
import com.test.rabbitmq_producer.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文章信息管理表(Article)表服务实现类
 *
 * @author liu.gc
 * @since 2022-12-19 14:04:21
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Article queryById(Long id) {
        return this.articleMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param article 筛选条件
     * @param page 当前页
     * @param size 页大小
     * @return 查询结果
     */
    @Override
    public PageInfo<Article> queryByPage(Article article, int page, int size) {
        PageHelper.startPage(page, size);
        Page<Article> articles = this.articleMapper.queryAllByLimit(article);
        PageInfo<Article> pages = new PageInfo<Article>(articles);
        return pages;
    }

    /**
     * 新增数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    @Override
    public Article insert(Article article) {
        this.articleMapper.insert(article);
        return article;
    }

    /**
     * 修改数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    @Override
    public Article update(Article article) {
        this.articleMapper.update(article);
        return this.queryById(article.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.articleMapper.deleteById(id) > 0;
    }
}
