package com.test.pub.service.impl;

import com.test.pub.entity.WxzwZdyxqk;
import com.test.pub.mapper.WxzwZdyxqkMapper;
import com.test.pub.service.WxzwZdyxqkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (WxzwZdyxqk)表服务实现类
 *
 * @author LGCSpadger
 * @since 2021-05-17 21:39:30
 */
@Service("wxzwZdyxqkService")
public class WxzwZdyxqkServiceImpl implements WxzwZdyxqkService {

    @Autowired
    private WxzwZdyxqkMapper wxzwZdyxqkMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public WxzwZdyxqk queryById(Integer id) {
        return this.wxzwZdyxqkMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<WxzwZdyxqk> queryAllByLimit(int offset, int limit) {
        return this.wxzwZdyxqkMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 实例对象
     */
    @Override
    public WxzwZdyxqk insert(WxzwZdyxqk wxzwZdyxqk) {
        this.wxzwZdyxqkMapper.insert(wxzwZdyxqk);
        return wxzwZdyxqk;
    }

    /**
     * 修改数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 实例对象
     */
    @Override
    public WxzwZdyxqk update(WxzwZdyxqk wxzwZdyxqk) {
        this.wxzwZdyxqkMapper.update(wxzwZdyxqk);
        return this.queryById(wxzwZdyxqk.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.wxzwZdyxqkMapper.deleteById(id) > 0;
    }
}