package com.test.redis.controller;

import com.test.redis.service.RedisService;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="redis管理",tags = {"redis管理"})
@RestController
@RequestMapping("redis")
public class RedisController {

  @Resource
  private RedisService redisService;

  @GetMapping("/goodsReduce")
  public String goodsReduce() {
    return redisService.goodsReduce();
  }

  @GetMapping("/goodsReduceAddLock")
  public String goodsReduceAddLock() {
    return redisService.goodsReduceAddLock();
  }

  @GetMapping("/goodsReduceDistLockOne")
  public String goodsReduceDistLockOne() {
    return redisService.goodsReduceDistLockOne();
  }

  @GetMapping("/goodsReduceDistLockTwo")
  public String goodsReduceDistLockTwo() {
    return redisService.goodsReduceDistLockTwo();
  }

  @GetMapping("/goodsReduceDistLockThree")
  public String goodsReduceDistLockThree() {
    return redisService.goodsReduceDistLockThree();
  }

  @GetMapping("/goodsReduceDistLockFour")
  public String goodsReduceDistLockFour() {
    return redisService.goodsReduceDistLockFour();
  }

  @GetMapping("/goodsReduceDistLockFive")
  public String goodsReduceDistLockFive() {
    return redisService.goodsReduceDistLockFive();
  }

}