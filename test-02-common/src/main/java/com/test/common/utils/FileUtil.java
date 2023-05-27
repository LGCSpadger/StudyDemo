package com.test.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liugc 2022.07.11 19:19
 */
@Slf4j
public class FileUtil {

  /**
   * 文件上传至本地代码resources目录下
   * @param fileSavePath
   * @param multipartFile
   */
  public static void uploadFileToLocal(String fileSavePath,MultipartFile multipartFile) {
    try {
      File fileExist = new File(fileSavePath);
      if (!fileExist.exists()) {
        fileExist.mkdirs();
      }
      // 获取文件的名称
      String fileName = multipartFile.getOriginalFilename();
      // 获取文件对象
      File imgFile = new File(fileSavePath, fileName);
      // 完成文件的上传
      multipartFile.transferTo(imgFile);
      log.info("文件上传成功，上传路径：" + fileSavePath + File.separator + fileName);
    } catch (Exception e) {
      log.error("文件上传发生异常，异常信息为:{}",e);
    }
  }

  public static void deleteFileByNameAndPath(String filePath,String fileName) {
    File fileExist = new File(filePath);
    if (fileExist.exists()) {
      File[] files = fileExist.listFiles();
      if (files != null && files.length > 0) {
        for (int i = 0; i < files.length; i++) {
          String currFileName = files[i].getName();
          if (fileName.equals(currFileName)) {
            files[i].delete();
          }
        }
      }
    }
  }

  public static byte[] queryFileByNameAndPath(String filePath,String fileName) throws IOException {
    byte[] bytes = null;
    File fileExist = new File(filePath);
    if (fileExist.exists()) {
      File[] files = fileExist.listFiles();
      if (files != null && files.length > 0) {
        for (int i = 0; i < files.length; i++) {
          String currFileName = files[i].getName();
          if (fileName.equals(currFileName)) {
            BufferedImage read = ImageIO.read(new FileInputStream(files[i]));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String fileSub = currFileName.substring(currFileName.lastIndexOf(".") + 1);
            ImageIO.write(read, fileSub, out);
            bytes = out.toByteArray();
            break;
          }
        }
      }
    }
    return bytes;
  }

  public static void downloadFileFromLocal(String filePath,String fileName, HttpServletResponse response) {
    FileInputStream fis = null;
    ServletOutputStream sos = null;
    try {
      //设置响应头
      response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
      File fileExist = new File(filePath);
      if (fileExist.exists()) {
        File[] files = fileExist.listFiles();
        if (files != null && files.length > 0) {
          for (int i = 0; i < files.length; i++) {
            String currFileName = files[i].getName();
            if (fileName.equals(currFileName)) {
              ClassPathResource classPathResource = new ClassPathResource(filePath);
              fis = new FileInputStream(classPathResource.getFile());
              sos = response.getOutputStream();
              IOUtils.copy(fis, sos);
              break;
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
        if (sos != null) {
          sos.flush();
          sos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
