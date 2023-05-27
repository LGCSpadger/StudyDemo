package com.test.mybatis_plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.mybatis_plus.entity.User;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author spadger
 * @since 2021-09-01 14:26:48
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> selectUserBySId(long id);

}