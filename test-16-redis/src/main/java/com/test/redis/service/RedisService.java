package com.test.redis.service;

public interface RedisService {

  String goodsReduce();

  String goodsReduceAddLock();

  String goodsReduceDistLockOne();

  String goodsReduceDistLockTwo();

  String goodsReduceDistLockThree();

  String goodsReduceDistLockFour();

  String goodsReduceDistLockFive();

}