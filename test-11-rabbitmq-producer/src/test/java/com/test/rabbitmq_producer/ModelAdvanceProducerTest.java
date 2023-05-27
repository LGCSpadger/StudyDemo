package com.test.rabbitmq_producer;

//import com.test.rabbitmq_producer.config.DelayQueueConfig;
import com.test.rabbitmq_producer.utils.DLXQueue;
import com.test.rabbitmq_producer.utils.DelayedQueue;
import com.test.rabbitmq_producer.utils.TtlQueue;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RabbitMQ 进阶模式测试类 -- 生产者
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ModelAdvanceProducerTest {

  @Resource
  private DelayedQueue delayedQueue;

  @Resource
  private TtlQueue ttlQueue;

  @Resource
  private DLXQueue dlxQueue;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /**
   * 延迟队列（Delayed Queue）
   */
  @Test
  public void delayedQueueTest() {
    delayedQueue.sendDelayedQueue("delayed.queue","重新发送消息到延迟队列delayed.queue上",10000);
  }

  /**
   * TTL队列
   */
  @Test
  public void ttlQueueTest() {
    ttlQueue.sendTtlQueue("TTL.queue","发送消息到 TTL 队列上",12000000);
  }

  /**
   * 死信队列
   */
  @Test
  public void dlxQueueTest() {
    dlxQueue.sendDLXQueue("common.queue","dead.queue","发送消息到死信队列上",30000);
  }

}
