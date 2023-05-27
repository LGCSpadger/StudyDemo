package com.test.pub.entity;
 
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
/**
 * 小说作者信息管理表(Author)实体类
 *
 * @author liu.gc
 * @since 2023-04-12 14:09:02
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    private static final long serialVersionUID = -35098094095174842L;
    
    /**
     * id
     */ 
    private Long id;
    /**
     * 作者姓名
     */ 
    private String authorName;
    /**
     * 作者年龄
     */ 
    private Integer age;
    /**
     * 作者出生时间
     */ 
    private Long birthday;
    /**
     * 代表作
     */ 
    private String magnumOpus;
    /**
     * 作者写的小说列表
     */
    private List<Article> articleList;
 
}