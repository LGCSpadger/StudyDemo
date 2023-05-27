package com.test.rabbitmq_consumer01;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class RabbitmqConsumerTest {

    //队列名称
    private final String QUEUE = "hellword";

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Test
    public void test01() {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setVirtualHost("/");
            //与rabbitmq创建TCP连接
            connection = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            //声明队列，如果rabbitmq中没有此队列将会自动创建（param1：队列名称；param2：是否持久化；param3：队列是否独占此连接；param4：队列不再使用时是否自动删除此队列；param5：队列参数）
            channel.queueDeclare(QUEUE,true, false,false,null);
            //定义消费方法
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws UnsupportedEncodingException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //路由key
                    String routingKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String message = new String(body, "utf-8");
                    log.info("接收到的消息内容：" + message);
                }
            };
            //监听队列（param1：队列名称；param2：是否自动回复，设置为true表示接收到消息后自动向mq回复 收到了，mq接收到 回复 后会删除消息，设置为false则需要手动回复；param3：消费消息的方法，消费者接收到消息后调用此方法）
            channel.basicConsume(QUEUE,true,consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
