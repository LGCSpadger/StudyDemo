package com.test.mybatis_plus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.mybatis_plus.entity.BlogArticle;
import com.test.mybatis_plus.service.BlogArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (BlogArticle)表控制层
 *
 * @author spadger
 * @since 2021-09-01 14:34:10
 */
@Api(value="博客文章管理",tags = {"博客文章管理"})
@RestController
@RequestMapping("blogArticle")
public class BlogArticleController extends ApiController {

    /**
     * 服务对象
     */
    @Autowired
    private BlogArticleService blogArticleService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param blogArticle 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询全部")
    @GetMapping("selectAll")
    public R selectAll(Page<BlogArticle> page, BlogArticle blogArticle) {
        return success(this.blogArticleService.page(page, new QueryWrapper<>(blogArticle)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("根据id查询")
    @GetMapping("selectAll/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.blogArticleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param blogArticle 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping("insert")
    public R insert(@RequestBody BlogArticle blogArticle) {
        return success(this.blogArticleService.save(blogArticle));
    }

    /**
     * 修改数据
     *
     * @param blogArticle 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping("update")
    public R update(@RequestBody BlogArticle blogArticle) {
        return success(this.blogArticleService.updateById(blogArticle));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping("delete")
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.blogArticleService.removeByIds(idList));
    }
}