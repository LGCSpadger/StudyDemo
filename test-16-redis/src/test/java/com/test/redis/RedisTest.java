package com.test.redis;

//import com.test.redis.service.RedisService;
//import com.test.redis.utils.RedisUtils;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class RedisTest {

//    @Autowired
//    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private RedisService redisService;

    //向redis中添加数据
    @Test
    public void test01() {
//        redisTemplate.delete("goods:001");
        redisTemplate.opsForValue().set("goods:002",String.valueOf(100));
        System.out.println(redisTemplate.opsForValue().get("goods:002"));
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("ttt", "kkk", Duration.ofSeconds(30));
    }

    //循环查询数据测试
    @Test
    public void test02() {
        redisTemplate.delete("TEST_TOPIC1");
    }

    //向redis中添加数据
    @Test
    public void test03() {
        Object test_topic_01 = redisTemplate.opsForValue().get("TEST_TOPIC_01");
        log.info("test_topic_01: {}",test_topic_01);
        Object test_topic_02 = redisTemplate.opsForValue().get("TEST_TOPIC_02");
        log.info("test_topic_02: {}",test_topic_02);
    }


}
