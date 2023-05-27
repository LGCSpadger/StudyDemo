package com.test.ftp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FtpUtil {

  @Value("${ftp.host}")
  private String host;

  @Value("${ftp.port}")
  private int port;

  @Value("${ftp.username}")
  private String username;

  @Value("${ftp.password}")
  private String password;

  private FTPClient ftpClient = null;

  /**
   * 创建连接
   * @return
   */
  public FTPClient connectFtp() {
    try {
      ftpClient = new FTPClient();
      ftpClient.connect(host,port);
      if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        log.info("连接FTP失败，用户名或密码错误！");
        ftpClient.disconnect();
      } else {
        log.info("FTP连接成功!");
      }
      ftpClient.login(username,password);
    } catch (Exception e) {
      log.error("登陆FTP失败，请检查FTP相关配置信息是否正确！错误信息：{}", e);
      return null;
    }
    return ftpClient;
  }

  /**
   * 关闭FTP连接
   *
   * @param ftpClient
   */
  public void closeFtpClient(FTPClient ftpClient) {
    if (ftpClient.isConnected()) {
      try {
        ftpClient.disconnect();
      } catch (Exception e) {
        log.error("未能正常关闭连接！异常信息：{}", e);
      }
    }
  }

  /**
   * 按前后缀查询文件
   * @param facepic
   * @param prefix
   * @param suffix
   * @return
   */
  public List<String> showPatternFiles(String facepic, String prefix, String suffix) {
    List<String> ret = new ArrayList();
    String fileName = null;
    FTPClient ftpClient = null;
    try {
      ftpClient = connectFtp();
      boolean changeFlag = ftpClient.changeWorkingDirectory(facepic);
      if (!changeFlag) {
        throw new IOException("进入Ftp目录" + facepic + "失败");
      }
      FTPFile[] files = ftpClient.listFiles(facepic);
      for (FTPFile ftpFile : files) {
        fileName = ftpFile.getName();
        if ((!".".equals(fileName)) && (!"..".equals(fileName))) {
          String regEx = null;
          if (StringUtils.isNotBlank(prefix)) {
            regEx = prefix + "*." + suffix;
          } else {
            regEx = suffix + "$";
          }
          Pattern pattern = Pattern.compile(regEx);
          if (pattern.matcher(fileName).find()) {
            ret.add(fileName);
          }
        }
      }
    } catch (Exception e) {
      log.error("获取文件失败", e);
    } finally {
      if (ftpClient != null) {
        //close(ftpClient);
      }
    }
    return ret;
  }


  /**
   * 从FTP下载文件到本地
   * @param ftpClient
   * @param remotePath    ftp服务器文件路径，如果配置了多用户多目录权限后，使用相对路径即可。例如：ftp配置了 local_root=/usr/local/ftp/data/spadger/，现在想要从 /usr/local/ftp/data/spadger/test/ 中下载文件，那这里路径传 test 即可；如果想要从 /usr/local/ftp/data/spadger/ 中下载文件，那这里路径传 "" 即可。
   * @param fileName      文件名。例如：集合体系图.png
   * @param downloadPath  下载到本地的路径。例如：H:/test/
   * @return
   */
  public boolean downloadFile(FTPClient ftpClient, String remotePath, String fileName, String downloadPath) {
    boolean downloadResult = false;
    try {
      FTPFile[] files = ftpClient.listFiles();
      if (files.length > 0) {
        for (FTPFile file : files) {
          String remoteFileName = new String(file.getName().getBytes("ISO-8859-1"), "UTF-8");
          log.info("将要下载的文件名称：{}", remoteFileName);
          log.info("传入的文件名：{}", fileName);
          if (remoteFileName.equals(fileName)) {
            File localFile = new File(downloadPath + remoteFileName);
            downloadResult = ftpClient.retrieveFile(file.getName(), new FileOutputStream(localFile));
            break;
          }
        }
      }
      log.info("FTP文件下载结果：{}", downloadResult);
    } catch (Exception e) {
      log.error("FTP文件下载失败！异常信息：{}", e);
    } finally {
      try {
        ftpClient.disconnect();
      } catch (IOException e) {
        log.error("FTP客户端关闭异常！异常信息：{}", e);
      }
    }
    return downloadResult;
  }

  /**
   * 上传文件
   *
   * @param serviceDec     ftp服务保存地址
   * @param fileName       上传到ftp的文件名
   * @param originfilename 待上传文件的名称（绝对地址） *
   * @return
   */
  public boolean uploadFile(FTPClient ftpClient, String serviceDec, String fileName, String originfilename) {
    log.info("开始上传文件");
    try (InputStream input = new FileInputStream(new File(originfilename))) {
      return uploadFile(ftpClient, serviceDec, fileName, input);
    } catch (Exception e) {
      log.error("文件上传失败！失败信息：{}", e);
    }
    return false;
  }

  /**
   * 上传文件
   *
   * @param serviceDec  ftp服务保存地址，如果配置了多用户多目录权限后，使用相对路径即可。例如：ftp配置了 local_root=/usr/local/ftp/data/spadger/，现在想要上传文件至 /usr/local/ftp/data/spadger/test/ 中，那这里路径传 test 即可；如果想要上传文件至 /usr/local/ftp/data/spadger/ 中，那这里路径传 "" 即可。
   * @param fileName    上传到ftp的文件名。例如：集合体系图.png
   * @param inputStream 输入文件流
   * @return
   */
  private boolean uploadFile(FTPClient ftpClient, String serviceDec, String fileName, InputStream inputStream) {
    try {
      log.info("开始上传文件");
      ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
      createDirecroty(ftpClient, serviceDec);
      ftpClient.makeDirectory(serviceDec);
      ftpClient.changeWorkingDirectory(serviceDec);
      ftpClient.storeFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"), inputStream);
      inputStream.close();
      ftpClient.logout();
      log.info("上传文件成功");
    } catch (Exception e) {
      log.error("上传文件异常！异常信息：{}", e);
    } finally {
      try {
        if (ftpClient.isConnected()) {
          ftpClient.disconnect();
        }
        if (null != inputStream) {
          inputStream.close();
        }
      } catch (IOException e) {
        log.error("FTP客户端关闭异常！异常信息：{}", e);
        return false;
      }
    }
    return true;
  }

  //改变目录路径
  private boolean changeWorkingDirectory(FTPClient ftpClient, String directory) {
    boolean flag = true;
    try {
      flag = ftpClient.changeWorkingDirectory(directory);
      if (flag) {
        log.info("进入文件夹" + directory + " 成功！");

      } else {
        log.info("进入文件夹" + directory + " 失败！开始创建文件夹");
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return flag;
  }

  //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
  private boolean createDirecroty(FTPClient ftpClient, String remote) throws IOException {
    boolean success = true;
    String directory = remote + "/";
    // 如果远程目录不存在，则递归创建远程服务器目录
    if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(ftpClient, new String(directory))) {
      int start = 0;
      int end = 0;
      if (directory.startsWith("/")) {
        start = 1;
      } else {
        start = 0;
      }
      end = directory.indexOf("/", start);
      String path = "";
      String paths = "";
      while (true) {
        String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
        path = path + "/" + subDirectory;
        if (!existFile(ftpClient, path)) {
          if (makeDirectory(ftpClient, subDirectory)) {
            changeWorkingDirectory(ftpClient, subDirectory);
          } else {
            log.info("创建目录[" + subDirectory + "]失败");
            changeWorkingDirectory(ftpClient, subDirectory);
          }
        } else {
          changeWorkingDirectory(ftpClient, subDirectory);
        }

        paths = paths + "/" + subDirectory;
        start = end + 1;
        end = directory.indexOf("/", start);
        // 检查所有目录是否创建完毕
        if (end <= start) {
          break;
        }
      }
    }
    return success;
  }

  //判断ftp服务器文件是否存在
  private boolean existFile(FTPClient ftpClient, String path) throws IOException {
    boolean flag = false;
    FTPFile[] ftpFileArr = ftpClient.listFiles(path);
    if (ftpFileArr.length > 0) {
      flag = true;
    }
    return flag;
  }

  //创建目录
  private boolean makeDirectory(FTPClient ftpClient, String dir) {
    boolean flag = true;
    try {
      flag = ftpClient.makeDirectory(dir);
      if (flag) {
        log.info("创建文件夹" + dir + " 成功！");

      } else {
        log.info("创建文件夹" + dir + " 失败！");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * 获取FTP某一特定目录下的所有文件名称
   *
   * @param ftpClient  已经登陆成功的FTPClient
   * @param ftpDirPath FTP上的目标文件路径
   */
  public List<String> getFileNameList(FTPClient ftpClient, String ftpDirPath) {
    List<String> list = new ArrayList();
    try {

      // 通过提供的文件路径获取FTPFile对象列表
      FTPFile[] files = ftpClient.listFiles(ftpDirPath);
      // 遍历文件列表，打印出文件名称
      for (int i = 0; i < files.length; i++) {
        FTPFile ftpFile = files[i];
        // 此处只打印文件，未遍历子目录（如果需要遍历，加上递归逻辑即可）
        if (ftpFile.isFile()) {
//                        log.info(ftpDirPath + ftpFile.getName());
          list.add(ftpFile.getName());
        }
      }
    } catch (IOException e) {
      log.error("错误" + e);
    }
    return list;
  }

  /**
   * 获取到服务器文件夹里面最新创建的文件名称
   *
   * @param ftpDirPath 文件路径
   * @param ftpClient  ftp的连接
   * @return fileName
   */
  public String getNewFile(FTPClient ftpClient, String ftpDirPath) throws Exception {

    // 通过提供的文件路径获取FTPFile对象列表
    FTPFile[] files = ftpClient.listFiles(ftpDirPath);
    if (files == null) {
      throw new Exception("文件数组为空");
    }
    Arrays.sort(files, new Comparator<FTPFile>() {
      @Override
      public int compare(FTPFile f1, FTPFile f2) {
        return f1.getTimestamp().compareTo(f2.getTimestamp());
      }

      public boolean equals(Object obj) {
        return true;
      }
    });
    return ftpDirPath + "/" + files[files.length - 1].getName();

  }


}
