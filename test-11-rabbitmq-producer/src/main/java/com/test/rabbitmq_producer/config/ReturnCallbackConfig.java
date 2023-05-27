package com.test.rabbitmq_producer.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 实现发送消息回调接口
 * 如果消息未能投递到目标queue里将触发回调 returnCallback ，一旦向 queue 投递消息未成功，这里一般会记录下当前消息的详细投递数据，方便后续做重发或者补偿等操作。
 */
@Component
public class ReturnCallbackConfig implements RabbitTemplate.ReturnsCallback {

  @Resource
  private RabbitTemplate rabbitTemplate;

  // @PostContruct是spring框架的注解，在⽅法上加该注解会在项⽬启动的时候执⾏该⽅法，也可以理解为在spring容器初始化的时候执行
  @PostConstruct
  public void init(){
    rabbitTemplate.setReturnsCallback(this);
  }

  @Override
  public void returnedMessage(ReturnedMessage returnedMessage) {
    System.out.println("消息--" + returnedMessage.getMessage().toString() + "--被交换机--" + returnedMessage.getExchange() + "--回退！"
         + "退回原因为："+returnedMessage.getReplyText());
    // 回退了所有的信息，可做补偿机制
  }
}

