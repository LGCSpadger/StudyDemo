package com.test.redis.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterConfig {

  @Autowired
  private RedisProperties redisProperties;

  /**
   * 注意：
   * 1、这里返回的 JedisCluster 是单例的，并且可以直接注入到其他类中去使用
   * @return
   */
  @Bean
  public JedisCluster getJedisCluster() {
    List<String> nodesList = redisProperties.getCluster().getNodes();//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
    Set<HostAndPort> nodesSet = new HashSet<>();

    for (String ipPort : nodesList) {
      String[] ipPortPair = ipPort.split(":");
      nodesSet.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
    }

    return new JedisCluster(nodesSet,redisProperties.getConnectTimeout().getNano(),1000,1,redisProperties.getPassword() ,new GenericObjectPoolConfig());//需要密码连接的创建对象方式
  }

}
