package com.test.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class FreemarkerTest {

    //反射
    //java中获取指定文件或者目录的绝对路径
    @Test
    public void testGetPath() {
        //1.获取项目的根路径
        String xmglj = this.getClass().getResource("/").getPath();
        log.info("项目根路径：" + xmglj);
        //2.获取项目中 resources 目录下的 templates 目录的绝对路径（方式一）
        String temPath01 = this.getClass().getResource("/templates").getPath();
        log.info("项目中 resources 目录下的 templates 目录的绝对路径（方式一）：" + temPath01);
        String temPath02 = this.getClass().getClassLoader().getResource("templates").getPath();
        log.info("项目中 resources 目录下的 templates 目录的绝对路径（方式二）：" + temPath02);
    }

    //基于模板生成静态化文件
    @Test
    public void test01() {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //设置模板路径
            String classPath = this.getClass().getResource("/templates").getPath();//获取 resources 中 templates 目录的绝对路径
            String path = this.getClass().getClassLoader().getResource("templates").getPath();
            log.info("模板路径：" + classPath);
            configuration.setDirectoryForTemplateLoading(new File(classPath));
            //设置字符集
            configuration.setDefaultEncoding("UTF-8");
            //加载模板
            Template template = configuration.getTemplate("freemarker_test_01.ftl");
            //数据模型
            Map<String,String> map = new HashMap<>();
            map.put("name","天王盖地虎");
            //执行静态化，得到静态化之后的内容
            String templateContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            log.info("静态化之后的内容：" + templateContent);
            //将静态化后的内容转成输入流
            inputStream = IOUtils.toInputStream(templateContent);
            //通过文件输出流将静态化后的内容输出到指定目录的文件中
            fileOutputStream = new FileOutputStream(new File("E:/FreemarkerTest/freemarker_test_01.html"));
            int copy = IOUtils.copy(inputStream, fileOutputStream);
            log.info("文件输出结果：" + copy);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //基于模板字符串生成静态化文件
    @Test
    public void test02() {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板内容
            String templateStr = "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <title>Hello World!</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<#-- 缺失变量默认值使用 ! ，例如：${name!} 表示如果name为空，则显示空字符串 -->\n" +
                    "Hello ${name!}!\n" +
                    "</body>\n" +
                    "</html>";
            //模板加载器
            StringTemplateLoader stl = new StringTemplateLoader();
            stl.putTemplate("template",templateStr);
            configuration.setTemplateLoader(stl);
            //得到模板
            Template template = configuration.getTemplate("template","UTF-8");
            //数据模型
            Map<String,String> map = new HashMap<>();
            map.put("name","天王盖地虎");
            //执行静态化，得到静态化之后的内容
            String templateContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            log.info("静态化之后的内容：" + templateContent);
            //将静态化后的内容转成输入流
            inputStream = IOUtils.toInputStream(templateContent);
            //通过文件输出流将静态化后的内容输出到指定目录的文件中
            fileOutputStream = new FileOutputStream(new File("E:/FreemarkerTest/freemarker_test_03.html"));
            int copy = IOUtils.copy(inputStream, fileOutputStream);
            log.info("文件输出结果：" + copy);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //map集合的几种遍历方式
    @Test
    public void test03() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "ab");
        map.put(4, "ab");
        map.put(4, "ab");// 和上面相同 ， 会自己筛选
        System.out.println(map.size());

        System.out.println("第一种：通过Map.keySet遍历key和value：");
        for (Integer key : map.keySet()) {
            //map.keySet()返回的是所有key的值
            String value = map.get(key);//得到每个key多对用value的值
            System.out.println(key + "     " + value);
        }

        System.out.println("第二种：通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        // 第三种：推荐，尤其是容量大时
        System.out.println("第三种：通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }

        System.out.println("第四种：通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("value= " + v);
        }
    }

}
