package com.test.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 service层 就无法注入
@SpringBootTest
public class JedisTest {

    private static Jedis jedis = null;

    static {
        //创建 jedis 对象
//        jedis = new Jedis("127.0.0.1", 6379);
        jedis = new Jedis("10.11.51.11", 7001);
        //设置 redis 的连接密码
        jedis.auth("admin");
    }

    /**
     * 使用 jedis 测试 redis 是否能正常连接
     * 备注：
     * ① 如果能正常连接，jedis.ping() 的返回结果为：PONG
     */
    @Test
    public void test01() {
        String result = jedis.ping();
        log.info("使用 jedis 测试 redis 是否能正常连接，结果：{}",result);
        jedis.close();
    }

    /**
     * 操作 String
     */
    @Test
    public void test02() {
        //jedis.keys("*") 表示查询全部
        Set<String> keys01 = jedis.keys("*");
        log.info("查询redis中所有存在的 key，总数：{}",keys01.size());
        if (keys01.size() > 0) {
            for (String key : keys01) {
                log.info("查询redis中所有存在的 key，key：{}",key);
            }
        }
        log.info("------------------------------------");
        //添加数据
        jedis.set("key01","斗破苍穹");
        jedis.set("key02","火影忍者");
        jedis.set("key03","画江湖之不良人");
        //批量添加数据
        jedis.mset("key04","斗罗大陆","key05","遮天","key06","星辰变");
        //批量查询
        List<String> mget = jedis.mget("key01", "key05");
        log.info("批量查询，结果：{}",mget);
        Set<String> keys02 = jedis.keys("*");
        log.info("查询redis中所有存在的 key，总数：{}",keys02.size());
        if (keys02.size() > 0) {
            for (String key : keys02) {
                log.info("查询redis中所有存在的 key，key：{}",key);
            }
        }
        log.info("------------------------------------");
        Boolean keyExist = jedis.exists("key01");
        log.info("判断 key 是否存在，结果：{}",keyExist);
        //jedis.ttl("key01") 返回结果为 -1 表示永不过期，-2 表示已经过期，其他数据表示剩余过期时间，单位为秒
        Long expiresTime = jedis.ttl("key01");
        log.info("获取 key 的过期时间，结果：{}",expiresTime);
        String key01Value = jedis.get("key01");
        log.info("根据 key 查询对应的 value，结果：{}",key01Value);
        jedis.close();
    }

    /**
     * 操作 List
     */
    @Test
    public void test03() {
        jedis.lpush("list01", "斗罗大陆", "遮天", "星辰变", "火影忍者");
        List<String> list = jedis.lrange("list01", 0, -1);
        log.info("查询list所有结果集，结果：{}",list);
        jedis.close();
    }

    /**
     * 操作 Set
     */
    @Test
    public void test04() {
        jedis.sadd("set01","天蚕土豆","鲁迅");
        Set<String> set = jedis.smembers("set01");
        log.info("查询set所有结果集，结果：{}",set);
        //删除set集合中的某个值
        jedis.srem("set01","鲁迅");
        Set<String> set01 = jedis.smembers("set01");
        log.info("删除后再查询set所有结果集，结果：{}",set01);
        jedis.close();
    }

    /**
     * 操作 Hash
     */
    @Test
    public void test05() {
        jedis.hset("hash01","username","萧炎");
        jedis.hset("hash01","age","23");
        String username = jedis.hget("hash01", "username");
        String age = jedis.hget("hash01", "age");
        log.info("操作 Hash，结果：{}",username);
        log.info("操作 Hash，结果：{}",age);
        HashMap<String, String> map = new HashMap<>();
        map.put("phone","17805131927");
        map.put("address","南京市江宁区");
        map.put("QQ","1106459669");
        jedis.hmset("hash01",map);
        List<String> list = jedis.hmget("hash01", "phone","QQ");
        log.info("操作 Hash，结果：{}",list);
//        List<String> hash01 = jedis.hmget("hash01");//这样写会报错
//        log.info("操作 Hash，结果：{}",hash01);
        jedis.close();
    }

    /**
     * 操作 ZSet
     */
    @Test
    public void test06() {
        jedis.zadd("zset01",100D,"z3");
        jedis.zadd("zset01",90d,"z2");
        Set<String> zset = jedis.zrange("zset01", 0, -1);
        log.info("操作 ZSet，结果：{}",zset);
        jedis.close();
    }

    /**
     * 清空所有的数据
     */
    @Test
    public void test07() {
        Set<String> keys01 = jedis.keys("*");
        log.info("清空数据之前，查询所有的key，结果：{}",keys01);
        //清空所有的数据
//        String flushDB = jedis.flushDB();
//        System.out.println("清空所有的数据，结果：" + flushDB);
//        Set<String> keys02 = jedis.keys("*");
//        log.info("清空数据之后，查询所有的key，结果：{}",keys02);
//        jedis.close();
    }

    /**
     * 实际案例：模拟验证码发送
     * 1、输入手机号，点击发送后随机生成6位数字码，2分钟有效
     * 2、输入验证码，点击验证，返回成功或失败
     * 3、每个手机号每天只能获取3次验证码
     */
    @Test
    public void test08() {
        //验证码发送
        verifyCode("17805131927");
        //验证码校验
//        getRedisCode("17805131927","66666");
    }

    //验证码校验
    private void getRedisCode(String phone,String code) {
        //验证码key
        String codeKey = "VerifyCode" + phone + ":code";
        String redisCode = jedis.get(codeKey);
        if (redisCode.equals(code)) {
            System.out.println("验证通过");
        } else {
            System.out.println("验证失败");
        }
        jedis.close();
    }

    //每个手机每天只能获取三次验证码，验证码放到redis中，设置过期时间
    private void verifyCode(String phone) {
        //手机获取验证码次数key
        String countKey = "VerifyCode" + phone + ":count";
        //验证码key
        String codeKey = "VerifyCode" + phone + ":code";
        //每个手机每天只能获取三次验证码
        String count = jedis.get("countKey");
        if (count == null) {
            //没有发送次数，说明是第一次获取验证码
            jedis.setex(countKey,24*60*60,"1");
        } else if (Integer.parseInt(count) <= 2) {
            //获取验证码次数加一
            jedis.incr(countKey);
        } else {
            //当天获取验证码次数超过3次，不能再获取验证码
            System.out.println("今天获取验证码次数已超过三次，无法再获取！！");
            jedis.close();
        }
        //验证码存入redis
        String vcode = getCode();
        jedis.setex(codeKey,120,vcode);//过期时间120秒
        jedis.close();
    }

    //生成 6 位数验证码
    private String getCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }


}
