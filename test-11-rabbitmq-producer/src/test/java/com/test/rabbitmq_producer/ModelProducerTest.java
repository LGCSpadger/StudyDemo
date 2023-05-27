package com.test.rabbitmq_producer;

import com.test.common.utils.TimeFormatUtil;
import com.test.rabbitmq_producer.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RabbitMQ 常用模式测试类 -- 生产者
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ModelProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 简单模式（Simple）
     * 一对一消费，一条消息只有一个消费者可以消费
     * 备注：
     * 1、一个队列中的一条消息只会被一个消费者消费一次
     * 2、简单模式中，使用默认交换机direct
     */
    @Test
    public void simpleSendMessageTest() {
        for (int i = 1; i <= 10; i++) {
            String currTime = TimeFormatUtil.getTimeByMs(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            //要发送的消息内容
            String message = "hello，简单模式（Simple），发送第 " + i + " 条消息，发送时间：" + currTime;
            rabbitTemplate.convertAndSend(RabbitmqConfig.SIMPLE_QUEUE,message);
            System.out.println("简单模式（Simple），生产者，发送第 " + i + " 条消息，发送的消息内容：" + message);
        }
    }

    /**
     * 工作队列模式（Work Queues）
     * 多个消费者，你一个我一个分配消费消息，有预取机制，默认公平消费（轮询策略），可配置能者多劳模式，谁完成的快，谁多做一点
     * 备注：一个队列中的一条消息只会被一个消费者消费一次
     */
    @Test
    public void workQueuesSendMessageTest() {
        for (int i = 1; i <= 10; i++) {
            String currTime = TimeFormatUtil.getTimeByMs(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            //要发送的消息内容
            String message = "hello，工作队列模式（Work Queues），发送第 " + i + " 条消息，发送时间：" + currTime;
            rabbitTemplate.convertAndSend(RabbitmqConfig.WORK_QUEUES,message);
            System.out.println("工作队列模型（Work Queues），生产者，发送第 " + i + " 条消息，发送的消息内容：" + message);
        }
    }

    /**
     * 发布订阅模式（Publish/Subscribe）
     * 发布订阅模式使用 fanout 类型的交换机，将接收到的消息路由到每一个跟其绑定的queue(队列)
     * 备注：
     * 1、生产者将消息发送给交换机，并指定路由规则
     * 2、交换机将收到的所有消息按照路由规则转发到跟当前交换机绑定的所有队列上（每个和当前交换机绑定的队列都会收到消息，然后被消费者消费，也就是说一条消息可以被多个消费者同时消费）
     */
    @Test
    public void publishSubscribeSendMessageTest() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            String currTime = TimeFormatUtil.getTimeByMs(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            //要发送的消息内容
            String message = "hello，publish.subscribe.queues，发送第 " + i + " 条消息，发送时间：" + currTime;
            String routingKey = "";
            if (i % 2 == 0) {
                message = message.replace("queues", "queues.01");
                routingKey = "publish.subscribe.queues.01";
            } else {
                message = message.replace("queues", "queues.02");
                routingKey = "publish.subscribe.queues.02";
            }
            /**
             * convertAndSend(String exchange, String routingKey, Object object)：
             * 参数一（String exchange）：交换机名称
             * 参数二（String routingKey）：路由规则
             * 参数三（Object object）：发送的消息内容
             */
            rabbitTemplate.convertAndSend(RabbitmqConfig.PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE,routingKey,message);
            System.out.println("发布订阅模型（Publish/Subscribe），生产者，发送第 " + i + " 条消息，发送的消息内容：" + message);
//            Thread.sleep(2000);
        }
    }

    /**
     * 路由模式（Routing）
     * Routing模式 使用 Direct 类型的交换机，将接收到的消息路由到每一个跟其绑定的queue(队列)
     * 备注：
     * 1、生产者将消息发送给交换机，并指定路由规则
     * 2、交换机将收到的所有消息按照路由规则转发到跟当前交换机绑定的所有队列上（每个和当前交换机绑定的队列都会收到消息，然后被消费者消费，也就是说一条消息可以被多个消费者同时消费）
     * 3、路由模式不支持使用通配符，convertAndSend() 方法中指定的 routingKey 和队列绑定交换机时 with() 方法中配置的 routingKey 必须完全匹配才能将消息发送到具体的队列上
     */
    @Test
    public void routingSendMessageTest() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            String currTime = TimeFormatUtil.getTimeByMs(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            //要发送的消息内容
            String message = "hello，routing.queues，发送第 " + i + " 条消息，发送时间：" + currTime;
            String routingKey = "";
            if (i % 2 == 0) {
                //发送短信
                message = message.replace("queues", "queues.sms");
                routingKey = "routing.queues.sms";
            } else {
                //发送邮件
                message = message.replace("queues", "queues.email");
                routingKey = "routing.queues.email";
            }
            /**
             * convertAndSend(String exchange, String routingKey, Object object)：
             * 参数一（String exchange）：交换机名称
             * 参数二（String routingKey）：指定的路由，Routing模式下指定的路由必须和 RabbitmqConfig 配置类中队列和交换机绑定时 with() 中配置的 routingKey 要一致才行，不支持使用通配符
             * 参数三（Object object）：发送的消息内容
             */
            rabbitTemplate.convertAndSend(RabbitmqConfig.ROUTING_DIRECT_TEST_EXCHANGE,routingKey,message);
            System.out.println("路由模式（Routing），生产者，发送第 " + i + " 条消息，发送的消息内容：" + message);
//            Thread.sleep(1000);
        }
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
    @Test
    public void topicSendMessageTest() throws InterruptedException {
        for (int i = 1; i <= 9; i++) {
            String currTime = TimeFormatUtil.getTimeByMs(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            //要发送的消息内容
            String message = "hello，topic.queues，发送第 " + i + " 条消息，发送时间：" + currTime;
            String routingKey = "";
            if (i % 3 == 0) {//3、6、9
                //发送短信
                message = message.replace("queues", "queues.sms");
                routingKey = "topic.queues.sms";
            }  else if (i % 3 == 1){//1、4、7
                //发送邮件
                message = message.replace("queues", "queues.email");
                routingKey = "topic.queues.email";
            } else {//2、5、8
                //短信和邮件都发送
                message = message.replace("queues", "queues.sms.email");
                routingKey = "topic.queues.sms.email";
            }
            /**
             * convertAndSend(String exchange, String routingKey, Object object)：
             * 参数一（String exchange）：交换机名称
             * 参数二（String routingKey）：指定的路由，Topic模式下支持使用通配符进行匹配
             * 参数三（Object object）：发送的消息内容
             */
            rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_TEST_EXCHANGE,routingKey,message);
            System.out.println("主题模式（Topic，也叫通配符模式），生产者，发送第 " + i + " 条消息，发送的消息内容：" + message);
//            Thread.sleep(500);
        }
    }

}
