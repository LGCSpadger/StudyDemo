package com.test.pub.service;

import com.test.pub.entity.WxzwZdyxqk;

import java.util.List;

/**
 * (WxzwZdyxqk)表服务接口
 *
 * @author LGCSpadger
 * @since 2021-05-17 21:39:30
 */
public interface WxzwZdyxqkService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WxzwZdyxqk queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<WxzwZdyxqk> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 实例对象
     */
    WxzwZdyxqk insert(WxzwZdyxqk wxzwZdyxqk);

    /**
     * 修改数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 实例对象
     */
    WxzwZdyxqk update(WxzwZdyxqk wxzwZdyxqk);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}