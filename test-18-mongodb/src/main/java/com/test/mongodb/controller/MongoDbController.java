package com.test.mongodb.controller;

import com.test.common.response.QueryResponseResult;
import com.test.mongodb.controller.result.CmsPageResult;
import com.test.mongodb.controller.vo.QueryVo;
import com.test.mongodb.entity.CmsPage;
import com.test.mongodb.service.MongoDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mongodb")
@Api(value = "/mongodbApi", description = "mongodb数据操作接口Api")
public class MongoDbController {

  @Resource
  private MongoDbService mongoDbService;

  @ApiOperation("查询全部")
  @GetMapping(value = "/findAll")
  public QueryResponseResult<CmsPage> findAll() {
    QueryResponseResult<CmsPage> result = mongoDbService.findAll();
    return result;
  }

  @ApiOperation("查询--分页")
  @PostMapping(value="/findPage")
  public QueryResponseResult<CmsPage> findPage(@RequestParam("page") int page,@RequestParam("size") int size,
      @RequestParam(value = "orderBy",required = false) String orderBy,
      @RequestBody(required = false) List<QueryVo> condList) {
    QueryResponseResult<CmsPage> result = mongoDbService.findPage(page, size, orderBy, condList);
    return result;
  }

  @ApiOperation("查询单条数据")
  @PostMapping(value="/findOne")
  public CmsPageResult findOne(@RequestBody List<QueryVo> condList) {
    CmsPageResult result = mongoDbService.findOne(condList);
    return result;
  }

}
