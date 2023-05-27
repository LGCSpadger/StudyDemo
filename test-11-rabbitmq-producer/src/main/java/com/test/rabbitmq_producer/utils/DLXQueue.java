package com.test.rabbitmq_producer.utils;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DLXQueue {

  // routingKey
  private static final String DEAD_ROUTING_KEY = "dead.routingkey";
  private static final String ROUTING_KEY = "common.routingkey";
  private static final String DEAD_EXCHANGE = "dead.exchange";
  private static final String EXCHANGE = "common.exchange";

  @Resource
  RabbitTemplate rabbitTemplate;

  @Resource
  RabbitAdmin rabbitAdmin;

  /**
   * 死信队列
   * 发送消息，消息过期后进入死信交换机，交由死信队列处理
   *
   * @param queueName     队列名称
   * @param deadQueueName 死信队列名称
   * @param params        消息内容
   * @param expiration    过期时间 毫秒
   */
  public void sendDLXQueue(String queueName, String deadQueueName, Object params, Integer expiration) {
    //先创建一个ttl队列和死信队列
    Map<String, Object> map = new HashMap<>();
    // 队列设置存活时间，单位ms,必须是整形数据。
    map.put("x-message-ttl", expiration);
    // 设置死信交换机
    map.put("x-dead-letter-exchange", DEAD_EXCHANGE);
    // 设置死信交换机路由键
    map.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);

    //创建队列并指定该队列绑定的死信交换机，参数1：队列名称  参数2：持久化  参数3：是否排他 参数4：自动删除队列  参数5：队列参数
    Queue queue = new Queue(queueName, true, false, false, map);
    rabbitAdmin.declareQueue(queue);
    //创建交换机
    DirectExchange directExchange = new DirectExchange(EXCHANGE, true, false);
    rabbitAdmin.declareExchange(directExchange);
    //队列绑定交换机
    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(ROUTING_KEY);
    rabbitAdmin.declareBinding(binding);

    //创建死信队列
    Queue deadQueue = new Queue(deadQueueName, true, false, false);
    rabbitAdmin.declareQueue(deadQueue);
    //创建死信交换机
    DirectExchange deadExchange = new DirectExchange(DEAD_EXCHANGE, true, false);
    rabbitAdmin.declareExchange(deadExchange);
    //死信队列绑定死信交换机
    Binding deadbinding = BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    rabbitAdmin.declareBinding(deadbinding);

    // 发送消息
    rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, params);
  }
}
