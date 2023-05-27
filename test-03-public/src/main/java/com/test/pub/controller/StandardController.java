package com.test.pub.controller;

import com.test.common.response.CommonCode;
import com.test.common.response.QueryResponseResult;
import com.test.common.response.ResponseResult;
import com.test.pub.annotate.RequestLimit;
import com.test.pub.entity.Article;
import com.test.pub.entity.Author;
import com.test.pub.service.StandardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/standardApi")
@Api(value = "/standardApi", description = "标准接口Api")
public class StandardController {

  @Autowired
  private StandardService standardService;

  //Semaphore一般用于流量的控制，特别是公共资源有限的应用场景。例如数据库的连接，假设数据库的连接数上线为10个，多个线程并发操作数据库可以使用Semaphore来控制并发操作数据库的线程个数最多为10个。
  //定义semaphore实例，设置许可数为1，即只能有一个线程能进来
  final Semaphore semaphore = new Semaphore(1);

  @ApiOperation("文章信息保存")
  @PostMapping(value="/saveArticle")
  public ResponseResult saveArticle(@RequestBody Article article) {
    try {
      if (semaphore.availablePermits() > 0) {
        //尝试获取许可，成功获得许可之后，代码才能继续往下执行
        semaphore.acquire();
        ResponseResult result = standardService.saveArticle(article);
        return result;
      } else {
        return new ResponseResult(CommonCode.SERVER_ERROR);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseResult(CommonCode.FAIL);
    } finally {
      //释放许可，当前线程用完之后必须要释放许可，其他线程才能进来
      semaphore.release();
    }
  }

  @ApiOperation("查询作者列表")
  @PostMapping(value="/queryAuthorList/{page}/{size}")
  public QueryResponseResult<Author> queryAuthorList(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody Author author) {
    QueryResponseResult<Author> result = standardService.queryAuthorList(page, size, author);
    return result;
  }

  @ApiOperation("查询作者及作者写的小说列表")
  @PostMapping(value="/queryAuthorAndArticleList/{page}/{size}")
  public QueryResponseResult<Author> queryAuthorAndArticleList(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody Author author) {
    QueryResponseResult<Author> result = standardService.queryAuthorAndArticleList(page, size, author);
    return result;
  }

  @ApiOperation("文章信息批量保存")
  @PostMapping(value="/saveBatchArticle")
  public ResponseResult saveBatchArticle(@RequestBody List<Article> datas) {
    ResponseResult result = standardService.saveBatchArticle(datas);
    return result;
  }

  @ApiOperation("文章信息修改")
  @PostMapping(value="/updateArticle")
  public ResponseResult updateArticle(@RequestBody Article article) {
    ResponseResult result = standardService.updateArticle(article);
    return result;
  }

  @ApiOperation("文章信息删除")
  @PostMapping(value="/deleteArticle")
  public ResponseResult deleteArticle(@RequestBody Article article) {
    ResponseResult result = standardService.deleteArticle(article);
    return result;
  }

  @ApiOperation("文章列表查询--分页")
  @PostMapping(value="/queryDataList/{page}/{size}")
  public QueryResponseResult<Article> queryDataList(@PathVariable("page") int page,@PathVariable("size") int size,
      @RequestBody Article article) {
    QueryResponseResult<Article> result = standardService.queryDataList(page, size, article);
    return result;
  }

  @ApiOperation("接口防刷测试")
  @GetMapping(value="/testPrevent")
  @RequestLimit(seconds = 20, maxCount = 2)//指定此接口同一个用户在20秒内只能访问2次
  public ResponseResult testPrevent() {
    ResponseResult result = new ResponseResult(CommonCode.SUCCESS);
    result.setMessage("接口防刷测试");
    log.info("接口防刷测试：{}",result);
    return result;
  }

}