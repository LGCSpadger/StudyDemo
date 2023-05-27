package com.test.mybatis_plus;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.mybatis_plus.entity.User;
import com.test.mybatis_plus.mapper.UserMapper;
import java.util.ArrayList;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    private UserMapper userMapper;

    //查询全部用户
    @Test
    public void test01() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    //查询插入新的数据
    @Test
    public void test02() {
        User user = new User();
        user.setName("行走的空白代码");
        user.setAge(27);
        user.setEmail("120101954@qq.com");
        int result = userMapper.insert(user); //插入数据的时候会自动生成id，数据插入完成后会将id自动回填到当前实体类中
        System.out.println(result); //受影响的行数
        System.out.println(user); //发现id自动回填
    }

    //测试更新
    @Test
    public void test03() {
        User user = new User();
        user.setId(1432617439904002049L);
        user.setName("我的博客叫：行走的空白代码");
        user.setAge(27);
        user.setEmail("2424496907@qq.com");
        //注意：updateById参数是一个对象
        int result = userMapper.updateById(user); //自动生成id
        System.out.println(result); //受影响的行数
    }

    //测试 mybatis-plus 的 乐观锁 成功案例
    @Test
    public void test04() {
        //1.查询用户信息
        User user = userMapper.selectById(1432617439904002049L);
        //2.修改用户信息
        user.setEmail("13610506606@163.com");
        //3.执行更新操作
        userMapper.updateById(user);
    }

    //测试 mybatis-plus 的 乐观锁 失败案例（多线程下）
    @Test
    public void test05() {
        log.info("开始 测试 mybatis-plus 的 乐观锁 失败案例（多线程下）");
        //线程1
        User user1 = userMapper.selectById(1432617439904002049L);
        user1.setName("godfrey111");
        user1.setEmail("13610506606@163.com");

        //模拟另外一个线程执行插队操作
        User user2 = userMapper.selectById(1432617439904002049L);
        user2.setName("godfrey222");
        user2.setEmail("13610506606@163.com");
        userMapper.updateById(user2);

        //自旋锁多次操作尝试提交
        userMapper.updateById(user1);
        log.info("结束 测试 mybatis-plus 的 乐观锁 失败案例（多线程下）");
    }

    //测试查询
    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    //测试批量查询
    @Test
    public void testSelectByBatchId() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));
        users.forEach(System.out::println);
    }

    //条件查询之一 使用map操作
    @Test
    public void testSelectBatchIds() {
        HashMap<String, Object> map = new HashMap<>();
        //自定义查询
        map.put("name","Tom");
        map.put("age",28);

        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //测试分页查询
    @Test
    public void testPage() {
        //参数一：当前页
        //参数二：页面大小
        //使用了分页插件之后，所有的分页操作页变得简单了
        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page,null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    @Test
    public void testTemp() {
        long startTime = System.currentTimeMillis();
        long resu01 = startTime % 3600000;
        long resu02 = (startTime / 1000L) % 3600;
        System.out.println(resu01);
        System.out.println(resu02);
    }


}
