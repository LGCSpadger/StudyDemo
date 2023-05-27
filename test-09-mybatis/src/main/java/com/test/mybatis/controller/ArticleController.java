package com.test.mybatis.controller;

import com.test.mybatis.entity.Article;
import com.test.mybatis.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 文章信息管理表(Article)表控制层
 *
 * @author liu.gc
 * @since 2022-09-09 17:46:31
 */
@Slf4j
@RestController
@RequestMapping("article")
@Api(value = "/articleApi", description = "文章信息管理表Api")
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
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @ApiOperation("分页查询")
    @GetMapping
    public ResponseEntity<Page<Article>> queryByPage(Article article, PageRequest pageRequest) {
        return ResponseEntity.ok(articleService.queryByPage(article, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public ResponseEntity<Article> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(articleService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param article 实体
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public ResponseEntity<Article> add(Article article) {
        return ResponseEntity.ok(articleService.insert(article));
    }

    /**
     * 编辑数据
     *
     * @param article 实体
     * @return 编辑结果
     */
    @ApiOperation("编辑数据")
    @PutMapping
    public ResponseEntity<Article> edit(Article article) {
        return ResponseEntity.ok(articleService.update(article));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @ApiOperation("删除数据")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(articleService.deleteById(id));
    }

}

