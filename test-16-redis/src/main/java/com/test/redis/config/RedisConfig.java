package com.test.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  final String TOPIC_NAME1 = "TEST_TOPIC_01"; // 订阅主题
  final String TOPIC_NAME2 = "TEST_TOPIC_02"; // 订阅主题

  //编写配置类，可模仿RedisAutoConfiguration配置类，该类在开发中可直接使用
  @Bean
  @SuppressWarnings("all")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    //由于源码autoConfig中是<Object, Object>，开发中一般直接使用<String,Object>
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
    redisTemplate.setConnectionFactory(factory);

    //Json序列化配置
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper om = new ObjectMapper();
    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);

    //String的序列化
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    //key采用string的序列化
    redisTemplate.setKeySerializer(stringRedisSerializer);
    //hash的key采用string的序列化
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    //value序列化采用jackson
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    //hash的value序列化方式采用jackson
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  /**
   * 配置 Redis消息监听器容器
   * 1、一个消息监听容器可以配置多个消息监听器，实现多个队列的消息监听
   *
   * @param redisConnectionFactory 连接工厂
   * @param listener               消息监听器
   * @return redis消息监听容器
   */
  @Bean
  @SuppressWarnings("all")
  public RedisMessageListenerContainer container(
      RedisConnectionFactory redisConnectionFactory,
      RedisMessageListener listener
  ) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    // 监听所有库的key过期事件
    container.setConnectionFactory(redisConnectionFactory);
    //将 TOPIC_NAME1 加入监听容器
    container.addMessageListener(listener, new PatternTopic(TOPIC_NAME1));
    //将 TOPIC_NAME2 加入监听容器
    container.addMessageListener(listener, new PatternTopic(TOPIC_NAME2));

    return container;
  }

}