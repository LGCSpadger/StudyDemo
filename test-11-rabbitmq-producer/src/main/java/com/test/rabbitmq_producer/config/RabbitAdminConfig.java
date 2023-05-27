package com.test.rabbitmq_producer.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitAdminConfig {

//  @Value("${spring.rabbitmq.host}")
  @Value("${spring.rabbitmq.addresses}")
  private String host;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Value("${spring.rabbitmq.virtualhost}")
  private String virtualhost;

  @Bean
  public ConnectionFactory connectionFactory(){
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setAddresses(host);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setVirtualHost(virtualhost);
    connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
    connectionFactory.setPublisherReturns(true);
    return connectionFactory;
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    rabbitAdmin.setAutoStartup(true);
    return rabbitAdmin;
  }
}

