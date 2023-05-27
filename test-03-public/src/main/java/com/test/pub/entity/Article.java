package com.test.pub.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (Article)实体类
 *
 * @author LGCSpadger
 * @since 2021-05-14 20:07:20
 */
@Data
public class Article implements Serializable {

    private Long id;
    private String name;
    private String artDesc;
    private String author;
    private Long createTime;
    private String content;
    private int type;
    private String exField01;
    private String exField02;
    private String exField03;

}