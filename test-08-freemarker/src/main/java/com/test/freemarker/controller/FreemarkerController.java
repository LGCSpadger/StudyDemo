package com.test.freemarker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.test.freemarker.entity.Student;
import com.test.freemarker.utils.FreemarkerToWordUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FreemarkerController {

    //插值，即 ${..}
    @RequestMapping(value = "/test01", method = RequestMethod.GET)
    public String test01(Map<String,Object> map) {
        map.put("name","天王盖地虎");
        return "freemarker_test_01";
    }

    //遍历表格数据
    @RequestMapping(value = "/test02", method = RequestMethod.GET)
    public String test02(Map<String,Object> map) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Student student01 = new Student();
            student01.setName("唐三");
            student01.setAge(19);
            student01.setMoney(200.10f);
            student01.setBirthday(sdf.parse("2012-01-05"));
            Student student02 = new Student();
            student02.setName("小舞");
            student02.setAge(18);
            student02.setMoney(180.10f);
            student02.setBirthday(sdf.parse("2011-05-05"));
            Student student03 = new Student();
            student03.setName("朱竹青");
            student03.setAge(18);
            student03.setMoney(140.10f);
            student03.setBirthday(sdf.parse("2011-02-05"));
            Student student04 = new Student();
            student04.setName("奥斯卡");
            student04.setAge(20);
            student04.setMoney(150.10f);
            student04.setBirthday(sdf.parse("2010-06-09"));
            Student student05 = new Student();
            student05.setName("戴沐白");
            student05.setAge(22);
            student05.setMoney(190.80f);
            student05.setBirthday(sdf.parse("2008-05-03"));
            List<Student> tsFriends = Arrays.asList(student02,student03,student04,student05);
            List<Student> xwFriends = Arrays.asList(student01,student03,student04,student05);
            List<Student> zzqFriends = Arrays.asList(student01,student02,student04,student05);
            List<Student> askFriends = Arrays.asList(student01,student02,student03,student05);
            List<Student> dmbFriends = Arrays.asList(student01,student02,student03,student04);

            student01.setFriends(tsFriends);
            student02.setFriends(xwFriends);
            student03.setFriends(zzqFriends);
            student04.setFriends(askFriends);
            student05.setFriends(dmbFriends);

            student01.setBestFriend(student02);
            student02.setBestFriend(student01);
            student03.setBestFriend(student05);
            student04.setBestFriend(student01);
            student05.setBestFriend(student03);

            List<Student> studentList = Arrays.asList(student01,student02,student03,student04,student05);

            Map<String,Student> studentMap = new HashMap<>();
            studentMap.put("student01",student01);
            studentMap.put("student02",student02);
            studentMap.put("student03",student03);

            Date today = new Date();

            map.put("studentList",studentList);
            map.put("studentMap",studentMap);
            map.put("today",today);
            map.put("point",125546857);
            //JSON.toJSONString() 方法默认会过滤掉 值为null 的字段，设置 SerializerFeature.WriteMapNullValue 可以保留 值为null 的字段
            map.put("jsonData", JSON.toJSONString(student03, SerializerFeature.WriteMapNullValue));
        } catch (Exception e){
            e.printStackTrace();
        }
        return "freemarker_test_02";
    }

    //使用 freemarker 导出数据，生成 word 文档
    @RequestMapping(value = "/test03", method = RequestMethod.GET)
    @ResponseBody
    public void test03() {
        try {
            Student student01 = new Student();
            student01.setName("唐三");
            student01.setGrade("一");
            student01.setSclass("二");
            student01.setChinese(85);
            student01.setMath(100);
            student01.setEnglish(96);
            Student student02 = new Student();
            student02.setName("小舞");
            student02.setGrade("一");
            student02.setSclass("三");
            student02.setChinese(97);
            student02.setMath(95);
            student02.setEnglish(98);
            List<Student> stuList = Arrays.asList(student01,student02);
            Map<String,Object> map = new HashMap<>();
            map.put("stuList",stuList);
            FreemarkerToWordUtil.exportDataToWord(map,"E:/FreemarkerTest/希望中学六年级学生成绩表.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
