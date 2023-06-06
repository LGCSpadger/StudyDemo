package com.test.pub;

import cn.hutool.core.lang.hash.CityHash;
import com.test.pub.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Optional 使用介绍
 * 作用：使用 jdk8 的 Optional 类可以优雅的规避 NullPointerException(空指针异常) 问题
 * 原理：Optional 本质是一个容器，容器中存在为 null 或者不包含非 null 值的容器对象。提供了一系列的方法供我们判断该容器里面的对象是否存在。
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class OptionalTest {

    @Test
    public void test01() {
        Article article = new Article();
        //正常判断一个对象中的某个字段是否为空值
        if (article != null) {
            String bookName = article.getName();
            if (bookName != null && bookName != "") {
                System.out.println("输出操作");
            }
        }
        //使用 Optional 类进行空值判断
        Optional<String> optResult = Optional.ofNullable(article).map(Article::getName);//如果 article 中的 name 属性为空，则直接返回 null
        if (optResult.isPresent()) {//判断容器是否为空
            String bookName = optResult.get();//从容器中取出返回的对象
        }
    }
}
