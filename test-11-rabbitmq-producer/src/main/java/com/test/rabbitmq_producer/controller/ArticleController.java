package com.test.rabbitmq_producer.controller;

import com.github.pagehelper.PageInfo;
import com.test.rabbitmq_producer.entity.Article;
import com.test.rabbitmq_producer.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 文章信息管理表(Article)表控制层
 *
 * @author liu.gc
 * @since 2022-12-19 14:04:40
 */
@RestController
@RequestMapping("/article")
@Api(value = "/articleApi", description = "文章信息管理表(Article)接口Api")
public class ArticleController {
    /**
     * 服务对象
     */
    @Resource
    private ArticleService articleService;

    /**
     * 分页查询
     *
     * @param article 筛选条件
     * @param page      当前页
     * @param size      页大小
     * @return 查询结果
     */
    @ApiOperation("分页查询")
    @GetMapping("/queryByPage")
    public ResponseEntity<PageInfo<Article>> queryByPage(Article article, int page, int size) {
        return ResponseEntity.ok(this.articleService.queryByPage(article, page, size));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/queryById/{id}")
    public ResponseEntity<Article> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.articleService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param article 实体
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public ResponseEntity<Article> add(Article article) {
        return ResponseEntity.ok(this.articleService.insert(article));
    }

    /**
     * 编辑数据
     *
     * @param article 实体
     * @return 编辑结果
     */
    @ApiOperation("编辑数据")
    @PutMapping("/edit")
    public ResponseEntity<Article> edit(Article article) {
        return ResponseEntity.ok(this.articleService.update(article));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @ApiOperation("删除数据")
    @DeleteMapping("/deleteById")
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.articleService.deleteById(id));
    }

}
