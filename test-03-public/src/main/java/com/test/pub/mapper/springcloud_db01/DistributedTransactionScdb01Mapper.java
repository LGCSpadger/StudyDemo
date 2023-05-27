package com.test.pub.mapper.springcloud_db01;

import com.baomidou.dynamic.datasource.annotation.DS;
import java.util.Map;

@DS("springcloud_db01")
public interface DistributedTransactionScdb01Mapper {

  void saveProduct(Map map);

}
