package com.test.rabbitmq_producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 消息生产者配置类
 * 配置类需要配置哪些东西：
 * 1、定义队列
 * 2、定义交换机
 * 3、配置交换机类型等相关信息（比如说：是否持久化、是否自动删除...）
 * 4、声明队列
 * 5、队列绑定交换机（RabbitMQ默认使用direct交换机，每个新建的队列都会自动绑定到 direct 交换机上，且绑定的路由键 routingKey 名称和队列名称相同。如果想让队列绑定其他交换机，可以手动进行绑定。）
 */
@Slf4j
@Configuration
public class RabbitmqConfig {

    //定义队列
    public static final String SIMPLE_QUEUE = "simple.queue";//简单模式测试队列
    public static final String WORK_QUEUES = "work.queues";//工作队列模式测试队列
    public static final String PUBLISH_SUBSCRIBE_QUEUES_1 = "publish.subscribe.queues.01";//发布订阅模式测试队列
    public static final String PUBLISH_SUBSCRIBE_QUEUES_2 = "publish.subscribe.queues.02";//发布订阅模式测试队列
    public static final String ROUTING_QUEUES_SMS = "routing.queues.sms";//路由模式测试队列
    public static final String ROUTING_QUEUES_EMAIL = "routing.queues.email";//路由模式测试队列
    public static final String TOPIC_QUEUES_SMS = "topic_queues_sms";//主题模式短信队列
    public static final String TOPIC_QUEUES_EMAIL = "topic_queues_email";//主题模式邮件队列

    //定义交换机
    public static final String MODEL_TEST_EXCHANGE = "model_test_exchange";//rabbitmq工作模式通用测试交换机
    public static final String PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE = "fanout_exchange";//发布订阅模式测试交换机
    public static final String ROUTING_DIRECT_TEST_EXCHANGE = "direct_exchange";//路由模式测试交换机
    public static final String TOPIC_TEST_EXCHANGE = "topic_exchange";//主题模式测试交换机

    /**
     * 配置交换机类型等相关信息
     * 交换机具体类型如下：
     * 1、direct：它会把消息路由到那些 binding key 与 routing key 完全匹配的 Queue 中
     * 2、fanout：它会把所有发送到该 Exchange 的消息路由到所有与它绑定的 Queue 中
     * 3、headers：headers 类型的 Exchange 不依赖于 routing key 与 binding key 的匹配规则来路由消息，而是根据发送的消息内容中的 headers 属性进行匹配。（headers 类型的交换器性能差，不实用，基本不会使用。）
     * 4、topic：与direct模式相比，多了个可以使用通配符进行匹配的功能！，这种模式Routingkey一般都是由一个或多个单词组成，多个单词之间以"."分割，例如：item.insert ---------星号 匹配一个1词 , 例audit.* ------- #号匹配一个或多个词 audit.#
     * @return
     */
    @Bean(MODEL_TEST_EXCHANGE)
    public Exchange MODEL_TEST_EXCHANGE() {
        /**
         * durable(boolean isDurable)：是否持久化，默认为 true
         * autoDelete()：代表当前队列的最后一个消费者退订时被自动删除。注意：此时不管队列中是否还存在消息，队列都会删除
         */
        return ExchangeBuilder.topicExchange(MODEL_TEST_EXCHANGE).durable(true).build();
    }

    /**
     * 配置交换机--fanout交换机
     * @return
     */
    @Bean(PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE)
    public Exchange PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE() {
        return ExchangeBuilder.fanoutExchange(PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE).durable(true).build();
    }

    /**
     * 配置交换机--direct交换机
     * @return
     */
    @Bean(ROUTING_DIRECT_TEST_EXCHANGE)
    public Exchange ROUTING_DIRECT_TEST_EXCHANGE() {
        return ExchangeBuilder.directExchange(ROUTING_DIRECT_TEST_EXCHANGE).durable(true).build();
    }

    /**
     * 配置交换机--topic交换机
     * @return
     */
    @Bean(TOPIC_TEST_EXCHANGE)
    public Exchange TOPIC_TEST_EXCHANGE() {
        return ExchangeBuilder.topicExchange(TOPIC_TEST_EXCHANGE).durable(true).build();
    }

    //声明-简单模式测试队列
    @Bean(SIMPLE_QUEUE)
    public Queue SIMPLE_QUEUE() {
        return new Queue(SIMPLE_QUEUE);
    }

    //声明-工作队列模式测试队列
    @Bean(WORK_QUEUES)
    public Queue WORK_QUEUES() {
        return new Queue(WORK_QUEUES);
    }

    //声明-发布订阅模式测试队列01
    @Bean(PUBLISH_SUBSCRIBE_QUEUES_1)
    public Queue PUBLISH_SUBSCRIBE_QUEUES_1() {
        return new Queue(PUBLISH_SUBSCRIBE_QUEUES_1);
    }

    //声明-发布订阅模式测试队列02
    @Bean(PUBLISH_SUBSCRIBE_QUEUES_2)
    public Queue PUBLISH_SUBSCRIBE_QUEUES_2() {
        return new Queue(PUBLISH_SUBSCRIBE_QUEUES_2);
    }

    //声明-路由模式短信测试队列
    @Bean(ROUTING_QUEUES_SMS)
    public Queue ROUTING_QUEUES_SMS() {
        return new Queue(ROUTING_QUEUES_SMS);
    }

    //声明-路由模式邮件测试队列
    @Bean(ROUTING_QUEUES_EMAIL)
    public Queue ROUTING_QUEUES_EMAIL() {
        return new Queue(ROUTING_QUEUES_EMAIL);
    }

    //声明-主题模式短信队列
    @Bean(TOPIC_QUEUES_SMS)
    public Queue TOPIC_QUEUES_SMS() {
        return new Queue(TOPIC_QUEUES_SMS);
    }

    //声明-主题模式邮件队列
    @Bean(TOPIC_QUEUES_EMAIL)
    public Queue TOPIC_QUEUES_EMAIL() {
        return new Queue(TOPIC_QUEUES_EMAIL);
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_PUBLISH_SUBSCRIBE_QUEUES_1(@Qualifier(PUBLISH_SUBSCRIBE_QUEUES_1) Queue queue,
        @Qualifier(PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("publish.subscribe.queues.01").noargs();
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_PUBLISH_SUBSCRIBE_QUEUES_2(@Qualifier(PUBLISH_SUBSCRIBE_QUEUES_2) Queue queue,
        @Qualifier(PUBLISH_SUBSCRIBE_FANOUT_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("publish.subscribe.queues.02").noargs();
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_ROUTING_QUEUES_SMS(@Qualifier(ROUTING_QUEUES_SMS) Queue queue,
        @Qualifier(ROUTING_DIRECT_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("routing.queues.sms").noargs();
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_ROUTING_QUEUES_EMAIL(@Qualifier(ROUTING_QUEUES_EMAIL) Queue queue,
        @Qualifier(ROUTING_DIRECT_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("routing.queues.email").noargs();
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_TOPIC_QUEUES_SMS(@Qualifier(TOPIC_QUEUES_SMS) Queue queue,
        @Qualifier(TOPIC_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("topic.queues.sms.#").noargs();
    }

    /**
     * 队列绑定到交换机
     * @param queue         队列
     * @param exchange      交换机
     * @return
     */
    @Bean
    public Binding BINDING_TOPIC_QUEUES_EMAIL(@Qualifier(TOPIC_QUEUES_EMAIL) Queue queue,
        @Qualifier(TOPIC_TEST_EXCHANGE) Exchange exchange) {
        //在 with() 中指定 routingKey ，routingKey 中的 # 号表示 通配符，可以匹配任何字符
        return BindingBuilder.bind(queue).to(exchange).with("topic.queues.#.email").noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
