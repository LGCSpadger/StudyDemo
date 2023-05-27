package com.test.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    /**
     * 打包
     *
     * @param dir            要打包的目录
     * @param zipFile        打包后的文件路径
     * @throws Exception
     */
    public static void zip(String dir, String zipFile) throws Exception {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File sourceFile = new File(dir);
            out.putNextEntry(new ZipEntry(sourceFile.getName()));
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                IOUtils.copy(in, out);
            } catch (Exception e) {
                throw new RuntimeException("打包异常: " + e.getMessage());
            }
        }
    }

    /**
     * 打包
     *
     * @param dir            要打包的目录
     * @param zipFile        打包后的文件路径
     * @param includeBaseDir 是否包括最外层目录
     * @throws Exception
     */
    public static void zipDir(String dir, String zipFile, boolean includeBaseDir) throws Exception {
        if (zipFile.startsWith(dir)) {
            throw new RuntimeException("打包生成的文件不能在打包目录中");
        }
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File fileDir = new File(dir);
            String baseDir = "";
            if (includeBaseDir) {
                baseDir = fileDir.getName();
            }
            compress(out, fileDir, baseDir);
        }
    }

    public static void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {
        if (sourceFile.isDirectory()) {
            base = base.length() == 0 ? "" : base + File.separator;
            File[] files = sourceFile.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                // todo 打包空目录
                // out.putNextEntry(new ZipEntry(base));
                return;
            }
            for (File file : files) {
                compress(out, file, base + file.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                IOUtils.copy(in, out);
            } catch (Exception e) {
                throw new RuntimeException("打包异常: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        String dir = "F:/test/测试文件local.xlsx";
        String dir = "F:/test";
//        String zipPath = "F:/test/target.zip";
        String zipPath = "F:/template/target.zip";
//        zip(dir, zipPath);
        zipDir(dir,zipPath,false);
    }

}
