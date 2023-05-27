package com.test.ftp;

import static org.junit.Assert.assertEquals;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import com.test.ftp.utils.FtpUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class FtpTest {

  @Autowired
  private FtpUtil ftpUtil;

  /**
   * 备注：
   * 1、ftp.pwd() 获取到的是用户的home目录
   * 2、如果vsftpd服务配置了多用户多目录权限，也就是配置了 local_root 目录的话，exist()、upload() 等方法中传入的路径都应该是相对路径，而不是绝对路径。
   * 例如：想要将文件上传至 /usr/local/ftp/data/spadger/ 目录下，但是配置了 local_root=/usr/local/ftp/data/spadger/ 的话，那 upload() 方法中传入的路径为 "" 即可
   * @throws IOException
   */
  @Test
  public void test01() throws IOException {
    String ftpDataPath = "/usr/local/ftp/data/";
    Ftp ftp = new Ftp("192.168.10.11",21,"spadger","spadger");
    String pwd = ftp.pwd();//显示的是用户的home目录
    System.out.println("pwd: " + pwd);
    boolean exist = ftp.exist(ftpDataPath);
    System.out.println("目录是否存在：" + exist);
//    boolean mkdir = ftp.mkdir("magic");
//    System.out.println("创建目录是否成功：" + mkdir);
//    boolean existOther = ftp.exist("/magic");
//    System.out.println("创建后目录是否存在：" + existOther);
//    ftp.cd(ftpDataPath + "spadger");
    boolean upload = ftp.upload("", FileUtil.file("H:\\test\\OMC1.xls"));
    System.out.println("上传结果：" + upload);
    ftp.close();
  }

  static String LOCAL_CHARSET = "GBK";
  static String SERVER_CHARSET = "ISO-8859-1";

  @Test
  public void test02() throws IOException {
    FTPClient ftpClient = new FTPClient();
    ftpClient.connect("192.168.10.11",21);
    ftpClient.login("spadger","spadger");
//    InputStream inputStream = ftpClient.retrieveFileStream("/usr/local/ftp/data/spadger/集合体系图.png");
    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
      LOCAL_CHARSET = "UTF-8";
    }
    ftpClient.setControlEncoding(LOCAL_CHARSET);
    FTPFile[] ftpFiles = ftpClient.listFiles();
    if (ftpFiles.length > 0) {
      for (FTPFile ff : ftpFiles) {
        System.out.println("222222222: " + ff.getName());
        if (ff.getName().equals("集合体系图.png")) {
          File localFile = null;
          localFile = new File("H:/test/" + new String(ff.getName().getBytes("ISO-8859-1"), "UTF-8"));
          boolean b = ftpClient
              .retrieveFile(ff.getName(), new FileOutputStream(localFile));
          System.out.println("11111111: " + b);
        }
      }
    }
  }

  /**
   * 文件上传
   */
  @Test
  public void test03() {
    FTPClient ftpClient = ftpUtil.connectFtp();
    boolean uploadResult = ftpUtil.uploadFile(ftpClient, "", "集合体系图.png", "H:\\test\\集合体系图.png");
    System.out.println("上传结果：" + uploadResult);
  }

  /**
   * 文件下载
   */
  @Test
  public void test04() {
    FTPClient ftpClient = ftpUtil.connectFtp();
    boolean downloadFile = ftpUtil.downloadFile(ftpClient, "", "集合体系图.png", "H:/test/");
    System.out.println("下载结果：" + downloadFile);
  }

  /**
   * 下载文件
   * @throws IOException
   */
  @Test
  public void test05() throws IOException {
    FTPClient ftpClient = new FTPClient();
    ftpClient.connect("192.168.10.11",21);
    boolean isFtpServer = FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
    assertEquals(true, isFtpServer);
    ftpClient.login("spadger","spadger");
    FTPFile[] files = ftpClient.listFiles();
    if (files.length > 0) {
      for (FTPFile file : files) {
        File localFile = new File("H:/test/" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
        System.out.println("要下载的文件名: " + localFile.getName());
        boolean b = ftpClient.retrieveFile(file.getName(), new FileOutputStream(localFile));
        System.out.println("下载结果: " + b);
      }
    }
    ftpClient.disconnect();
  }

  /**
   * 上传文件
   * @throws IOException
   */
  @Test
  public void test06() throws IOException {
    FTPClient ftpClient = new FTPClient();
    ftpClient.connect("192.168.10.11",21);
    boolean isFtpServer = FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
    assertEquals(true, isFtpServer);
    ftpClient.login("spadger","spadger");
    FileInputStream fileInputStreamE = new FileInputStream(new File("H:\\test\\OMC1.xls"));
    boolean storeResultE = ftpClient.storeFile(new String("OMC1.xls".getBytes("UTF-8"),"ISO-8859-1"), fileInputStreamE);
    System.out.println("英文名文件上传结果：" + storeResultE);
    fileInputStreamE.close();
    FileInputStream fileInputStreamC = new FileInputStream(new File("H:\\test\\集合体系图.png"));
    boolean storeResultC = ftpClient.storeFile(new String("集合体系图.png".getBytes("UTF-8"),"ISO-8859-1"), fileInputStreamC);
    System.out.println("中文名文件上传结果：" + storeResultC);
    fileInputStreamC.close();
    ftpClient.disconnect();
  }

}
