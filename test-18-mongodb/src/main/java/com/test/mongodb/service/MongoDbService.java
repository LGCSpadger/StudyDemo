package com.test.mongodb.service;

import com.test.common.response.QueryResponseResult;
import com.test.mongodb.controller.result.CmsPageResult;
import com.test.mongodb.controller.vo.QueryVo;
import com.test.mongodb.entity.CmsPage;
import java.util.List;
import java.util.Map;

public interface MongoDbService {

  /**
   * 查询全部--不分页
   * @return
   */
  QueryResponseResult<CmsPage> findAll();

  /**
   * 查询全部--分页
   * @param page 当前页
   * @param size 每页展示条数
   * @param orderBy 排序字段
   * @param condList 其他查询条件
   * @return
   */
  QueryResponseResult<CmsPage> findPage(int page, int size, String orderBy, List<QueryVo> condList);

  /**
   * 查询单条数据
   * @param condList 查询条件
   * @return
   */
  CmsPageResult findOne(List<QueryVo> condList);

}
