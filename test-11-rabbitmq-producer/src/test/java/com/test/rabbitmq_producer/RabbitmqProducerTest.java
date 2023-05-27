package com.test.rabbitmq_producer;

import com.github.pagehelper.PageInfo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.test.rabbitmq_producer.config.RabbitmqConfig;
import com.test.rabbitmq_producer.entity.Article;
import com.test.rabbitmq_producer.service.ArticleService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RabbitmqProducerTest {

    //队列名称
    private final String QUEUE = "hellword";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ArticleService articleService;

    //生产者发送消息
    @Test
    public void rabbitmqFactoryTest() {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");//rabbitmq默认虚拟机名称为 "/" ，虚拟机相当于一个独立的mq服务器
            //创建与RabbitMQ服务的TCP连接
            connection = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            //声明队列，如果rabbitmq中没有此队列将会自动创建（param1：队列名称；param2：是否持久化；param3：队列是否独占此连接；param4：队列不再使用时是否自动删除此队列；param5：队列参数）
            channel.queueDeclare(QUEUE,true,false,false,null);
            //模拟一条消息
            String message = "helloworld！小明" + System.currentTimeMillis();
            //发布消息（param1：Exchange（交换机）名称，如果没有指定，则使用默认的交换机（不指定交换机的话填一个空字符串，不要填null，否则会报错）；param2：routingKey，消息的路由key，是用于交换机将消息转发到指定的消息队列；param3：消息包含的属性；param4：消息体）
            //这里没有指定交换机，消息将会发送给默认交换机，每个队列也会绑定那个默认交换机，但是不能显示绑定或解除绑定。默认的交换机，routingKey等于队列名称。
            channel.basicPublish("",QUEUE,null,message.getBytes());
            log.info("已发送的消息内容：" + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消息转换器
     * 解决问题：代码里直接发送对象，虽然接收的到消息，但是rabbitmq的界面上看到的消息会是乱码
     */
    @Test
    public void rabbitmqDataFormatTest() {
        Article article = new Article();
        article.setAuthor("鲁迅");
        PageInfo<Article> articlePageInfo = articleService.queryByPage(article, 1, 10);
        log.info("查询结果：{}",articlePageInfo.getList());
        rabbitTemplate.convertAndSend(RabbitmqConfig.SIMPLE_QUEUE,articlePageInfo.getList());
    }

}
