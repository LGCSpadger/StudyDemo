package com.test.pub;

import com.test.pub.service.DistributedTransactionServ;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 分布式事务测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class DistributedTransactionTest {

  @Autowired
  private DistributedTransactionServ dsServ;

  @Test
  public void test01() {
    dsServ.transaction2PC();
  }

}
