package com.test.mybatis.service.impl;

import com.test.mybatis.mapper.ArticleMapper;
import com.test.mybatis.entity.Article;
import com.test.mybatis.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 文章信息管理表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-09 16:24:28
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
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Article> queryByPage(Article article, PageRequest pageRequest) {
        long total = this.articleMapper.count(article);
        return new PageImpl<>(this.articleMapper.queryAllByLimit(article, pageRequest), pageRequest, total);
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
