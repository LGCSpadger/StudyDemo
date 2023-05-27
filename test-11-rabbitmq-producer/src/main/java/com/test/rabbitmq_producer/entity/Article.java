package com.test.rabbitmq_producer.entity;
 
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
/**
 * 文章信息管理表(Article)实体类
 *
 * @author liu.gc
 * @since 2022-12-19 11:08:19
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable {

    private static final long serialVersionUID = 632115499209311185L;
    
    /**
     * id
     */ 
    private Long id;
    /**
     * 文章名称
     */ 
    private String name;
    /**
     * 文章描述
     */ 
    private String artDesc;
    /**
     * 作者
     */ 
    private String author;
    /**
     * 创作时间
     */ 
    private Long createTime;
    /**
     * 文章内容
     */ 
    private String content;
    /**
     * 文章类型（1：天文学；2：地理学；3：物理学；4：生物学；5：it技术；6：网络安全；7：美食；8：化学；9：建筑学；10：大自然）
     */ 
    private Long type;
    /**
     * 扩展字段01
     */ 
    private String exField01;
    /**
     * 扩展字段02
     */ 
    private String exField02;
    /**
     * 扩展字段03
     */ 
    private String exField03;
 
}
