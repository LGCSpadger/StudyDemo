package com.test.common.utils;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class DataExportToWordUtil {

    private final Logger log = LoggerFactory.getLogger(DataExportToWordUtil.class);

    /**
     * 使用poi动态生成word（docx格式），根据模板将数据动态填充进去，并通过浏览器下载此文件
     * @param response
     * @param templatePath  word模板路径
     * @param fileName  生成的word文件名称。例如：fileName = "测试文件.docx"
     * @param temporarySavePath 生成的word文件临时存储路径，浏览器下载完成后可以将此文件路径中的文件删除。例如：temporarySavePath = "F:/test"
     * @param data  word文件中所需的数据（Map格式）
     * @param bindListNames 表格对应的数据集的名称集合，word当中包含多少个表格，每个表格对应的数据集可能都是不一样的，每个表格都会对应一个数据集，将所有数据集的名称放进该集合，作为参数传递过来，然后一一进行绑定
     */
    public void exportToWord(HttpServletResponse response, String templatePath, String fileName, String temporarySavePath, Map<String,Object> data, List<String> bindListNames) {
        XWPFTemplate template = null;
        FileOutputStream fos = null;
        OutputStream out = null;
        try {
            //1.根据模板文件所在的路径加载模板文件
            ClassPathResource classPathResource = new ClassPathResource(templatePath);
            String resource = classPathResource.getURL().getPath();
            //2.根据模板填充数据
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            ConfigureBuilder builder = Configure.builder();
            if (bindListNames != null && bindListNames.size() > 0) {
                for (int i = 0; i < bindListNames.size(); i++) {
                    builder.bind(bindListNames.get(i), policy);//绑定表格对应的数据集合名称，word当中包含多少个表格，就要绑定多少次，每个表格对应的数据集都要通过集合名称进行绑定
                }
            }
            Configure configure = builder.build();
            //渲染数据
            template = XWPFTemplate.compile(resource, configure).render(data);
            //3.生成文件保存至临时存储路径中
            fos = new FileOutputStream(temporarySavePath + File.separator + fileName);
            template.write(fos);
            //4.通过浏览器下载保存在临时存储路径中的文件
            //设置强制下载不打开
            response.setContentType("application/force-download");
            //设置文件名，这里文件名不能使用默认编码，否则浏览器下载后文件名会乱码
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(),"iso-8859-1"));
            out = response.getOutputStream();
            template.write(out);
            out.flush();
        } catch (Exception e) {
            log.info("生成word过程中出现异常，异常原因：" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeAll(template,fos,out);
        }
    }

    /**
     * 使用poi动态生成word（docx格式），根据模板将数据动态填充进去，并保存到指定的本地文件夹中
     * @param templatePath  word模板路径
     * @param fileName  生成的word文件名称。例如：fileName = "测试文件.docx"
     * @param temporarySavePath 生成的word文件本地存储路径。例如：temporarySavePath = "F:/test"
     * @param data  word文件中所需的数据（Map格式）
     * @param bindListNames 表格对应的数据集的名称集合，word当中包含多少个表格，每个表格对应的数据集可能都是不一样的，每个表格都会对应一个数据集，将所有数据集的名称放进该集合，作为参数传递过来，然后一一进行绑定
     */
    public void exportToWordAndSaveToLocal(String templatePath, String fileName, String temporarySavePath, Map<String,Object> data, List<String> bindListNames) {
        XWPFTemplate template = null;
        FileOutputStream fos = null;
        try {
            //1.根据模板文件所在的路径加载模板文件
            ClassPathResource classPathResource = new ClassPathResource(templatePath);
            String resource = classPathResource.getURL().getPath();
            //2.根据模板填充数据
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            ConfigureBuilder builder = Configure.builder();
            if (bindListNames != null && bindListNames.size() > 0) {
                for (int i = 0; i < bindListNames.size(); i++) {
                    builder.bind(bindListNames.get(i), policy);//绑定表格对应的数据集合名称，word当中包含多少个表格，就要绑定多少次，每个表格对应的数据集都要通过集合名称进行绑定
                }
            }
            Configure configure = builder.build();
            //渲染数据
            template = XWPFTemplate.compile(resource, configure).render(data);
            //3.生成文件保存至临时存储路径中
            fos = new FileOutputStream(temporarySavePath + File.separator + fileName);
            template.write(fos);
        } catch (Exception e) {
            log.info("生成word过程中出现异常，异常原因：" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeAll(template,fos,null);
        }
    }

    /**
     * 关闭所有资源
     * @param template
     * @param fos
     * @param out
     */
    private void closeAll(XWPFTemplate template,FileOutputStream fos,OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (template != null) {
            try {
                template.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
