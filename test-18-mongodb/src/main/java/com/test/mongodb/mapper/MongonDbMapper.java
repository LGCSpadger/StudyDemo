package com.test.mongodb.mapper;

import com.test.mongodb.entity.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 在spring boot中使用 MongoRepository 的时候，不需要在启动类中添加 @MapperScan("com.test.mongodb.mapper") 注解，也不需要在 MongonDbMapper 接口上添加 @Repository 注解。
 * MongonDbMapper 接口继承 MongoRepository 后会自动创建bean
 */
public interface MongonDbMapper extends MongoRepository<CmsPage,String> {

  /**
   * 根据页面名称查询
   * @param pageName
   * @return
   */
  CmsPage findByPageName(String pageName);

}
