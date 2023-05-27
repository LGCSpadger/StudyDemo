package com.test.redis.service.impl;

import com.test.redis.service.RedisService;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

  @Resource
  private RedisTemplate redisTemplate;

  @Resource
  private RedissonClient redissonClient;

  // Redis分布式锁的key
  public static final String REDIS_LOCK = "good_lock";

  // 使用ReentrantLock锁解决单体应用的并发问题
  Lock lock = new ReentrantLock();

  @Resource
  private JedisCluster jedisCluster;

  private int index = 0;
  private int successIndex = 0;

  /**
   * 单体应用下，多个用户购买同一商品，会发生数据不一致问题（同一件商品被购买多次）
   * @return
   */
  @Override
  public String goodsReduce() {
    // Redis中存有goods:001号商品，数量为100
    Object result = redisTemplate.opsForValue().get("goods:001");
    int total = 0;
    if (result != null) {
      // 获取到剩余商品数
      total = Integer.parseInt(result.toString());
    }

    if(total > 0){
      // 剩余商品数大于0 ，则进行扣减
      int realTotal = total - 1;
      // 将商品数回写数据库
      redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
      successIndex++;
      System.out.println("单体---不加锁---购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
      return "单体---不加锁----购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8016";
    }else{
      index++;
      System.out.println("单体---不加锁---购买商品失败，失败次数：" + index);
    }
    return "单体---不加锁----购买商品失败，服务端口为8016";
  }

  /**
   * 单体应用下，多个用户购买同一商品，会发生数据不一致问题（同一件商品被购买多次）
   * 加锁解决数据不一致问题 -- redis单机锁
   * @return
   */
  @Override
  public String goodsReduceAddLock() {
    lock.lock();//加锁独占对象
    try {
      // Redis中存有goods:001号商品，数量为100
      Object result = redisTemplate.opsForValue().get("goods:001");
      int total = 0;
      if (result != null) {
        // 获取到剩余商品数
        total = Integer.parseInt(result.toString());
      }

      if(total > 0){
        // 剩余商品数大于0 ，则进行扣减
        int realTotal = total - 1;
        // 将商品数回写数据库
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
        successIndex++;
        System.out.println("单体---加锁---购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
        return "单体---加锁---购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8016";
      }else{
        index++;
        System.out.println("单体---加锁---购买商品失败，失败次数：" + index);
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
      lock.unlock();
    } finally {
      lock.unlock();
    }
    return "单体---加锁---购买商品失败，服务端口为8016";
  }

  /**
   * redis 分布式锁：方式一
   * 解决的问题：此方法可以解决分布式中数据一致性问题
   * 不足：该方法是在 finally 代码块中释放锁，假如程序运行中服务器挂了，程序无法走到 finally 代码块中，也就无法释放锁，其他正常的线程也就无法获得锁
   * @return
   */
  @Override
  public String goodsReduceDistLockOne() {
    // 每个人进来先要进行加锁，key值为"good_lock"，value随机生成
    String value = UUID.randomUUID().toString().replace("-","");
    try {
      // 加锁
      Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);
      // 加锁失败
      if(!flag){
//        log.info( value+ " 抢锁失败");
        return "抢锁失败！";
      }
//      log.info( value+ " 抢锁成功");
      // Redis中存有goods:001号商品，数量为100
      Object result = redisTemplate.opsForValue().get("goods:001");
      int total = 0;
      if (result != null) {
        // 获取到剩余商品数
        total = Integer.parseInt(result.toString());
      }

      if(total > 0){
        // 剩余商品数大于0 ，则进行扣减
        int realTotal = total - 1;
        // 将商品数回写数据库
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
        successIndex++;
        System.out.println("分布式锁***方式一***购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
        return "分布式锁***方式一***购买商品成功，库存还剩：" + realTotal + "件";
      }else{
        index++;
        System.out.println("分布式锁***方式一***购买商品失败，失败次数：" + index);
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
      lock.unlock();
    } finally {
      //释放锁
      redisTemplate.delete(REDIS_LOCK);
    }
    return "分布式锁***方式一***购买商品失败，服务端口为8016";
  }

  /**
   * redis 分布式锁：方式二
   * 解决的问题：在 方式一 的基础上，为 key 增加过期时间，即使服务器宕机，到了一定时间，锁也会被释放
   * 不足：虽然设置了过期时间，解决了key无法删除的问题，但是还有新的问题
   * 新问题：上面设置了key的过期时间为10秒，如果业务逻辑比较复杂，需要调用其他微服务，处理时间需要15秒（模拟场景，别较真），而当10秒钟过去之后，这个key就过期了，其他请求就又可以设置这个key，
   * 此时如果耗时15秒的请求处理完了，回来继续执行程序，就会把别人设置的key给删除了，这是个很严重的问题！
   *
   * 补充：
   * redis中设置过期时间的两种方法：
   * 1、redisTemplate.expire(REDIS_LOCK,10, TimeUnit.SECONDS)  该方法需要单独的一行代码，不具备原子性
   * 2、redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value,10L,TimeUnit.SECONDS)  在加锁的同时设置过期时间
   * @return
   */
  @Override
  public String goodsReduceDistLockTwo() {
    // 每个人进来先要进行加锁，key值为"good_lock"，value随机生成
    String value = UUID.randomUUID().toString().replace("-","");
    try {
      // 为key加一个过期时间，其余代码不变
      Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK,value,10L, TimeUnit.SECONDS);
      // 加锁失败
      if(!flag){
//        log.info( value+ " 抢锁失败");
        return "抢锁失败！";
      }
//      log.info( value+ " 抢锁成功");
      // Redis中存有goods:001号商品，数量为100
      Object result = redisTemplate.opsForValue().get("goods:001");
      int total = 0;
      if (result != null) {
        // 获取到剩余商品数
        total = Integer.parseInt(result.toString());
      }

      if(total > 0){
        // 剩余商品数大于0 ，则进行扣减
        int realTotal = total - 1;
        // 将商品数回写数据库
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
        successIndex++;
        System.out.println("分布式锁***方式一***购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
        return "分布式锁***方式一***购买商品成功，库存还剩：" + realTotal + "件";
      }else{
        index++;
        System.out.println("分布式锁***方式一***购买商品失败，失败次数：" + index);
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
      lock.unlock();
    } finally {
      //释放锁
      redisTemplate.delete(REDIS_LOCK);
    }
    return "分布式锁***方式一***购买商品失败，服务端口为8016";
  }

  /**
   * redis 分布式锁：方式三
   * 解决的问题：在 方式二 的基础上，规定了谁加的锁，谁才能删除，防止某个线程删掉了其他线程设置的key，这种方式解决了因服务处理时间太长而释放了别人锁的问题。
   * 不足：finally块 的判断和 del删除操作 不是原子操作，并发的时候也会出问题，并发嘛，就是要保证数据的一致性，保证数据的一致性，最好要保证对数据的操作具有原子性。
   * @return
   */
  @Override
  public String goodsReduceDistLockThree() {
    // 每个人进来先要进行加锁，key值为"good_lock"，value随机生成
    String value = UUID.randomUUID().toString().replace("-","");
    try {
      // 为key加一个过期时间，其余代码不变
      Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK,value,10L, TimeUnit.SECONDS);
      // 加锁失败
      if(!flag){
//        log.info( value+ " 抢锁失败");
        return "抢锁失败！";
      }
//      log.info( value+ " 抢锁成功");
      // Redis中存有goods:001号商品，数量为100
      Object result = redisTemplate.opsForValue().get("goods:001");
      int total = 0;
      if (result != null) {
        // 获取到剩余商品数
        total = Integer.parseInt(result.toString());
      }

      if(total > 0){
        // 剩余商品数大于0 ，则进行扣减
        int realTotal = total - 1;
        // 将商品数回写数据库
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
        successIndex++;
        System.out.println("分布式锁***方式一***购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
        return "分布式锁***方式一***购买商品成功，库存还剩：" + realTotal + "件";
      }else{
        index++;
        System.out.println("分布式锁***方式一***购买商品失败，失败次数：" + index);
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
      lock.unlock();
    } finally {
      // 谁加的锁，谁才能删除！！！！
      if(redisTemplate.opsForValue().get(REDIS_LOCK).equals(value)){
        redisTemplate.delete(REDIS_LOCK);
      }
    }
    return "分布式锁***方式一***购买商品失败，服务端口为8016";
  }

  /**
   * redis 分布式锁：方式四
   * 解决的问题：在 方式三 的基础上，解决了删除操作的原子性问题。
   * 不足：没有考虑到缓存续命 和 redis集群部署下异步复制造成锁的丢失（主节点没来得及把刚刚set进来的这条数据给从节点，就挂了） 的问题。
   * @return
   */
  @Override
  public String goodsReduceDistLockFour() {
    // 每个人进来先要进行加锁，key值为"good_lock"，value随机生成
    String value = UUID.randomUUID().toString().replace("-","");
    try {
      // 为key加一个过期时间，其余代码不变
      Boolean flag = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK,value,10L, TimeUnit.SECONDS);
      // 加锁失败
      if(!flag){
//        log.info( value+ " 抢锁失败");
        return "抢锁失败！";
      }
//      log.info( value+ " 抢锁成功");
      // Redis中存有goods:001号商品，数量为100
      Object result = redisTemplate.opsForValue().get("goods:001");
      int total = 0;
      if (result != null) {
        // 获取到剩余商品数
        total = Integer.parseInt(result.toString());
      }

      if(total > 0){
        // 剩余商品数大于0 ，则进行扣减
        int realTotal = total - 1;
        // 将商品数回写数据库
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));
        successIndex++;
        System.out.println("分布式锁***方式一***购买商品---成功，成功次数：" + successIndex + "，库存剩余：" + realTotal);
        return "分布式锁***方式一***购买商品成功，库存还剩：" + realTotal + "件";
      }else{
        index++;
        System.out.println("分布式锁***方式一***购买商品失败，失败次数：" + index);
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
      lock.unlock();
    } finally {
      // 谁加的锁，谁才能删除，使用Lua脚本，进行锁的删除
//      Jedis jedis = null;
      try{
//        jedis = new Jedis(redisHost,redisPort);

        String script = "if redis.call('get',KEYS[1]) == ARGV[1] " +
            "then " +
            "return redis.call('del',KEYS[1]) " +
            "else " +
            "   return 0 " +
            "end";

        Object eval = jedisCluster.eval(script, Collections.singletonList(REDIS_LOCK), Collections.singletonList(value));
        if("1".equals(eval.toString())){
          System.out.println("-----del redis lock ok....");
        }else{
          System.out.println("-----del redis lock error ....");
        }
      }catch (Exception e){

      }finally {
        if(null != jedisCluster){
          jedisCluster.close();
        }
      }

    }
    return "分布式锁***方式一***购买商品失败，服务端口为8016";
  }

  /**
   * redis 分布式锁：方式五（RedLock的 Redisson 落地实现）
   * 解决的问题：在 方式四 的基础上，解决了 缓存续命 和 redis集群部署下异步复制造成锁的丢失（主节点没来得及把刚刚set进来的这条数据给从节点，就挂了） 的问题。
   * 在分布式锁中要解决的几个问题：
   * 1、添加锁之后假如程序挂了没有来得及解锁，使得其他线程无法正常获取锁？解决：在加锁的同时给锁设置过期时间，这样即使在解锁之前程序就挂了，只要到了过期时间，该锁依然会被释放掉。
   * 2、加锁和给锁设置过期时间怎么保证原子性？解决：redisTemplate的 setIfAbsent() 方法可以一步完成加锁和设置过期时间两个操作，保证原子性；Redisson 框架底层使用 Lua 脚本保证原子性。
   * 3、如何防止一个线程删掉另外一个线程加的锁？解决：锁--(key,value)形式，key是固定的，value为uuid，每个线程对应一个uuid，这样解锁的时候判断一下是不是自己加的锁。
   * 4、如何给锁续命（A线程加锁时设置过期时间为10s，但是可能需要15秒才能执行完，才能正常去解锁，但是10s时该锁已过期，B线程已经进来，并且已经加了锁，那15s的时候A线程执行完
   * 后解锁解的其实是B线程的锁，而B线程又会把C线程的锁解掉，也就相当于所有的线程都没有加锁）？解决：Redisson 底层原理其实是当一个线程进来加锁之后，它会开启一个分线程，
   * 定时（该锁的过期时间的1/3）的重置这个锁的过期时间。
   * 备注：
   * Redisson：一个强大的 redis 框架
   * Redisson 实现分布式锁的原理：
   * 锁续时：Redisson 的具体实现思路, 中文翻译叫做 “看门狗”
   *    1、获取到锁之后执行 “看门狗” 流程
   *    2、使用 Netty 的 Timeout 实现定时延时
   *    3、比如锁过期 30 秒, 每过 1/3 时间也就是 10 秒会检查锁是否存在, 存在则更新锁的超时时间
   *    备注：Redisson 底层使用 Lua 脚本保证 检查锁是否存在 和 更新锁的过期时间 两个操作的原子性。（redis底层是c++实现的，c++中有对Lua脚本的特殊操作，所以在redis中将 一个Lua脚本 中的所有操作视为一次不可分割的操作）
   * 解锁：Redisson解锁也是通过 Lua 脚本实现的，也可视为一次原子操作
   * @return
   */
  @Override
  public String goodsReduceDistLockFive() {
    RLock lock = redissonClient.getLock(REDIS_LOCK);
    lock.lock();//lock() 方法加锁成功 默认过期时间 30 秒, 并且支持 “看门狗” 续时功能
    try {
      int total = Integer.parseInt(redisTemplate.opsForValue().get("goods:001").toString());// Redis中存有goods:001号商品，数量为100
      if (total > 0) {
        int realTotal = total - 1;// 剩余商品数大于0 ，则进行扣减
        redisTemplate.opsForValue().set("goods:001",String.valueOf(realTotal));// 将商品数回写数据库
        return "分布式锁***方式五***扣减成功，库存剩余：" + realTotal + "件";
      } else {
        System.out.println("分布式锁***方式五***扣减失败，库存不足");
      }
    } catch (Exception e) {
      log.error("异常：{}",e);
    } finally {
      if(lock.isLocked() && lock.isHeldByCurrentThread()){
        lock.unlock();//解锁
      }
    }
    return "分布式锁***方式五***扣减失败，库存不足";
  }

}
