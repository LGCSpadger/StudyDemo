package com.test.pub.service;

public interface DistributedTransactionServ {

  /**
   * 分布式事务案例一：一个方法中操作两个数据库
   * 例如：订单服务，下单减库存，涉及到订单库和库存库
   */
  void transaction2PC();

}
