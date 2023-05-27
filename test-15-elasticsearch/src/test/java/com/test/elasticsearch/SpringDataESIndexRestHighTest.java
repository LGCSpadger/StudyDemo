package com.test.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.elasticsearch.entity.User;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.StatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 使用 Spring Data Elasticsearch 操作 es 索引库
 * RestHighLevelClient 客户端测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 service层 就无法注入
@SpringBootTest
public class SpringDataESIndexRestHighTest {

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  /**
   * 创建索引
   */
  @Test
  public void restHighCreateIndex() throws IOException {
    //创建创建索引的请求对象
    CreateIndexRequest createIndexRequest = new CreateIndexRequest("user");
    //创建索引
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    //获取创建索引的响应状态
    boolean acknowledged = createIndexResponse.isAcknowledged();
    System.out.println("索引操作，创建索引，创建结果：" + acknowledged);
  }

  /**
   * 查询索引库的相关信息
   */
  @Test
  public void restHighQueryIndexsInfo() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("user");
    GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
    //获取创建索引的响应状态
    //获取索引库的相关信息
    System.out.println("索引库的别名：" + getIndexResponse.getAliases());
    System.out.println("索引库的映射信息：" + JSON.toJSONString(getIndexResponse.getMappings()));
    System.out.println("索引库的配置信息：" + getIndexResponse.getSettings());
    System.out.println("索引库的默认配置信息：" + getIndexResponse.getDefaultSettings());
    System.out.println("不知道啥信息111：" + getIndexResponse.getIndices());
    System.out.println("不知道啥信息222：" + getIndexResponse.getDataStreams());
  }

  /**
   * 删除索引库
   */
  @Test
  public void restHighDeleteIndex() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("user");
    System.out.println("索引操作，删除之前--判断索引是否存在，结果：" + restHighLevelClient.indices().exists(getIndexRequest,RequestOptions.DEFAULT));
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("user");
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    System.out.println("索引操作，删除索引，删除结果：" + acknowledgedResponse.isAcknowledged());
    System.out.println("索引操作，删除之后--判断索引是否存在，结果：" + restHighLevelClient.indices().exists(getIndexRequest,RequestOptions.DEFAULT));
  }

  /**
   * 索引库添加文档
   * 注意：
   * ① 如果索引库不存在，添加文档的时候，默认会自动创建索引库，前提是实体类中使用 @Document(indexName = "user", shards = 1, replicas = 1) 注解绑定了索引库
   * ② 如果映射关系没有创建，添加文档的时候，默认会根据实体类中 @Field(type = FieldType.Text) 注解绑定的属性自动创建映射关系
   */
  @Test
  public void restHighAddDocument() throws IOException {
    IndexRequest indexRequest = new IndexRequest();
    indexRequest.index("user").id("1001");
    User user01 = new User();
    user01.setId(1001L);
    user01.setName("萧炎");
    user01.setSex("男");
    user01.setAge(20);
    user01.setRole("男主");
    //向es添加数据，必须将数据转换为json格式
    ObjectMapper objectMapper = new ObjectMapper();
    String user01Str = objectMapper.writeValueAsString(user01);
    indexRequest.source(user01Str, XContentType.JSON);
    //向索引库中插入数据
    IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，添加文档，结果：" + indexResponse.getResult());
  }

  /**
   * 索引库查询文档
   */
  @Test
  public void restHighGetDocument() throws IOException {
    GetRequest getRequest = new GetRequest();
    getRequest.index("user").id("1001");//根据文档id查询
    GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，查询文档，结果：" + getResponse.getSourceAsString());
  }

  /**
   * 索引库修改文档
   */
  @Test
  public void restHighUpdateDocument() throws IOException {
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index("user").id("1001");
    updateRequest.doc(XContentType.JSON,"id",1002L);
    UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，修改文档，结果：" + updateResponse.getResult());
  }

  /**
   * 索引库删除文档
   */
  @Test
  public void restHighDeleteDocument() throws IOException {
    GetRequest getRequest = new GetRequest();
    getRequest.index("user").id("1001");//根据文档id查询
    System.out.println("文档操作，删除之前 -- 判断文档是否存在，结果：" + restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT));
    DeleteRequest deleteRequest = new DeleteRequest();
    deleteRequest.index("user").id("1001");//根据文档id进行删除
    DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，删除文档，结果：" + deleteResponse.getResult());
    System.out.println("文档操作，删除之后 -- 判断文档是否存在，结果：" + restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT));
  }

  /**
   * 索引库批量添加文档
   */
  @Test
  public void restHighAddDocumentBatch() throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON,"name","萧炎","sex","男","age",18,"role","男主","birthday",new Date(1417251624000L),"worth",100000000L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON,"name","云韵","sex","女","age",25,"role","女二","birthday",new Date(867574824000L),"worth",55555555L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON,"name","萧熏儿","sex","女","age",18,"role","女一","birthday",new Date(1411981224000L),"worth",99999999L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1004").source(XContentType.JSON,"name","美杜莎","sex","女","age",25,"role","女一","birthday",new Date(858934824000L),"worth",99999999L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1005").source(XContentType.JSON,"name","药老","sex","男","age",55,"role","男二","birthday",new Date(-87145176000L),"worth",88888888L,"isDied",true));
    bulkRequest.add(new IndexRequest().index("user").id("1006").source(XContentType.JSON,"name","海波东","sex","男","age",45,"role","男三","birthday",new Date(240915624000L),"worth",66666666L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1007").source(XContentType.JSON,"name","石漠城萧鼎","sex","男","age",21,"role","男四","birthday",new Date(990349224000L),"worth",7895562L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1008").source(XContentType.JSON,"name","石漠城萧厉","sex","男","age",20,"role","男四","birthday",new Date(1026982824000L),"worth",6975898L,"isDied",false));
    bulkRequest.add(new IndexRequest().index("user").id("1009").source(XContentType.JSON,"name","萧战-萧族族主","sex","男","age",46,"role","男四","birthday",new Date(190198824000L),"worth",8546897L,"isDied",false));
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，批量添加文档，耗时：" + bulkResponse.getTook());
  }

  /**
   * 索引库批量删除文档
   */
  @Test
  public void restHighDeleteDocumentBatch() throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(new DeleteRequest().index("user").id("1001"));
    bulkRequest.add(new DeleteRequest().index("user").id("1002"));
    bulkRequest.add(new DeleteRequest().index("user").id("1003"));
    bulkRequest.add(new DeleteRequest().index("user").id("1004"));
    bulkRequest.add(new DeleteRequest().index("user").id("1005"));
    bulkRequest.add(new DeleteRequest().index("user").id("1006"));
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println("文档操作，批量删除文档，耗时：" + bulkResponse.getTook());
  }

  /**
   * 高级查询之全量查询
   */
  @Test
  public void restHighSearchDocumentAll() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之全量查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之全量查询，查询耗时：" + searchResponse.getTook());
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之全量查询，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之精确匹配
   * 注意：
   * ① QueryBuilders.termQuery(String name,String value)：termQuery是精确匹配，即输入的查询内容是什么，就会按照什么取查询，不会解析查询内容，进行分词。这里的 name 是文档中的字段名，value 是要查询的内容。
   */
  @Test
  public void restHighSearchDocumentTermQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    //根据年龄字段查询
//    searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age",18)));
    //根据名称 name 字段精确匹配
    searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("name","炎")));
    //根据角色 role 字段精确匹配
//    searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("role","男主")));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之精确匹配，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之精确匹配，查询耗时：" + searchResponse.getTook());
    hits.forEach(p -> {
      System.out.println("文档操作，高级查询之精确匹配，查询结果，文档id：" + p.getId() + "，文档原生信息：" + p.getSourceAsString());
    });
  }

  /**
   * 高级查询之布尔查询
   */
  @Test
  public void restHighSearchDocumentBoolQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.should(QueryBuilders.rangeQuery("age").gte(20));//年龄大于等于20
    boolQueryBuilder.should(QueryBuilders.rangeQuery("age").lte(45));//年龄小于等于45
    boolQueryBuilder.must(QueryBuilders.matchQuery("sex","女"));//性别为女

    searchSourceBuilder.query(boolQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之布尔查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之布尔查询，查询耗时：" + searchResponse.getTook());
    hits.forEach(p -> {
      System.out.println("文档操作，高级查询之布尔查询，查询结果，文档id：" + p.getId() + "，文档原生信息：" + p.getSourceAsString());
    });
  }

  /**
   * 高级查询之日期查询
   */
  @Test
  public void restHighSearchDocumentDateQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("birthday");
//    rangeQueryBuilder.gt("now-21y");//now-21y：表示当前时间减去21年
    rangeQueryBuilder.gt(1027158006000L);//可以直接填时间戳，毫秒值

    searchSourceBuilder.query(rangeQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之日期查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之日期查询，查询耗时：" + searchResponse.getTook());
    hits.forEach(p -> {
      System.out.println("文档操作，高级查询之日期查询，查询结果，文档id：" + p.getId() + "，文档原生信息：" + p.getSourceAsString());
    });
  }

  /**
   * 高级查询之多个id查询
   */
  @Test
  public void restHighSearchDocumentMoreIdsQuery() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
    idsQueryBuilder.addIds("1001","1002","1003");

    searchSourceBuilder.query(idsQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之多个id查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之多个id查询，查询耗时：" + searchResponse.getTook());
    hits.forEach(p -> {
      System.out.println("文档操作，高级查询之多个id查询，查询结果，文档id：" + p.getId() + "，文档原生信息：" + p.getSourceAsString());
    });
  }

  /**
   * 高级查询之多条件组合查询
   */
  @Test
  public void restHighSearchDocumentByConditionsMore() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    //年龄必须是18岁，must 相当于 and
    boolQueryBuilder.must(QueryBuilders.matchQuery("age",18));
    //性别必须是男，must 相当于 and
//    boolQueryBuilder.must(QueryBuilders.matchQuery("sex","男"));
    //性别应该是男，should 相当于 or
    boolQueryBuilder.should(QueryBuilders.matchQuery("sex","男"));
    //性别应该是女，should 相当于 or
    boolQueryBuilder.should(QueryBuilders.matchQuery("sex","女"));
    searchSourceBuilder.query(boolQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之多条件组合查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之多条件组合查询，查询耗时：" + searchResponse.getTook());
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之多条件组合查询，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之模糊查询
   * 1、模糊查询 FuzzyQueryBuilder：QueryBuilders.fuzzyQuery(String name, Object value) 分词模糊查询，通过增加fuzziness 模糊属性，来查询
   * ① 输入的 name 会被分词
   * ② Fuzziness 参数会指定编辑次数，Fuzziness.ZERO 为绝对匹配，Fuzziness.ONE 为编辑一次（指修改一个字符），Fuzziness.TWO 为编辑两次（指修改两个字符），Fuzziness.AUTO 为默认推荐的方式
   * 2、Fuzziness 具体指什么？
   * QueryBuilders.fuzzyQuery("name", "萧").fuzziness() 方法是用来度量把一个单词转换为另一个单词需要的单字符编辑次数。单字符编辑方式如下：
   * ① 替换 一个字符到另一个字符: _f_ox -> _b_ox
   * ② 插入 一个新字符: sic -> sick
   * ③ 删除 一个字符:: b_l_ack -> back
   * ④ 换位 调整字符: _st_ar -> _ts_ar
   * 当然, 一个字符串的单次编辑次数依赖于它的长度。例如：对 hat 进行两次编辑可以得到 mad，所以允许对长度为3的字符串进行两次修改就太过了，Fuzziness 参数可以被设置成 AUTO，结果会在下面的最大编辑距离中:
   * ① Fuzziness.ZERO：适用于只有 1 或 2 个字符的字符串
   * ② Fuzziness.ONE：适用于 3、4或5个字符的字符串
   * ③ Fuzziness.TWO：适用于多于5个字符的字符串
   * 当然, 你可能发现编辑距离为 2 仍然是太过了，返回的结果好像并没有什么关联，把 Fuzziness 设置为 Fuzziness.ONE ,你可能会获得更好的结果和性能.
   */
  @Test
  public void restHighSearchDocumentByConditionsLike() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //根据姓名 name 字段进行模糊匹配，不加 fuzziness() 方法表示默认，name 字段中要包含一个 “萧” 字算满足条件
//    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "萧");
    //根据姓名 name 字段进行模糊匹配，Fuzziness.AUTO 是默认配置，name 字段中要包含一个 “萧” 字算满足条件
//    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "萧").fuzziness(Fuzziness.AUTO);
    //根据姓名 name 字段进行模糊匹配，表示 name 字段在不做任何修改的前提下包含一个 “萧” 字则满足条件
//    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "萧").fuzziness(Fuzziness.ZERO);
    //根据姓名 name 字段进行模糊匹配，表示 name 字段如果修改一个字符能使 name 字段包含一个 “萧炎” 字则满足条件
//    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "萧炎").fuzziness(Fuzziness.ONE);
    //根据姓名 name 字段进行模糊匹配，表示 name 字段如果修改两个个字符能使 name 字段包含一个 “石漠” 字则满足条件
    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "石漠").fuzziness(Fuzziness.TWO);
    searchSourceBuilder.query(fuzzyQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之模糊查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之模糊查询，查询耗时：" + searchResponse.getTook());
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之模糊查询，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之范围查询
   */
  @Test
  public void restHighSearchDocumentRange() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //指定根据 age 字段进行区间查询
    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
    //gte()：大于等于，lte()：小于等于，gt()：大于，lt()：小于。
    rangeQueryBuilder.gte(18);
    rangeQueryBuilder.lte(25);
    searchSourceBuilder.query(rangeQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之范围查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之范围查询，查询耗时：" + searchResponse.getTook());
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之范围查询，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之高亮查询
   */
  @Test
  public void restHighSearchDocumentHighLight() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("sex", "女");
    searchSourceBuilder.query(termQueryBuilder);
    //构建高亮对象
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    //高亮样式
    highlightBuilder.preTags("<font color='red'>");//前缀标签
    highlightBuilder.postTags("</font>");//后缀标签
    //选择高亮字段
    highlightBuilder.field("name");
    highlightBuilder.requireFieldMatch(false);//多字段高亮显示时，需要设置为 false
    highlightBuilder.field("role");
    searchSourceBuilder.highlighter(highlightBuilder);

    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之高亮查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之高亮查询，查询耗时：" + searchResponse.getTook());
    System.out.println("文档操作，高级查询之高亮查询，结果：" + searchResponse);
    hits.forEach(p -> {
      System.out.println("文档操作，高级查询之高亮查询，文档原生信息：" + p.getSourceAsString() + "，高亮信息：" + p.getHighlightFields());
    });
  }

  /**
   * 高级查询之查询结果字段过滤
   */
  @Test
  public void restHighSearchDocumentResultFieldFilter() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    searchSourceBuilder.query(matchAllQueryBuilder);
    //要排除的字段
    String[] excludes = {"age","role"};
    //要展示的字段
    String[] includes = {};
    searchSourceBuilder.fetchSource(includes,excludes);

    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之查询结果字段过滤，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之查询结果字段过滤，查询耗时：" + searchResponse.getTook());
    System.out.println("文档操作，高级查询之查询结果字段过滤，结果：" + searchResponse);
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之查询结果字段过滤，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之查询结果排序
   */
  @Test
  public void restHighSearchDocumentResultOrder() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    searchSourceBuilder.query(matchAllQueryBuilder);
    //要排除的字段
    String[] excludes = {"sex","role"};
    //要展示的字段
    String[] includes = {"name","age"};
    searchSourceBuilder.fetchSource(includes,excludes);
    //按照年龄字段 age 排序，SortOrder.ASC 升序
//    searchSourceBuilder.sort("age", SortOrder.ASC);
    //按照年龄字段 age 排序，SortOrder.DESC 降序
    searchSourceBuilder.sort("age", SortOrder.DESC);

    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之查询结果排序，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之查询结果排序，查询耗时：" + searchResponse.getTook());
    System.out.println("文档操作，高级查询之查询结果排序，结果：" + searchResponse);
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之查询结果排序，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之查询结果去重
   */
  @Test
  public void restHighSearchDocumentResultCollapse() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    searchSourceBuilder.query(matchAllQueryBuilder);
    //年龄 age 字段去重
    CardinalityAggregationBuilder cardinalityAggregationBuilderAgeCard = AggregationBuilders.cardinality("ageCard").field("age");
    searchSourceBuilder.aggregation(cardinalityAggregationBuilderAgeCard);
    //角色 role 字段去重
    CardinalityAggregationBuilder cardinalityAggregationBuilderRoleCard = AggregationBuilders.cardinality("roleCard").field("role");
    searchSourceBuilder.aggregation(cardinalityAggregationBuilderRoleCard);

    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之查询结果去重，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之查询结果去重，查询耗时：" + searchResponse.getTook());
    Aggregations aggregations = searchResponse.getAggregations();
    ParsedCardinality parsedCardinalityAge = aggregations.get("ageCard");
    System.out.println("文档操作，高级查询之查询结果去重，年龄去重后的结果，key：" + parsedCardinalityAge.getName() + "，去重后的数量：" + parsedCardinalityAge.getValue());
    ParsedCardinality parsedCardinalityRole = aggregations.get("roleCard");
    System.out.println("文档操作，高级查询之查询结果去重，角色去重后的结果，key：" + parsedCardinalityRole.getName() + "，去重后的数量：" + parsedCardinalityRole.getValue());
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之查询结果去重，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

  /**
   * 高级查询之聚合查询
   * 注意：
   * ① Text 类型的字段是不允许进行聚合操作的，不能进行分组统计
   */
  @Test
  public void restHighSearchDocumentAggregation() throws IOException {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("user");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    //查询年龄 age 的最大值，最大值对应的key为 avgAge
    AggregationBuilder aggregationBuilderMax = AggregationBuilders.max("maxAge").field("age");
    //查询年龄 age 的最小值，最小值对应的key为 avgAge
    AggregationBuilder aggregationBuilderMin = AggregationBuilders.min("minAge").field("age");
    //查询年龄 age 的平均值，平均值对应的key为 avgAge
    AggregationBuilder aggregationBuilderAvg = AggregationBuilders.avg("avgAge").field("age");
    //统计年龄 age 的数量，未去重
    AggregationBuilder aggregationBuilderAgeCount = AggregationBuilders.count("ageCount").field("age");
    //统计年龄 age 的数量，去重
    CardinalityAggregationBuilder cardinalityAggregationBuilderAgeCard = AggregationBuilders.cardinality("ageCard").field("age");
    //对年龄 age 字段进行统计，可以统计出最大值、最小值、平均值、数量、和......
    StatsAggregationBuilder statsAggregationBuilder = AggregationBuilders.stats("ageStatsResult").field("age");
    //根据年龄 age 进行分组，分组后的结果对应的key为 ageGroup
    AggregationBuilder aggregationBuilderAgeGroup = AggregationBuilders.terms("ageGroup").field("age");
    //根据性别 sex 进行分组，分组后的结果对应的key为 sexGroup
    AggregationBuilder aggregationBuilderSexGroup = AggregationBuilders.terms("sexGroup").field("sex");

    searchSourceBuilder.aggregation(aggregationBuilderMax);
    searchSourceBuilder.aggregation(aggregationBuilderMin);
    searchSourceBuilder.aggregation(aggregationBuilderAvg);
    searchSourceBuilder.aggregation(aggregationBuilderAgeCount);
    searchSourceBuilder.aggregation(cardinalityAggregationBuilderAgeCard);
    searchSourceBuilder.aggregation(statsAggregationBuilder);
    searchSourceBuilder.aggregation(aggregationBuilderAgeGroup);
    searchSourceBuilder.aggregation(aggregationBuilderSexGroup);
    searchSourceBuilder.sort("age", SortOrder.DESC);

    searchRequest.source(searchSourceBuilder);
    //执行查询操作
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = searchResponse.getHits();
    System.out.println("文档操作，高级查询之聚合查询，命中数量：" + hits.getTotalHits());
    System.out.println("文档操作，高级查询之聚合查询，查询耗时：" + searchResponse.getTook());
    Aggregations aggregations = searchResponse.getAggregations();
    ParsedMax parsedMax = aggregations.get("maxAge");
    System.out.println("文档操作，高级查询之聚合查询，最大值：" + parsedMax.getValue());
    ParsedMin parsedMin = aggregations.get("minAge");
    System.out.println("文档操作，高级查询之聚合查询，最小值：" + parsedMin.getValue());
    ParsedAvg parsedAvg = aggregations.get("avgAge");
    System.out.println("文档操作，高级查询之聚合查询，平均值：" + parsedAvg.getValue());
    ParsedValueCount parsedAgeCount = aggregations.get("ageCount");
    System.out.println("文档操作，高级查询之聚合查询，年龄未去重的数量：" + parsedAgeCount.getValue());
    ParsedCardinality parsedCardinalityAge = aggregations.get("ageCard");
    System.out.println("文档操作，高级查询之聚合查询，年龄去重后的数量：" + parsedCardinalityAge.getValue());
    ParsedStats ageStatsResult = aggregations.get("ageStatsResult");
    System.out.println("文档操作，高级查询之聚合查询，对年龄进行统计后的结果，统计个数（未去重）：" + ageStatsResult.getCount());
    System.out.println("文档操作，高级查询之聚合查询，对年龄进行统计后的结果，年龄平均值：" + ageStatsResult.getAvg());
    System.out.println("文档操作，高级查询之聚合查询，对年龄进行统计后的结果，年龄最大值：" + ageStatsResult.getMax());
    System.out.println("文档操作，高级查询之聚合查询，对年龄进行统计后的结果，年龄最小值：" + ageStatsResult.getMin());
    System.out.println("文档操作，高级查询之聚合查询，对年龄进行统计后的结果，年龄之和：" + ageStatsResult.getSum());
    System.out.println("############# 年龄分组结果输出部分 ###############");
    Terms termsAgeGroup = aggregations.get("ageGroup");
    for (Terms.Bucket bucket : termsAgeGroup.getBuckets()) {
      System.out.println("文档操作，高级查询之聚合查询，年龄分组，key：" + bucket.getKeyAsString() + "，value：" + bucket.getDocCount());
    }
    System.out.println("############# 年龄分组结果输出部分 ###############");
    System.out.println("------------- 性别分组结果输出部分 ----------------");
    Terms termsSexGroup = aggregations.get("sexGroup");
    for (Terms.Bucket bucket : termsSexGroup.getBuckets()) {
      System.out.println("文档操作，高级查询之聚合查询，性别分组，key：" + bucket.getKeyAsString() + "，value：" + bucket.getDocCount());
    }
    System.out.println("------------- 性别分组结果输出部分 ----------------");
    SearchHit[] result = hits.getHits();
    for (SearchHit searchHit: result) {
      System.out.println("文档操作，高级查询之聚合查询，id：" + searchHit.getId() + "，内容：" + searchHit.getSourceAsString());
    }
  }

}
