package com.test.elasticsearch;

import com.test.elasticsearch.entity.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 使用 Spring Data Elasticsearch 操作 es 索引库
 * ElasticsearchRestTemplate 客户端测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 service层 就无法注入
@SpringBootTest
public class SpringDataESIndexTest {

  @Autowired
  private ElasticsearchRestTemplate elasticsearchRestTemplate;

  /**
   * 创建索引
   */
  @Test
  public void createIndex() {
    /**
     * 方式一：指定索引库名称创建索引（只会创建一个名为 product 的索引库，不会创建映射）
     * 存在问题：创建索引之后，删除索引，再重新创建的时候，会报错 "reason":"index [product/906Vi3zzRaKb1zJaIKzvtg] already exists"
     * 报错原因：分词器反复创建：先用standard分词器建的index，然后使用ik分词器又建索引出现这个错误，相当于对已有的 index 修改 mapping
     * 报错解决办法：不创建索引就好了。如果已经删除掉了 product 索引库，es里是没有 product 索引的。但实体类  Product 里有"indexName = “product”"，你往索引里插入数据时，索引就自动存在了
     */
    IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("product"));
    boolean result = indexOperations.create();
    System.out.println("创建索引结果：" + result);
    boolean putMappingResult = indexOperations.putMapping(Product.class);
    System.out.println("索引映射添加结果：" + putMappingResult);
    /**
     * 方式二：指定实体类创建索引（创建一个名为 product 的索引库，同时会自动创建映射，映射关系就对应实体类 Product.class 的属性）
     * 存在问题：创建索引之后，删除索引，再重新创建的时候，会报错 "reason":"index [product/906Vi3zzRaKb1zJaIKzvtg] already exists"
     * 报错原因：分词器反复创建：先用standard分词器建的index，然后使用ik分词器又建索引出现这个错误，相当于对已有的 index 修改 mapping
     * 报错解决办法：不创建索引就好了。如果已经删除掉了 product 索引库，es里是没有 product 索引的。但实体类  Product 里有"indexName = “product”"，你往索引里插入数据时，索引就自动存在了
     */
//    IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Product.class);
//    boolean result = indexOperations.create();
//    System.out.println("创建索引结果：" + result);
  }

  /**
   * 根据索引名称判断索引是否存在
   */
  @Test
  public void existsIndex() {
    //方式一：单纯的根据索引库名称进行判断
    boolean result01 = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("product")).exists();
    System.out.println("索引是否存在：" + result01);
    //方式二：根据索引库对应的实体类，也就是加了 @Document(indexName = "product", shards = 1, replicas = 1) 注解的实体类进行判断
    boolean result02 = elasticsearchRestTemplate.indexOps(Product.class).exists();
    System.out.println("方式一，索引是否存在：" + result01);
    System.out.println("方式二，索引是否存在：" + result02);
  }

  /**
   * 根据索引名称删除索引库
   */
  @Test
  public void deleteIndex() {
    boolean result = false;
    IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Product.class);
    System.out.println("索引是否存在：" + indexOperations.exists());
    if (indexOperations.exists()) {
      result = indexOperations.delete();
    }
    System.out.println("删除结果：" + result);
    System.out.println("索引是否存在：" + indexOperations.exists());
  }

  /**
   * 创建文档（索引库插入数据）
   * 注意：
   * ① 假如索引库 product 不存在，在添加文档的时候会自动创建 product 索引库，索引库信息会和 Product.class 中的各个属性一一对应
   * ② 创建文档的时候如果指定了id，则会使用指定的id，如果没有指定文档id，es默认会自动创建一个uuid作为文档id
   * ③ 创建文档的时候如果指定了id，并且该文档已经存在，则默认为更新操作
   */
  @Test
  public void createDocument() {
    Product product = new Product();
    product.setId("1001");
    product.setName("小米 S12");
    product.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product.setPrice(3688.99);
    product.setCategory("手机");
    Product saveResult = elasticsearchRestTemplate.save(product);
    System.out.println("文档新增，指定id，创建结果：" + saveResult);
    Product product01 = new Product();
    product01.setName("一加 T3");
    product01.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product01.setPrice(4666.88);
    product01.setCategory("手机");
    Product saveResult01 = elasticsearchRestTemplate.save(product01);
    System.out.println("文档新增，不指定id，创建结果：" + saveResult01);
  }

  /**
   * 批量创建文档（索引库批量插入数据）
   * 注意：
   * ① 假如索引库 product 不存在，在添加文档的时候会自动创建 product 索引库，索引库信息会和 Product.class 中的各个属性一一对应
   * ② 创建文档的时候如果指定了id，则会使用指定的id，如果没有指定文档id，es默认会自动创建一个uuid作为文档id
   * ③ 创建文档的时候如果指定了id，并且该文档已经存在，则默认为更新操作
   */
  @Test
  public void createDocumentBatch() {
    List<Product> list = new ArrayList<>();
    Product product01 = new Product();
    product01.setId("1001");
    product01.setName("小米 S12");
    product01.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product01.setPrice(3688.99);
    product01.setCategory("手机");
    list.add(product01);
    Product product02 = new Product();
    product02.setId("1002");
    product02.setName("一加 T3");
    product02.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product02.setPrice(4666.88);
    product02.setCategory("手机");
    Product product03 = new Product();
    list.add(product02);
    product03.setId("1003");
    product03.setName("S11");
    product03.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product03.setPrice(2999.99);
    product03.setCategory("小米手机");
    list.add(product03);
    Iterable<Product> saveResult = elasticsearchRestTemplate.save(list);
    System.out.println("文档批量新增，创建结果：" + saveResult);
  }

  /**
   * 根据文档id判断文档是否存在
   */
  @Test
  public void existsDocument() {
    boolean result = elasticsearchRestTemplate.exists("1001", Product.class);
    System.out.println("文档是否存在：" + result);
  }

  /**
   * 根据文档id删除文档
   */
  @Test
  public void deleteDocument() {
    String result = elasticsearchRestTemplate.delete("j07wvIQBG2vb63j9iSAY", Product.class);
    System.out.println("根据文档id删除，结果：：" + result);
  }

  /**
   * 根据查询条件批量删除文档
   */
  @Test
  public void deleteDocumentByQueryCondition() {
    QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("小米");
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQueryBuilder);
    elasticsearchRestTemplate.delete(nativeSearchQuery, Product.class);
    System.out.println("根据查询条件批量删除文档!!!");
  }

  /**
   * 批量修改文档
   */
  @Test
  public void updateDocumentList() {
    List<Product> list = new ArrayList<>();
    Product product01 = new Product();
    product01.setId("1003");
    product01.setName("一加 T3");
    product01.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product01.setPrice(4688.88);
    product01.setCategory("手机");
    list.add(product01);
    Product product03 = new Product();
    product03.setId("1004");
    product03.setName("一加 T3 PRO");
    product03.setImages("F:\\pictures\\yy测试下载图片.jpg");
    product03.setPrice(5499.99);
    product03.setCategory("手机");
    list.add(product03);
    Iterable<Product> saveResult = elasticsearchRestTemplate.save(list);
    System.out.println("批量修改文档，结果：" + saveResult);
  }

  /**
   * 局部修改文档
   */
  @Test
  public void updateDocument() {
    //ctx._source 为固定内容，name、price 为文档属性名称（字段名）
    String script = "ctx._source.name='小米 S12 PRO';ctx._source.price=3999.99";
    //根据文档id修改文档指定内容
    UpdateQuery updateQuery = UpdateQuery.builder("1001").withScript(script).build();
    IndexCoordinates indexCoordinates = IndexCoordinates.of("product");
    UpdateResponse updateResponse = elasticsearchRestTemplate.update(updateQuery, indexCoordinates);
    System.out.println("局部修改文档，结果：" + updateResponse);
  }

  /**
   * 查询文档 -- 查询全部
   */
  @Test
  public void searchDocumentMatchAll() {
    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(matchAllQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，查询全部，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，查询全部，结果集：" + list);
  }

  /**
   * 查询文档 -- 根据文档id查询
   */
  @Test
  public void searchDocumentById() {
    Product product = elasticsearchRestTemplate.get("1001", Product.class);
    System.out.println("查询文档，根据文档id查询，结果集：" + product);
  }

  /**
   * 查询文档 -- 模糊查询
   */
  @Test
  public void searchDocumentLike() {
    //QueryBuilders.queryStringQuery("小米") 这种查询方式不指定匹配的字段，默认根据文档中除id外的第一个字段进行匹配，并且只会通过这一个字段进行匹配，而且查询条件会被分词，根据分词后的结果进行匹配
    QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("小米");
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，模糊查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，模糊查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 使用 match 查询 -- 模糊查询
   */
  @Test
  public void searchDocumentMatch() {
    //QueryBuilders.matchQuery("name", "小米") 指定具体的匹配字段，查询条件会被分词，根据分词后的结果进行匹配
    MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "小米");
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(matchQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，使用 match 查询，模糊查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，使用 match 查询，模糊查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 使用 match 查询 -- 模糊查询之短语搜索
   * 备注：短语搜索是对条件不分词，但是文档中属性根据配置实体类时指定的分词类型进行分词，如果属性使用ik分词器，从分词后的索引数据中进行匹配。
   */
  @Test
  public void searchDocumentMatchPhrase() {
    //QueryBuilders.matchPhraseQuery("name", "小米") 指定具体的匹配字段为 name，查询条件不分词，直接匹配
    MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("name", "小米");
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(matchPhraseQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，使用 match 查询，模糊查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，使用 match 查询，模糊查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 区间查询
   * 备注：短语搜索是对条件不分词，但是文档中属性根据配置实体类时指定的分词类型进行分词，如果属性使用ik分词器，从分词后的索引数据中进行匹配。
   */
  @Test
  public void searchDocumentRange() {
    //QueryBuilders.rangeQuery("price") 根据 price 字段进行区间查询。gte()：大于等于，lte()：小于等于，gt()：大于，lt()：小于。
    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(4688.88D).lte(6000D);//gte(4688.88D) 和 gte(4688.88) 效果相同
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(rangeQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，区间查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，区间查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 简单多条件查询
   * 实例：查询商品名称中包含 “米” 字，且价格大于等于 2555.88 的所有商品
   */
  @Test
  public void searchDocumentMoreCondition() {
    //QueryBuilders.rangeQuery("price") 根据 price 字段进行区间查询。gte()：大于等于，lte()：小于等于，gt()：大于，lt()：小于。
    List<QueryBuilder> queryBuilderList = new ArrayList<>();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    queryBuilderList.add(QueryBuilders.matchQuery("name","米"));//商品名称 中要包含 “米” 字
    queryBuilderList.add(QueryBuilders.rangeQuery("price").gte(2555.88));//商品价格 要大于等于 2555.88
//    boolQueryBuilder.must().addAll(queryBuilderList);//逻辑 与（and -- 多个条件都要满足）
    boolQueryBuilder.should().addAll(queryBuilderList);//逻辑 或（or -- 多个条件满足其一即可）
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，简单多条件查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，简单多条件查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 复杂多条件查询
   * 实例：查询商品名称包含 “小米” 或者 “一加”，并且商品类型为 “手机” 的所有商品
   */
  @Test
  public void searchDocumentMoreConditionOther() {
    //封装第一个查询条件
    List<QueryBuilder> queryBuilderList01 = new ArrayList<>();
    BoolQueryBuilder boolQueryBuilder01 = QueryBuilders.boolQuery();
    queryBuilderList01.add(QueryBuilders.matchQuery("name","小米"));//商品名称 中要包含 “小米” 关键字
    queryBuilderList01.add(QueryBuilders.matchQuery("category","手机"));//商品类型 要是 “手机”
    boolQueryBuilder01.must().addAll(queryBuilderList01);//逻辑 与（and -- 多个条件都要满足）
    //封装第二个查询条件
    List<QueryBuilder> queryBuilderList02 = new ArrayList<>();
    BoolQueryBuilder boolQueryBuilder02 = QueryBuilders.boolQuery();
    queryBuilderList02.add(QueryBuilders.matchQuery("name","一加"));//商品名称 中要包含 “一加” 关键字
    queryBuilderList02.add(QueryBuilders.matchQuery("category","手机"));//商品类型 要是 “手机”
    boolQueryBuilder02.must().addAll(queryBuilderList02);//逻辑 与（and -- 多个条件都要满足）
    //将 条件一 和 条件二 封装到 条件三 中，再使用 条件三 作为最后的查询条件
    List<QueryBuilder> queryBuilderList03 = new ArrayList<>();
    BoolQueryBuilder boolQueryBuilder03 = QueryBuilders.boolQuery();
    queryBuilderList03.add(boolQueryBuilder01);
    queryBuilderList03.add(boolQueryBuilder02);
    boolQueryBuilder03.should().addAll(queryBuilderList03);//逻辑 或（or -- 多个条件满足其一即可）
    //使用 条件三 作为最后的查询条件进行查询
    NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder03);
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，复杂多条件查询，查询总数：" + searchHits.getTotalHits());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，复杂多条件查询，结果集：" + list);
  }

  /**
   * 查询文档 -- 分页与排序
   * 实例：查询商品名称中包含 “米” 字，且价格大于等于 2555.88 的所有商品
   */
  @Test
  public void searchDocumentPageAndOrder() {
    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    Query query = new NativeSearchQuery(matchAllQueryBuilder);
    //排序（Direction.ASC：升序；Direction.DESC：降序）
    query.addSort(Sort.by(Direction.ASC,"id"));
    //分页（起始页：0）
    query.setPageable(PageRequest.of(0,2));
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(query, Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，分页与排序，查询总数：" + searchHits.getTotalHits() + "，当前页的数量：" + searchHits.getSearchHits().size());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，分页与排序，结果集：" + list);
  }

  /**
   * 查询文档 -- 去重
   * 实例：查询商品名称中包含 “米” 字，且价格大于等于 2555.88 的所有商品
   * 注意：去重的字段不能是 text 类型
   */
  @Test
  public void searchDocumentCollapse() {
    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.must(matchAllQueryBuilder);
    nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
    //根据商品类型 category 进行去重，去重的字段一定不能是 text 类型
    nativeSearchQueryBuilder.withCollapseField("category");
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), Product.class);
    List<Product> list = new ArrayList<>();
    System.out.println("查询文档，去重，查询总数：" + searchHits.getTotalHits() + "，去重后的数量：" + searchHits.getSearchHits().size());
    searchHits.forEach(sh -> {
      list.add(sh.getContent());
    });
    System.out.println("查询文档，去重，结果集：" + list);
  }

  /**
   * 查询文档 -- 分组聚合
   * 实例：根据商品类型进行分类，查询所有类型对应的商品数量
   * 注意：作为聚合的字段不能是 text 类型
   */
  @Test
  public void searchDocumentAggregations() {
    MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.must(matchAllQueryBuilder);
    nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
    //AggregationBuilders.terms("product_count") 中 product_count 是分组后的结果集对应的 key，field("category") 中的 category 为分组的字段
    nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("product_count").field("category"));
    SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), Product.class);
    Aggregations aggregations = searchHits.getAggregations();//分组聚合后的结果集对象
    ParsedStringTerms product_count = aggregations.get("product_count");
    Map<String,Long> map = new HashMap<>();
    System.out.println("查询文档，分组聚合，分组聚合后的结果集对象：" + product_count);
    for (Terms.Bucket bucket: product_count.getBuckets()) {
      map.put(bucket.getKeyAsString(),bucket.getDocCount());
    }
    System.out.println("查询文档，分组聚合，结果集：" + map);
  }

}
