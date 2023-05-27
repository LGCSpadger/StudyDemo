package com.test.rabbitmq_producer.utils;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DelayedQueue {

  // routingKey
  private static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
  // 延迟队列交换机
  private static final String DELAYED_EXCHANGE = "delayed.exchange";

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Resource
  RabbitAdmin rabbitAdmin;

  /**
   * 发送延迟队列
   * @param queueName 队列名称
   * @param params 消息内容
   * @param expiration 延迟时间 毫秒
   */
  public void sendDelayedQueue(String queueName, Object params, Integer expiration) {
    // 先创建一个队列
    Queue queue = new Queue(queueName);
    rabbitAdmin.declareQueue(queue);
    // 创建延迟队列交换机
    CustomExchange customExchange = createCustomExchange();
    rabbitAdmin.declareExchange(customExchange);
    // 将队列和交换机绑定
    Binding binding = BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
    rabbitAdmin.declareBinding(binding);
    // 发送延迟消息
    rabbitTemplate.convertAndSend(DELAYED_EXCHANGE, DELAYED_ROUTING_KEY, params, msg -> {
      // 发送消息的时候 延迟时长
      msg.getMessageProperties().setDelay(expiration);
      return msg;
    });
  }

  public CustomExchange createCustomExchange() {
    Map<String, Object> arguments = new HashMap<>();
    /**
     * 参数说明：
     * 1.交换机的名称
     * 2.交换机的类型
     * 3.是否需要持久化
     * 4.是否自动删除
     * 5.其它参数
     */
    arguments.put("x-delayed-type", "direct");
    return new CustomExchange(DELAYED_EXCHANGE,"x-delayed-message", true, false, arguments);
  }

}

