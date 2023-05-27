package com.test.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.test.common.entity.User;
import com.test.common.utils.ListUtil;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class MyPublicTest {

    //使用 groovy 执行动态的java脚本
    @Test
    public void test01() throws IllegalAccessException, InstantiationException {
        String output = "{\"pm_8431315360263108\":\"{\\\"transactionId\\\":\\\"\\\",\\\"code\\\":\\\"0\\\",\\\"message\\\":\\\"成功\\\",\\\"result\\\":[{\\\"PROJ_ID\\\":\\\"1GS-黔汇特产美食城门口LHYC\\\",\\\"INFO_TITLE\\\":null,\\\"START_TIME\\\":1590049661,\\\"END_TIME\\\":1592126075,\\\"NE_NAME\\\":\\\"1GS-黔汇特产美食城门口LDHY-JC-8\\\",\\\"START_TIME_STR\\\":\\\"2020-05-21 16:27:41\\\",\\\"END_TIME_STR\\\":\\\"2020-06-14 17:14:35\\\"}]}\"}";
        String classText = "import com.alibaba.fastjson.JSONObject;\n"
            + "import com.google.gson.Gson;\n"
            + "import com.google.gson.GsonBuilder;\n"
            + "\n"
            + "import java.util.ArrayList;\n"
            + "import java.util.HashMap;\n"
            + "import java.util.List;\n"
            + "import java.util.Map;\n"
            + "\n"
            + "class MyAnalysis{\n"
            + "  private Gson gson = new GsonBuilder().serializeNulls().create();\n"
            + "  String startexec(Map<String,Object> param){\n"
            + "    System.out.println(param.get(\"pm_8431315360263108\"));\n"
            + "    Map output = JSONObject.parseObject(param.get(\"pm_8431315360263108\").toString(), Map.class);\n"
            + "    List<Map> heads = new ArrayList<>();\n"
            + "    Map d1 = new HashMap();\n"
            + "    d1.put(\"field_desc\",\"工程id\");\n"
            + "    d1.put(\"field_name\",\"PROJ_ID\");\n"
            + "    heads.add(d1);\n"
            + "    d1 = new HashMap();\n"
            + "    d1.put(\"field_desc\",\"网元名称\");\n"
            + "    d1.put(\"field_name\",\"NE_NAME\");\n"
            + "    heads.add(d1);\n"
            + "    d1 = new HashMap();\n"
            + "    d1.put(\"field_desc\",\"开始时间\");\n"
            + "    d1.put(\"field_name\",\"START_TIME_STR\");\n"
            + "    heads.add(d1);\n"
            + "    d1 = new HashMap();\n"
            + "    d1.put(\"field_desc\",\"结束时间\");\n"
            + "    d1.put(\"field_name\",\"END_TIME_STR\");\n"
            + "    heads.add(d1);\n"
            + "    d1 = new HashMap();\n"
            + "    d1.put(\"field_desc\",\"告警标题\");\n"
            + "    d1.put(\"field_name\",\"INFO_TITLE\");\n"
            + "    heads.add(d1);\n"
            + "\n"
            + "    List<Map>  result = (List<Map>) output.get(\"result\");\n"
            + "    param.put(\"heads\",heads);\n"
            + "    param.put(\"result\",result);\n"
            + "    return  gson.toJson(param);\n"
            + "  }\n"
            + "}";
        CompilerConfiguration config = new CompilerConfiguration();
        GroovyClassLoader groovyClassLoader;
        config.setSourceEncoding("UTF-8");
        groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
        Class<?> groovyClass = groovyClassLoader.parseClass(classText);
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
        Map<String,Object> map = JSONObject.parseObject(output, Map.class);
        log.info("参数：{}" + map);
        Object startexec = groovyObject.invokeMethod("startexec", new Object[]{map});
        log.info("java脚本执行结果：{}" + startexec);
        System.out.println();
    }

    // List<Map>中 根据map的某个key去重
    @Test
    public void test02() {
//        Map<String, Map> msp = new HashMap<String, Map>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "1123");
        map1.put("name", "张三");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "2");
        map2.put("name", "李四");
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("id", "1123");
        map3.put("name", "王五");
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("id", "3");
        map4.put("name", "王五");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        List<User> list01 = new ArrayList<>();
        User user01 = new User();
        user01.setId(1L);
        user01.setName("张三");
        User user02 = new User();
        user02.setId(2L);
        user02.setName("李四");
        User user03 = new User();
        user03.setId(3L);
        user03.setName("张三");
        User user04 = new User();
        user04.setId(4L);
        user04.setName("张三");
        list01.add(user01);
        list01.add(user02);
        list01.add(user03);
        list01.add(user04);

        List<Map<String, Object>> mapList01 = ListUtil.listMapDistByKey(list, "name");
//        List<Map<String, Object>> mapList02 = ListUtil.listDistStremByKeyMap(list01, "name");
    }

    //json数据格式化
    @Test
    public void test03() {
        String jsonString = "{\"_index\":\"book_shop\",\"_type\":\"it_book\",\"_id\":\"1\",\"_score\":1.0," +
            "\"_source\":{\"name\": \"Java编程思想（第4版）\",\"author\": \"[美] Bruce Eckel\",\"category\": \"编程语言\"," +
            "\"price\": 109.0,\"publisher\": \"机械工业出版社\",\"date\": \"2007-06-01\",\"tags\": [ \"Java\", \"编程语言\" ]}}";

        JSONObject object = JSONObject.parseObject(jsonString);
        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteDateUseDateFormat);

        System.out.println(pretty);
    }

    @Test
    public void testTemp() throws IOException {
        String jsonString = "{\"_index\":\"book_shop\",\"_type\":\"it_book\",\"_id\":\"1\",\"_score\":1.0," +
            "\"_source\":{\"name\": \"Java编程思想（第4版）\",\"author\": \"[美] Bruce Eckel\",\"category\": \"编程语言\"," +
            "\"price\": 109.0,\"publisher\": \"机械工业出版社\",\"date\": \"2007-06-01\",\"tags\": [ \"Java\", \"编程语言\" ]}}";

        JSONObject object = JSONObject.parseObject(jsonString);
        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteDateUseDateFormat);

        System.out.println(pretty);
    }


}
