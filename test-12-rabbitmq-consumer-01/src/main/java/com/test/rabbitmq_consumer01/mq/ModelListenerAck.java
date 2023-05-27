package com.test.rabbitmq_consumer01.mq;

import com.rabbitmq.client.Channel;
import com.test.rabbitmq_consumer01.base.BaseRabbiMqHandler;
import com.test.rabbitmq_consumer01.base.MqListener;
import com.test.rabbitmq_consumer01.config.RabbitmqConfig;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;

/**
 * RabbitMQ 消费者消息确认机制，利用消费者参数Channel 进行消息确认操作
 * channel.basicAck()：消息确认
 * channel.basicNack()：消息回退
 * channel.basicReject()：消息拒绝
 */
@Configuration
public class ModelListenerAck extends BaseRabbiMqHandler<String> {

    /**
     * 封装rabbitmq消息确认、异常回退类 -- 之前
     * 简单模式（Simple）+ ack消费者消息确认机制
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "simple.queue",durable = "true")) // queuesToDeclare 自动声明队列
    public void simpleReceive(String msg, Channel channel, Message message) throws IOException {
        try {
            // 消息
            if (msg.contains("发送第 1 条消息") || msg.contains("发送第 2 条消息") || msg.contains("发送第 3 条消息")) {
                System.out.println("consumer-01，简单模式（Simple）+ ack消费者消息确认机制，正常接收到的消息：" + msg);
            } else if (msg.contains("发送第 8 条消息") || msg.contains("发送第 9 条消息") || msg.contains("发送第 10 条消息")) {
                throw new RuntimeException("人为制造一个异常");
            } else {
                System.out.println("consumer-01，简单模式（Simple）+ ack消费者消息确认机制，拒绝接收的消息：" + msg);
                /**
                 * 消息拒绝：与 basicNack 区别在于不能进行批量操作，其他用法很相似
                 * deliveryTag：表示消息投递序号。
                 * requeue：值为 true 消息将重新入队列，消息重入队列之后会依然会被监听消费。
                 */
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("consumer-01，简单模式（Simple）+ ack消费者消息确认机制，消息消费过程中发生异常，将此消息重入队列。消息内容：" + msg);
            /**
             * deliveryTag：表示消息投递序号。
             * multiple：是否批量确认。
             * requeue：值为 true 消息将重新入队列，消息重入队列之后会依然会被监听消费。
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
        /**
         * 消息成功消费之后确认
         * deliveryTag：表示消息投递序号，每次消费消息或者消息重新投递后，deliveryTag都会增加
         * multiple：是否批量确认，值为 true 则会一次性 ack所有小于当前消息 deliveryTag 的消息。
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 封装rabbitmq消息确认、异常回退类 -- 之后
     * 简单模式（Simple）+ ack消费者消息确认机制
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "simple.queue",durable = "true")) // queuesToDeclare 自动声明队列
    public void simpleReceive1(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(msg, deliveryTag, channel, new MqListener() {
            @Override
            public void handler(Object map, Channel channel) throws IOException {
                if (msg.contains("发送第 1 条消息") || msg.contains("发送第 2 条消息") || msg.contains("发送第 3 条消息")) {
                    System.out.println("consumer-01，简单模式（Simple）+ ack消费者消息确认机制，正常接收到的消息：" + msg);
                } else if (msg.contains("发送第 8 条消息") || msg.contains("发送第 9 条消息") || msg.contains("发送第 10 条消息")) {
                    throw new RuntimeException("人为制造一个异常");
                } else {
                    System.out.println("consumer-01，简单模式（Simple）+ ack消费者消息确认机制，拒绝接收的消息：" + msg);
                }
            }
        });
    }

}
