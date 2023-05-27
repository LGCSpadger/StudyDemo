package com.test.rabbitmq_producer.utils;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TtlQueue {

  // routingKey
  private static final String TTL_KEY = "ttl.routingkey";
  // 交换机
  private static final String TTL_EXCHANGE = "ttl.exchange";

  @Resource
  RabbitTemplate rabbitTemplate;

  @Resource
  RabbitAdmin rabbitAdmin;

  /**
   * TTL队列
   * @param queueName 队列名称
   * @param params 消息内容
   * @param expiration 过期时间 毫秒
   */
  public void sendTtlQueue(String queueName, Object params, Integer expiration) {
    //创建一个ttl队列
    Map<String, Object> map = new HashMap<>();
    // 队列设置存活时间，单位ms,必须是整形数据。
    map.put("x-message-ttl",expiration);
    /*参数1：队列名称  参数2：持久化  参数3：是否排他 参数4：自动删除队列  参数5：队列参数*/
    Queue queue = new Queue(queueName,true,false,false,map);
    rabbitAdmin.declareQueue(queue);
    //创建交换机
    DirectExchange directExchange = new DirectExchange(TTL_EXCHANGE, true, false);
    rabbitAdmin.declareExchange(directExchange);
    // 将队列和交换机绑定
    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(TTL_KEY);
    rabbitAdmin.declareBinding(binding);
    // 发送消息
    rabbitTemplate.convertAndSend(TTL_EXCHANGE,TTL_KEY,params);
  }
}

