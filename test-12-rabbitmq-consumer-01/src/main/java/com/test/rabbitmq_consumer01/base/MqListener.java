package com.test.rabbitmq_consumer01.base;

import com.rabbitmq.client.Channel;
import java.io.IOException;

public interface MqListener<T> {

  // 业务通过实现这个接口处理
  default void handler(T map, Channel channel) throws IOException {}

}

