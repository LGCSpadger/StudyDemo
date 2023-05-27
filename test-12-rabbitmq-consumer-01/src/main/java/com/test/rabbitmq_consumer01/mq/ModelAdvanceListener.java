package com.test.rabbitmq_consumer01.mq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 进阶模式测试类 -- 消费者
 */
@Configuration
public class ModelAdvanceListener {

  /**
   * 延迟队列（Delayed Queue）
   */
  @RabbitListener(queuesToDeclare = @Queue(value = "delayed.queue",durable = "ture"))
  public void delayedQueueReceive(String message) {
    System.out.println("consumer-01，延迟队列（Delayed Queue），接收到的消息：" + message);
  }

  /**
   * TTL队列
   */
  @RabbitListener(queues = "TTL.queue")
  public void ttlQueueReceive(String message) {
    System.out.println("consumer-01，TTL队列，接收到的消息：" + message);
  }

  /**
   * 死信队列
   */
  @RabbitListener(queuesToDeclare = @Queue(value = "dead.queue",durable = "ture"))
  public void deadQueueReceive(String message) {
    System.out.println("consumer-01，死信队列，接收到的消息：" + message);
  }

}
