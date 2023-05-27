package com.test.pub.mapper;

import com.test.pub.entity.WxzwZdyxqk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (WxzwZdyxqk)表数据库访问层
 *
 * @author LGCSpadger
 * @since 2021-05-17 21:39:33
 */
public interface WxzwZdyxqkMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WxzwZdyxqk queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<WxzwZdyxqk> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wxzwZdyxqk 实例对象
     * @return 对象列表
     */
    List<WxzwZdyxqk> queryAll(WxzwZdyxqk wxzwZdyxqk);

    /**
     * 新增数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 影响行数
     */
    int insert(WxzwZdyxqk wxzwZdyxqk);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<WxzwZdyxqk> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WxzwZdyxqk> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<WxzwZdyxqk> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WxzwZdyxqk> entities);

    /**
     * 修改数据
     *
     * @param wxzwZdyxqk 实例对象
     * @return 影响行数
     */
    int update(WxzwZdyxqk wxzwZdyxqk);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}