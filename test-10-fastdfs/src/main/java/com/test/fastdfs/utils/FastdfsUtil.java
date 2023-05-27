package com.test.fastdfs.utils;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * FastDFS 文件上传、下载工具类
 */
@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class FastdfsUtil {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 上传文件（选中指定的文件直接上传）
     * @param multipartFile 将要上传的文件
     * @return
     */
    public String upload(MultipartFile multipartFile) {
        String fileId =  null;
        try {
            if (multipartFile != null) {
                String originalFilename = multipartFile.getOriginalFilename().
                        substring(multipartFile.getOriginalFilename().
                                lastIndexOf(".") + 1);
                log.info("将要上传的文件类型：" + originalFilename);
                StorePath storePath = storageClient.uploadFile( multipartFile.getInputStream(),
                        multipartFile.getSize(),originalFilename , null);
                if (storePath != null) {
                    fileId = storePath.getFullPath();
                    log.info("文件上传成功！！！远程文件名：" + fileId);
                }
            }
        } catch (Exception e) {
            log.info("文件上传失败！！！");
            e.printStackTrace();
        }
        return fileId;
    }

    /**
     * 上传文件（给定本地文件的路径，通过文件路径找到文件并上传）
     * @param filePath  文件路径（绝对路径）
     * @return
     */
    public String uploadFileByFilePath(String filePath) {
        String fileId =  null;
        try {
            log.info("将要上传的文件路径：" + filePath);
            if (filePath != null) {
                //获取文件扩展名
                String fileExtName = filePath.substring(filePath.lastIndexOf(".") + 1);
                //上传文件
                File file = new File(filePath);
                FileInputStream inputStream = new FileInputStream(file);
                StorePath storePath = storageClient.uploadFile(inputStream, file.length(),
                        fileExtName, null);
                if (storePath == null) {
                    log.info("文件上传失败！！！");
                } else {
                    fileId = storePath.getFullPath();
                    log.info("文件上传成功！！！");
                }
            }
        } catch (Exception e) {
            log.info("文件上传失败！！！");
            e.printStackTrace();
        }
        return fileId;
    }

    /**
     * 查询文件相关信息
     * @param groupName         组名
     * @param remoteFilename    服务器上的远程文件名，例如: M00/00/00/wKgqHVty2ZCAHaBvAAE0vHMtwgw608.png
     * @return
     */
    public FileInfo getFileInfo(String groupName, String remoteFilename) {
        log.info("根据文件组名：" + groupName + "，文件名：" + remoteFilename + " 查询文件信息");
        FileInfo fileInfo = null;
        try {
            if (StringUtils.isNotEmpty(groupName) && StringUtils.isNotEmpty(remoteFilename)) {
                fileInfo = storageClient.queryFileInfo(groupName, remoteFilename);
                if (fileInfo != null) {
                    log.info("文件信息查询成功");
                }
            }
        } catch (Exception e) {
            log.info("文件信息查询失败！！！");
            e.printStackTrace();
        }
        return fileInfo;
    }

    /**
     * 通过浏览器下载文件
     * @param response
     * @param filePath          文件的临时存储路径
     * @param fileName          下载后的文件名
     * @param groupName         组名
     * @param remoteFilename    fastdfs服务器上的远程文件名，例如: M00/00/00/wKgqHVty2ZCAHaBvAAE0vHMtwgw608.png
     * @return
     */
    public void downloadFile(HttpServletResponse response, String filePath, String fileName, String groupName, String remoteFilename) {
        log.info("根据文件组名：" + groupName + "，文件名：" + remoteFilename + " 下载文件");
        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;
        try {
            if (StringUtils.isNotEmpty(groupName) && StringUtils.isNotEmpty(remoteFilename)) {
                byte[] bytes = storageClient.downloadFile(groupName, remoteFilename, new DownloadByteArray());
                if (bytes != null) {
                    log.info("文件信息查询成功");
                }
                File newPath = new File(filePath);
                newPath.mkdirs();
                if (!newPath.exists()) {
                    newPath.mkdirs();
                }
                File file = new File(filePath + "\\" + fileName);
                fos = new FileOutputStream(file);
                fos.write(bytes);
                // 设置响应头，控制浏览器下载该文件
                response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(),"iso-8859-1"));
                // 读取要下载的文件，保存到文件输入流
                in = new FileInputStream(filePath + "\\" + fileName);
                // 创建输出流
                out = response.getOutputStream();
                // 创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                // 循环将输入流中的内容读取到缓冲区当中
                while ((len = in.read(buffer)) > 0) {
                    // 输出缓冲区的内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
                //刷新
                out.flush();
                file.delete();
            }
        } catch (Exception e) {
            log.info("文件下载失败！！！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 下载文件至本地文件夹
     * @param groupName         组名
     * @param remoteFilename    fastdfs服务器上的远程文件名，例如: M00/00/00/wKgqHVty2ZCAHaBvAAE0vHMtwgw608.png
     * @param localPath         本地存储路径
     * @param fileName          本地文件名
     * @return
     */
    public boolean downloadFileToLocal(String groupName, String remoteFilename, String localPath, String fileName) {
        if (StringUtils.isEmpty(localPath)) {
            log.info("本地存储路径不能为空！！！");
            return false;
        }
        boolean result = false;
        FileOutputStream fos = null;
        try {
            if (StringUtils.isNotEmpty(groupName) && StringUtils.isNotEmpty(remoteFilename)) {
                byte[] bytes = storageClient.downloadFile(groupName, remoteFilename, new DownloadByteArray());
                File newPath = new File(localPath);
                newPath.mkdirs();
                if (!newPath.exists()) {
                    newPath.mkdirs();
                }
                File file = new File(localPath + "\\" + fileName);
                fos = new FileOutputStream(file);
                fos.write(bytes);
                result = true;
            }
        } catch (Exception e) {
            log.info("文件下载失败！！！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return result;
    }

}