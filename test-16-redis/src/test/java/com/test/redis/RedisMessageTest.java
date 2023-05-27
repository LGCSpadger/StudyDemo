package com.test.redis;

import com.test.redis.dto.MessageDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisMessageTest {

  @Autowired
  private RedisTemplate redisTemplate;

  final String TOPIC_NAME1 = "TEST_TOPIC_01"; // 订阅主题
  final String TOPIC_NAME2 = "TEST_TOPIC_02"; // 订阅主题

  @Test
  public void test01(){
    MessageDto dto1 = new MessageDto();
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter timeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    dto1.setData(timeFormatter1.format(now));
    dto1.setTitle("斗破苍穹");
    dto1.setContent("三上云岚宗，吊打云山老狗。。。。。。");
    MessageDto dto2 = new MessageDto();
    DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    dto2.setData(timeFormatter2.format(now));
    dto2.setTitle("画江湖之不良人");
    dto2.setContent("朝堂争斗，天子为爱弃江山！！！");
    //向两个频道发布消息
    redisTemplate.convertAndSend(TOPIC_NAME1, dto1);
    redisTemplate.convertAndSend(TOPIC_NAME2, dto2);
  }

  @Test
  public void test02(){

  }

  @Test
  public void test03(){
    redisTemplate.delete(TOPIC_NAME1);
    redisTemplate.delete(TOPIC_NAME2);
  }

}

