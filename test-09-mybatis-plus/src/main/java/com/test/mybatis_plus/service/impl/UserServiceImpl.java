package com.test.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mybatis_plus.entity.User;
import com.test.mybatis_plus.mapper.UserMapper;
import com.test.mybatis_plus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author spadger
 * @since 2021-09-01 14:26:46
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUserBySId(long id) {
        return userMapper.selectUserBySId(id);
    }
}