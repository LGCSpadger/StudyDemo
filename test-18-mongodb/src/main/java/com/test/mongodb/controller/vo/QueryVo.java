package com.test.mongodb.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryVo implements Serializable {

  @ApiModelProperty(value = "字段名称", name = "key", dataType = "String", example = "pageId")
  private String key;
  @ApiModelProperty(value = "字段值", name = "value", dataType = "Object", example = "5a754adf6abb500ad05688d9")
  private Object value;
  @ApiModelProperty(value = "比较方式", name = "compareType", dataType = "int", example = "1：等于；2：大于；3：小于；4：in；")
  private int compareType;

}
