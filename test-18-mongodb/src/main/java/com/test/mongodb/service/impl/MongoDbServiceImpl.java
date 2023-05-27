package com.test.mongodb.service.impl;

import com.alibaba.fastjson.JSON;
import com.test.common.response.CommonCode;
import com.test.common.response.QueryResponseResult;
import com.test.common.response.QueryResult;
import com.test.mongodb.controller.result.CmsPageResult;
import com.test.mongodb.controller.vo.QueryVo;
import com.test.mongodb.entity.CmsPage;
import com.test.mongodb.mapper.MongonDbMapper;
import com.test.mongodb.service.MongoDbService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoDbServiceImpl implements MongoDbService {

  @Resource
  private MongonDbMapper mongonDbMapper;

  @Resource
  private MongoTemplate mongoTemplate;

  /**
   * 查询全部
   * @return
   */
  @Override
  public QueryResponseResult<CmsPage> findAll() {
    List<CmsPage> list = mongonDbMapper.findAll();
    log.info("方法名：findAll，查询结果：{}",list);
    QueryResult<CmsPage> queryResult = new QueryResult<>();
    queryResult.setList(list);
    queryResult.setTotal(list.size());
    return new QueryResponseResult<CmsPage>(CommonCode.SUCCESS,queryResult);
  }

  /**
   * 查询--分页
   * @param page 当前页
   * @param size 每页展示条数
   * @param orderBy 排序字段
   * @param condList 其他查询条件
   * @return
   */
  @Override
  public QueryResponseResult<CmsPage> findPage(int page, int size, String orderBy, List<QueryVo> condList) {
    //查询条件
//    Criteria criteria = criteriaHandle(condList);
    Query query = new Query(criteriaHandle(condList));
    Pageable pageable = PageRequest.of(page, size);
    //分页条件
    query.with(pageable);
    //分组条件
    if (StringUtils.isNotEmpty(orderBy)) {
      query.with(Sort.by(Sort.Order.asc(orderBy)));
    }
    log.info("方法名：findPage，查询参数：{}",query);
    List<CmsPage> list = mongoTemplate.find(query, CmsPage.class);
    log.info("方法名：findPage，查询结果：{}",list);
    Criteria criteria01 = new Criteria();
    log.info("方法名：findPage，查询参数--criteria01：{}", JSON.toJSONString(criteria01));
    Query query01 = new Query(criteria01.and("pageAliase").is("轮播图"));
    log.info("方法名：findPage，查询参数--query01：{}",query01);
    List<CmsPage> list01 = mongoTemplate.find(query01, CmsPage.class);
    log.info("方法名：findPage，查询结果--list01：{}",list01);
    QueryResult<CmsPage> queryResult = new QueryResult<>();
    queryResult.setList(list);
    queryResult.setTotal(mongoTemplate.find(new Query(), CmsPage.class).size());
    return new QueryResponseResult<CmsPage>(CommonCode.SUCCESS,queryResult);
  }

  /**
   * 查询单条数据
   * @param condList 查询条件
   * @return
   */
  @Override
  public CmsPageResult findOne(List<QueryVo> condList) {
    //查询条件
    Criteria criteria = criteriaHandle(condList);
    Query query = new Query(criteria);
    log.info("方法名：findOne，查询参数：{}",query);
    CmsPage cmsPage = mongoTemplate.findOne(query, CmsPage.class);
    log.info("方法名：findOne，查询结果：{}",cmsPage);
    return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
  }

  /**
   * 查询条件封装处理
   * @param condList
   * @return
   */
  private Criteria criteriaHandle(List<QueryVo> condList) {
    Criteria criteria = new Criteria();
    if (CollectionUtils.isNotEmpty(condList)) {
      for (int i = 0; i < condList.size(); i++) {
        QueryVo queryVo = condList.get(i);
        if (queryVo != null) {
          switch (queryVo.getCompareType()) {
            case 1:
              criteria.and(queryVo.getKey()).is(queryVo.getValue());
              break;
            case 2:
              criteria.and(queryVo.getKey()).gt(queryVo.getValue());
              break;
            case 3:
              criteria.and(queryVo.getKey()).lt(queryVo.getValue());
              break;
            case 4:
              criteria.and(queryVo.getKey()).in(queryVo.getValue());
              break;
            default:
              criteria.and(queryVo.getKey()).is(queryVo.getValue());
              break;
          }
        }
      }
    }
    log.info("方法名：criteriaHandle，封装结果：{}",JSON.toJSONString(criteria));
    return criteria;
  }
}
