package com.test.mybatis_plus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBlog {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 博客文章id
     */
    private String blogId;

    /**
     * 博客文章名称
     */
    private String blogName;

}
