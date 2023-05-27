package com.test.fastdfs;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.test.fastdfs.utils.FastdfsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class FastdfsTest {

    @Autowired
    private FastdfsUtil fastdfsUtil;

    //从本地上传文件至fastdfs
    @Test
    public void test01() {
        String picResult = fastdfsUtil.uploadFileByFilePath("H:\\test\\test002.png");//上传图片
        String excResult = fastdfsUtil.uploadFileByFilePath("H:\\test\\OMC1.xls");//上传Excel
        System.out.println("图片上传结果：" + picResult);
        System.out.println("Excel上传结果：" + excResult);
    }

    //查询文件信息
    @Test
    public void test02() {
        FileInfo picResult = fastdfsUtil.getFileInfo("group1",
                "M00/00/00/wKgKC2Qva3yAEOPqAACGiVZTJrQ991.png");//查询图片信息
        FileInfo excResult = fastdfsUtil.getFileInfo("group1",
                "M00/00/00/wKgKC2Qva3yAMQrqAAn-AMxh_Kg508.xls");//查询Excel信息
        System.out.println("图片查询结果：" + picResult);
        System.out.println("Excel查询结果：" + excResult);
    }

    //下载文件至本地文件夹
    @Test
    public void test03() {
        boolean picResult = fastdfsUtil.downloadFileToLocal("group1",
                "M00/00/00/wKgKC2Qva3yAEOPqAACGiVZTJrQ991.png", "H:\\临时",
                "下载的图片文件.png");//下载图片，可以重新命个名
        boolean excResult = fastdfsUtil.downloadFileToLocal("group1",
                "M00/00/00/wKgKC2Qva3yAMQrqAAn-AMxh_Kg508.xls", "H:\\临时",
                "OMC1.xls");//下载Excel
        System.out.println("图片下载结果：" + picResult);
        System.out.println("Excel下载结果：" + excResult);
    }

}