package com.test.elasticsearch.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "user", shards = 1, replicas = 1)
public class User {

  @Id
  @Field(type = FieldType.Long)
  private Long id;
  @Field(type = FieldType.Text)
  private String name;
  @Field(type = FieldType.Keyword)
  private String sex;
  @Field(type = FieldType.Integer)
  private Integer age;
  @Field(type = FieldType.Keyword)
  private String role;
  @Field(type = FieldType.Date)
  private Date birthday;
  @Field(type = FieldType.Long)
  private Long worth;
  @Field(type = FieldType.Boolean)
  private boolean isDied;

}
