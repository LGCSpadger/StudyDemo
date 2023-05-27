package com.test.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * indexName    指定索引名
 * shards       指定分片（无特殊配置，默认即可）
 * replicas     指定副本集（无特殊配置，默认即可）
 */
@Data
@Document(indexName = "product", shards = 1, replicas = 1)
public class Product implements Serializable {

  private static final long serialVersionUID = 551589397625941752L;

  @Id
  private String id;//商品唯一标识
  @Field(type = FieldType.Text)
  private String name;//商品名称
  @Field(type = FieldType.Keyword)
  private String category;//商品分类
  @Field(type = FieldType.Double)
  private Double price;//商品价格
  @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd",timezone="GMT+8")
  private Date upTime;//商品上架时间
  @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone="GMT+8")
  private Date downTime;//商品下架时间
  @Field(type = FieldType.Keyword, index = false)//index = false 表示该字段不能作为查询条件
  private String images;//图片地址

}
