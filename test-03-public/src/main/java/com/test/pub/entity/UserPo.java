package com.test.pub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor//添加全参构造器
public class UserPo {

  private String name;
  private String cla;
  private Double score;

}
