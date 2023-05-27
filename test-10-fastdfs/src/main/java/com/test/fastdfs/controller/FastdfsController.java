package com.test.fastdfs.controller;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.test.common.response.CommonCode;
import com.test.common.response.ResponseResult;
import com.test.fastdfs.utils.FastdfsUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fastdfs")
@Slf4j
public class FastdfsController {

    @Autowired
    private FastdfsUtil fastdfsUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile",headers="content-type=multipart/form-data")
    public String uploadFile (@RequestParam("file") MultipartFile file){
        log.info("文件上传，参数：{}" + file);
        String result;
        try{
            String path = fastdfsUtil.upload(file);
            if (!StringUtils.isEmpty(path)){
                result = path ;
            } else {
                result = "上传失败" ;
            }
            return result;
        } catch (Exception e){
            result = "调用发生异常";
            log.error("调用发生异常，异常信息为:{}",e);
            return "调用发生异常";
        }
    }

    /**
     * 文件下载
     * @param response
     * @param remoteFilename    服务器上的远程文件名，例如: M00/00/00/rBt3OGLdCj6ARaxwAAAoUmZ5gGY410.jpg
     */
    @GetMapping(value = "/downloadFile")
    public void downloadFile (HttpServletResponse response,String remoteFilename){
        log.info("文件下载，参数：" + remoteFilename);
        try{
            fastdfsUtil.downloadFile(response,"H:\\临时","topoconfig.sql","group1",remoteFilename);
        } catch (Exception e){
            log.error("调用发生异常，异常信息为:{}",e);
        }
    }

    /**
     * 文件信息查询
     * @param remoteFilename    服务器上的远程文件名，例如: M00/00/00/rBt3OGLdCj6ARaxwAAAoUmZ5gGY410.jpg
     * @return
     */
    @GetMapping(value = "/queruFileInfo")
    public FileInfo queruFileInfo (String remoteFilename){
        log.info("文件信息查询，参数：" + remoteFilename);
        FileInfo fileInfo = null;
        try{
            fileInfo = fastdfsUtil.getFileInfo("group1", remoteFilename);
        } catch (Exception e){
            log.error("调用发生异常，异常信息为:{}",e);
        }
        return fileInfo;
    }

}
