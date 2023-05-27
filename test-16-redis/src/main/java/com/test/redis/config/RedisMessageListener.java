package com.test.redis.config;

import com.test.redis.dto.MessageDto;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 自定义消息监听器
 * 注意：
 * 1、自定义消息监听器需要实现 MessageListener 接口，重写里面的 onMessage 方法
 */
@Slf4j
@Component
public class RedisMessageListener implements MessageListener {

  @Resource
  private RedisTemplate redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String channel = new String(pattern);
    log.info("RedisMessageListener，监听的频道：{}，接收到的消息内容:{}",channel,message);
    //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
    MessageDto messageDto = (MessageDto) redisTemplate.getValueSerializer().deserialize(message.getBody());
    log.info("RedisMessageListener，反序列化得到消息对象:{}",messageDto);
    // ---------  监听到消息后具体的业务逻辑操作  -------- start --------
    Object value = redisTemplate.opsForValue().get(channel);
    log.info("RedisMessageListener，key:{}，存之前的value：{}",channel,value);
    //将接收到的消息存入redis中
    redisTemplate.opsForValue().set(channel,messageDto);
    Object valueOther = redisTemplate.opsForValue().get(channel);
    log.info("RedisMessageListener，key:{}，存之后的value：{}",channel,valueOther);
    // ---------  监听到消息后具体的业务逻辑操作  -------- end --------
  }

}