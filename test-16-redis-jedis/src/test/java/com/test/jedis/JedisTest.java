package com.test.jedis;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 service层 就无法注入
@SpringBootTest
public class JedisTest {

  /**
   * 使用 jedis 连接 redis
   */
  @Test
  public void test01() {
    Jedis jedis = new Jedis("10.11.51.11", 7001);
    jedis.auth("admin");
    String result = jedis.ping();
    log.info("使用 jedis 测试 redis 是否能正常连接，结果：{}",result);
    jedis.close();
  }

  /**
   * 使用 jedis 连接 redis 集群
   */
  @Test
  public void test02() {
    //创建jedisCluster
    Set<HostAndPort> nodes = new HashSet<>();
    nodes.add(new HostAndPort("10.11.51.11", 7001));
    nodes.add(new HostAndPort("10.11.51.11", 7002));
    nodes.add(new HostAndPort("10.11.51.12", 7001));
    nodes.add(new HostAndPort("10.11.51.12", 7002));
    nodes.add(new HostAndPort("10.11.51.13", 7001));
    nodes.add(new HostAndPort("10.11.51.13", 7002));

    //redis有设置密码
    JedisCluster cluster = new JedisCluster(nodes,100,100,100,"admin",new GenericObjectPoolConfig<>());
    //redis没有设置密码
//    JedisCluster cluster = new JedisCluster(nodes);


    cluster.set("jedis-key-001", "我在测试jedis");

    String result = cluster.get("jedis-key-001");
    System.out.println(result);

    cluster.close();
  }

}
