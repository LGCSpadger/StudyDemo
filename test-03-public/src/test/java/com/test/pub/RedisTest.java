package com.test.pub;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Redis相关测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class RedisTest {

  @Autowired
  private RedisTemplate redisTemplate;

  //判断set集合是否包含
  @Test
  public void test01() {
    redisTemplate.opsForValue().set("test-key-001","I am a test value",30, TimeUnit.MINUTES);
    System.out.println(redisTemplate.opsForValue().get("test-key-001"));
  }

}
