package com.test.elasticsearch.mapper;

import java.util.List;
import java.util.Map;

public interface ProductMapper {

  List<Map<String,Object>> queryAll();

}
