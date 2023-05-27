package com.test.mybatis_plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.mybatis_plus.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author spadger
 * @since 2021-09-01 14:26:45
 */
public interface UserService extends IService<User> {

    List<User> getUserBySId(long id);

}