package com.test.freemarker.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

/**
 * 使用freemarker导出数据生成word文档工具类
 */
@Slf4j
public class FreemarkerToWordUtil {

    public static void exportDataToWord(Map<String,Object> dataMap, String fileName) {
        Writer out = null;
        FileOutputStream fos = null;
        try {
            //创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            Template template = null;
            //设置模板路径
            String classPath = FreemarkerToWordUtil.class.getResource("/templates").getPath();//获取 resources 中 templates 目录的绝对路径
            log.info("模板路径：" + classPath);
            configuration.setDirectoryForTemplateLoading(new File(classPath));
            //设置字符集
            configuration.setDefaultEncoding("UTF-8");
            //加载模板
            template = configuration.getTemplate("希望中学六年级学生成绩表.ftl");
            fos = new FileOutputStream(new File(fileName));
            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");
            out = new BufferedWriter(oWriter);
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
