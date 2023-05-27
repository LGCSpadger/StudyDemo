package com.test.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.test.mongodb.controller.vo.QueryVo;
import com.test.mongodb.mapper.MongonDbMapper;
import com.test.mongodb.entity.CmsPage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class MongoDBTest {

  @Resource
  private MongonDbMapper mongonDbMapper;

  @Resource
  private MongoTemplate mongoTemplate;

  /**
   * 测试连接mongodb
   * 方式一：创建 mongodb 客户端连接 mongodb
   */
  @Test
  public void test01() {
    //方式一：创建 mongodb 客户端连接 mongodb
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    //连接数据库
    MongoDatabase database = mongoClient.getDatabase("xc_cms");
    //连接 collection
    MongoCollection<Document> collection = database.getCollection("cms_config");
    //查询第一个文档
    Document firstDocument = collection.find().first();
    String json = firstDocument.toJson();
    System.out.println(json);
  }

  /**
   * 测试连接mongodb
   * 方式一：通过 mongodb-url 的方式连接 mongodb
   */
  @Test
  public void test02() {
    //方式二：通过 mongodb-url 的方式连接 mongodb
    MongoClientURI mongoClientURI = new MongoClientURI("mongodb://root:root@localhost:27017");
    MongoClient mongoClient = new MongoClient(mongoClientURI);
    //连接数据库
    MongoDatabase database = mongoClient.getDatabase("xc_cms");
    //连接 collection
    MongoCollection<Document> collection = database.getCollection("cms_config");
    //查询第一个文档
    Document firstDocument = collection.find().first();
    String json = firstDocument.toJson();
    System.out.println(json);
  }

  @Test
  public void test03() {
    CmsPage cmsPage = mongonDbMapper.findByPageName("index.html");
    log.info("cmsPage：{}",cmsPage);
  }

  @Test
  public void test04() {
    Criteria criteria = new Criteria();
    Query query = new Query(criteria.and("pageAliase").is("轮播图"));
    log.info("方法名：test04，查询参数：{}",query);
    CmsPage cmsPage = mongoTemplate.findOne(query, CmsPage.class);
    log.info("方法名：test04，查询结果--01：{}",cmsPage);
    List<CmsPage> list = mongoTemplate.find(query, CmsPage.class);
    log.info("方法名：test04，查询结果--02：{}",list);
  }

  @Test
  public void test05() {
    List<QueryVo> condList = new ArrayList<>();
    QueryVo queryVo = new QueryVo();
    queryVo.setKey("pageAliase");
    queryVo.setValue("轮播图");
    queryVo.setCompareType(1);
//    queryVo.setKey("pageCreateTime");
//    queryVo.setValue(1517636273256L);
//    queryVo.setCompareType(2);
    condList.add(queryVo);
    Criteria criteria = new Criteria();
    if (CollectionUtils.isNotEmpty(condList)) {
      for (int i = 0; i < condList.size(); i++) {
        QueryVo vo = condList.get(i);
        if (vo != null) {
          switch (vo.getCompareType()) {
            case 1:
              criteria.and(vo.getKey()).is(vo.getValue());
              break;
            case 2:
              criteria.and(vo.getKey()).gt(vo.getValue());
              break;
            case 3:
              criteria.and(vo.getKey()).lt(vo.getValue());
              break;
            case 4:
              criteria.and(vo.getKey()).in(vo.getValue());
              break;
            default:
              criteria.and(vo.getKey()).is(vo.getValue());
              break;
          }
        }
      }
    }
    log.info("方法名：test05，查询参数--condList：{}",condList);
    Query query = new Query(criteria);
    log.info("方法名：test05，查询参数：{}",query);
    CmsPage cmsPage = mongoTemplate.findOne(query, CmsPage.class);
    log.info("方法名：test05，查询结果--01：{}",cmsPage);
    List<CmsPage> list = mongoTemplate.find(query, CmsPage.class);
    log.info("方法名：test05，查询结果--02：{}",list);
  }

}