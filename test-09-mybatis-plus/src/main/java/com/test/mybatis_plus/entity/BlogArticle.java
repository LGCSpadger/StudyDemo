package com.test.mybatis_plus.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (BlogArticle)表实体类
 *
 * @author spadger
 * @since 2021-09-01 14:59:17
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticle implements Serializable {

    private static final long serialVersionUID = 776503119457492545L;

    /**
     * 主键ID
     */
    @TableField("id")
    private Long id;

    /**
     * 博客文章名称
     */
    @TableField("name")
    private String name;

    /**
     * 博客主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 创作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 上一次修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 作者id
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 作者名称
     */
    @TableField("author_name")
    private String authorName;


}