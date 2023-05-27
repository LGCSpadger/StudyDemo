package com.test.rabbitmq_consumer01.mq;

import com.test.rabbitmq_consumer01.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 常用模式测试类 -- 消费者
 */
@Configuration
public class ModelListener {

    /**
     * 简单模式（Simple）
     * 一对一消费，一条消息只有一个消费者可以消费
     * 备注：
     * 1、一个队列中的一条消息只会被一个消费者消费一次
     * 2、简单模式中，使用默认交换机direct
     */
//    @RabbitListener(queues = {RabbitmqConfig.SIMPLE_QUEUE})//监听 RabbitmqConfig.SIMPLE_QUEUE 队列
//    public void simpleReceive(String message) {
//        System.out.println("consumer-01，简单模式（Simple），接收到的消息：" + message);
//    }

    /**
     * 工作队列模式（Work Queues）
     * 多个消费者，你一个我一个分配消费消息，有预取机制，默认公平消费（轮询策略），可配置能者多劳模式，谁完成的快，谁多做一点
     * 备注：一个队列中的一条消息只会被一个消费者消费一次
     */
    @RabbitListener(queues = {RabbitmqConfig.WORK_QUEUES})
    public void workQueuesReceive(String message) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("consumer-01，工作队列模型（Work Queues），消费者，接收到的消息：" + message);
    }

    /**
     * 发布订阅模式（Publish/Subscribe）
     * 发布订阅模式使用 fanout 类型的交换机，将接收到的消息路由到每一个跟其绑定的queue(队列)
     * 备注：
     * 1、生产者将消息发送给交换机，并指定路由规则
     * 2、交换机将收到的所有消息按照路由规则转发到跟当前交换机绑定的所有队列上（每个和当前交换机绑定的队列都会收到消息，然后被消费者消费，也就是说一条消息可以被多个消费者同时消费）
     */
    @RabbitListener(queues = {RabbitmqConfig.PUBLISH_SUBSCRIBE_QUEUES_1})
    public void publishSubscribeQueuesReceive(String message) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("consumer-01，发布订阅模型（Publish/Subscribe），消费者一*绑定队列*PUBLISH_SUBSCRIBE_QUEUES_1，接收到的消息：" + message);
    }

    /**
     * 路由模式（Routing）
     * Routing模式 使用 Direct 类型的交换机，将接收到的消息路由到每一个跟其绑定的queue(队列)
     * 备注：
     * 1、生产者将消息发送给交换机，并指定路由规则
     * 2、交换机将收到的所有消息按照路由规则转发到跟当前交换机绑定的所有队列上（每个和当前交换机绑定的队列都会收到消息，然后被消费者消费，也就是说一条消息可以被多个消费者同时消费）
     * 3、路由模式不支持使用通配符，convertAndSend() 方法中指定的 routingKey 和队列绑定交换机时 with() 方法中配置的 routingKey 必须完全匹配才能将消息发送到具体的队列上
     */
    @RabbitListener(queues = {RabbitmqConfig.ROUTING_QUEUES_SMS})//queues需要手动先创建队列
    public void routingQueuesReceive(String message) {
        System.out.println("consumer-01，路由模式（Routing），消费者一*绑定队列*ROUTING_QUEUES_SMS，接收到的消息：" + message);
    }

    /**
     * 主题模式（Topic，也叫通配符模式）
     * Topic模式 使用 topic 类型的交换机，将接收到的消息路由到每一个跟其绑定的queue(队列)
     * 备注：
     * 1、生产者将消息发送给交换机，并指定路由规则
     * 2、交换机将收到的所有消息按照路由规则转发到跟当前交换机绑定的所有队列上（每个和当前交换机绑定的队列都会收到消息，然后被消费者消费，也就是说一条消息可以被多个消费者同时消费）
     * 3、主题模式 相比于 路由模式 增加了通配符的使用
     * RabbitMQ 通配符有哪些：
     * 1、 # ：代表没有或一个或多个单词（不是一个字符哦！单词与单词之间用“.”分割）；
     * 2、 * ：代表没有或一个英文单词（不是一个字符哦）；
     * 通配符具体使用案例：
     * 1、topic.queues.sms.#     能匹配     topic.queues.sms、topic.queues.sms.111（111 可以换成任何字符）
     * 2、topic.queues.#.email       能匹配     topic.queues.email、topic.queues.111.email（111 可以换成任何字符）
     */
    @RabbitListener(queues = {RabbitmqConfig.TOPIC_QUEUES_SMS})
    public void topicQueuesReceive(String message) {
        System.out.println("consumer-01，主题模式（Topic，也叫通配符模式），消费者一*绑定队列*TOPIC_QUEUES_SMS，接收到的消息：" + message);
    }

}
