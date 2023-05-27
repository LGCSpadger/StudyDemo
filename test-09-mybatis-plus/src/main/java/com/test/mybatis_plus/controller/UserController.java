package com.test.mybatis_plus.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.mybatis_plus.entity.User;
import com.test.mybatis_plus.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author spadger
 * @since 2021-09-01 14:26:47
 */
@Api(value="用户信息管理",tags = {"用户信息管理"})
@RestController
@RequestMapping("user")
public class UserController extends ApiController {

    /**
     * 服务对象
     */
    @Autowired
    private UserService userService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询全部")
    @GetMapping("selectAll")
    public R selectAll(Page<User> page, User user) {
        return success(this.userService.page(page, new QueryWrapper<>(user)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("根据id查询")
    @GetMapping("selectOne/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.userService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping("insert")
    public R insert(@RequestBody User user) {
        return success(this.userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping("update")
    public R update(@RequestBody User user) {
        return success(this.userService.updateById(user));
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
        return success(this.userService.removeByIds(idList));
    }

    @ApiOperation("新增数据")
    @PostMapping("getUserBySId")
    public R getUserBySId(@RequestBody User user, long id) {
        System.out.println(user);
        List<User> users = this.userService.getUserBySId(id);
        return success(JSON.toJSONString(users));
    }
}