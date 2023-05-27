package com.test.rabbitmq_consumer01.base;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class BaseRabbiMqHandler<T> {

  public void onMessage(T t, Long deliveryTag, Channel channel, MqListener mqListener) {
    try {
      // 通过实现这个接口，处理我们的业务代码
      mqListener.handler(t, channel);
      /**
       * 手动确认消息
       * deliveryTag:该消息的index
       * multiple：是否批量确认，false只确认当前一个消费者收到消息，true确认所有consumer获得的消息（成功消费，消息从队列中删除 ）
       */
      channel.basicAck(deliveryTag, false);
    } catch (Exception e) {
      log.error("接收消息失败,重新放回队列,异常原因：{}",e.getMessage());
      try {
        /**
         * 重回队列
         * deliveryTag:该消息的index
         * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
         * requeue：值为 true 消息将重新入队列，消息重入队列之后会依然会被监听消费。
         */
        channel.basicNack(deliveryTag, false, true);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

  }
}

