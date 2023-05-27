package com.test.common.entity;

import lombok.Data;

/**
 * 测试
 */
@Data
public class PoiModel {

    private String content;//当前值

    private String oldContent;//同一列上一行的值

    private int rowIndex;//当前行

    private int cellIndex;//当前列

}
