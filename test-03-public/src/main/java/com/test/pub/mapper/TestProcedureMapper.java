package com.test.pub.mapper;

import java.util.List;
import java.util.Map;

/**
 * 测试存储过程调用
 */
public interface TestProcedureMapper {

    //测试调用只包含输出参数的存储过程
    List<Map<String,String>> testProcedureIn(String txfsName,int idValue);

    //测试调用包含输出参数和输出参数的存储过程
    List<Map<String,String>> testProcedureOut(String txfsName,int idValue,int outParam);

}
