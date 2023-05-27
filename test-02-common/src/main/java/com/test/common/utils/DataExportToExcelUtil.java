package com.test.common.utils;

import com.test.common.entity.PoiModel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class DataExportToExcelUtil<T> {

    private final Logger log = LoggerFactory.getLogger(DataExportToExcelUtil.class);

    /**
     *  数据导出到excel中，并通过浏览器下载
     *  备注：该方法只能生成 xls 文件，无法生成 xlsx 文件
     *
     * @param response
     * @param sheetName 导出的excel的sheet页的名称
     * @param filePath  文件的存储路径(临时存储路径，浏览器下载完文件之后会删除该文件)
     * @param fileName  导出的exce的文件名称
     * @param tableHeader   导出的表格的头部，每一列的名称
     * @param data  所有的数据集
     */
    public void dataWriteToExcelXls(HttpServletResponse response, String sheetName, String filePath, String fileName, List<String> tableHeader, Collection<T> data) {
        //生成excel文档对象
        HSSFWorkbook workBook = new HSSFWorkbook();
        //创建工作簿
        HSSFSheet mySheet = workBook.createSheet();
        mySheet.setDefaultColumnWidth(15);//设置单元格的默认宽度
        mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
        //设置工作簿的名字
        workBook.setSheetName(0, sheetName);
        //创建第一行，标题行
        int rowNomber = -1;
        HSSFRow myRow = mySheet.createRow(++rowNomber);
        HSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
        headStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());// 前景色设置 ①
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 填充模式 设置 ② 备注：① + ② 两步才能设置背景颜色
        headStyle.setWrapText(true);// 自动换行
        headStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//单元格上下居中
        headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        HSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
        bodyStyle.setWrapText(true);// 自动换行
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);//单元格上下居中
        bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        //设置字体样式
        HSSFFont headFont = workBook.createFont();
        headFont.setFontName("宋体");//设置字体
        headFont.setFontHeightInPoints((short) 10);//设置字体大小
        headFont.setBold(true);//设置字体是否加粗
        HSSFFont bodyFont = workBook.createFont();
        bodyFont.setFontName("宋体");//设置字体
        bodyFont.setFontHeightInPoints((short) 10);//设置字体大小
        headStyle.setFont(headFont);
        bodyStyle.setFont(bodyFont);

        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;

        try {
            //设置标题行，每一列的标题
            HSSFCell cell = null;
            if (tableHeader != null && tableHeader.size() > 0) {
                for (int i = 0; i < tableHeader.size(); i++) {
                    cell = myRow.createCell((short) i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(tableHeader.get(i));
                }
            }

            // 遍历集合数据，产生数据行
            Iterator<T> it = data.iterator();
            //添加数据
            while (it.hasNext()) {
                T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                //创建行
                HSSFRow  Row = mySheet.createRow(++rowNomber);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try
                    {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        // 判断值的类型后进行强制类型转换
                        String textValue = value == null ? "" : value.toString();
                        HSSFCell hssfCell = Row.createCell((short) i);
                        hssfCell.setCellStyle(bodyStyle);
                        hssfCell.setCellValue(textValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            File newPath = new File(filePath);
            newPath.mkdirs();
            if (!newPath.exists()) {
                newPath.mkdirs();
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
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
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     *  数据导出到excel中，并通过浏览器下载
     *  备注：该方法可以生成 xlsx 文件，加自定义样式，加了自定义样式文件会比较大
     *
     * @param response
     * @param sheetName 导出的excel的sheet页的名称
     * @param filePath  文件的存储路径(临时存储路径，浏览器下载完文件之后会删除该文件)
     * @param fileName  导出的exce的文件名称
     * @param tableHeader   导出的表格的头部，每一列的名称
     * @param data  所有的数据集
     */
    public void dataExportToExcelXlsxAddStyle(HttpServletResponse response, String sheetName, String filePath, String fileName, List<String> tableHeader, Collection<T> data) {
        //生成excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作簿
        XSSFSheet mySheet = workBook.createSheet();
        mySheet.setDefaultColumnWidth(15);//设置单元格的默认宽度
        mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
        //设置工作簿的名字
        workBook.setSheetName(0, sheetName);
        //创建第一行，标题行
        int rowNomber = -1;
        XSSFRow myRow = mySheet.createRow(++rowNomber);
        XSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
        headStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());// 前景色设置 ①
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 填充模式 设置 ② 备注：① + ② 两步才能设置背景颜色
        headStyle.setWrapText(true);// 自动换行
        headStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//单元格上下居中
        headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        XSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
        bodyStyle.setWrapText(true);// 自动换行
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);//单元格上下居中
        bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        //设置字体样式
        XSSFFont headFont = workBook.createFont();
        headFont.setFontName("宋体");//设置字体
        headFont.setFontHeightInPoints((short) 10);//设置字体大小
        headFont.setBold(true);//设置字体是否加粗
        XSSFFont bodyFont = workBook.createFont();
        bodyFont.setFontName("宋体");//设置字体
        bodyFont.setFontHeightInPoints((short) 10);//设置字体大小
        headStyle.setFont(headFont);
        bodyStyle.setFont(bodyFont);

        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;

        try {
            //设置标题行，每一列的标题
            XSSFCell cell = null;
            if (tableHeader != null && tableHeader.size() > 0) {
                for (int i = 0; i < tableHeader.size(); i++) {
                    cell = myRow.createCell((short) i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(tableHeader.get(i));
                }
            }

            // 遍历集合数据，产生数据行
            Iterator<T> it = data.iterator();
            //添加数据
            while (it.hasNext()) {
                T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                //创建行
                XSSFRow  Row = mySheet.createRow(++rowNomber);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try
                    {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        // 判断值的类型后进行强制类型转换
                        String textValue = value == null ? "" : value.toString();
                        XSSFCell hssfCell = Row.createCell((short) i);
                        hssfCell.setCellStyle(bodyStyle);
                        hssfCell.setCellValue(textValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            File newPath = new File(filePath);
            newPath.mkdirs();
            if (!newPath.exists()) {
                newPath.mkdirs();
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
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
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     *  数据导出到excel中，并通过浏览器下载
     *  备注：该方法可以生成 xlsx 文件，除了所有单元格框线和固定表头外不加其他自定义样式，加了自定义样式文件会比较大
     *
     * @param response
     * @param sheetName 导出的excel的sheet页的名称
     * @param filePath  文件的存储路径(临时存储路径，浏览器下载完文件之后会删除该文件)
     * @param fileName  导出的exce的文件名称
     * @param tableHeader   导出的表格的头部，每一列的名称
     * @param data  所有的数据集
     * Collection<T>    这里使用Collection<T>提高方法的可重用性
     */
    public void dataExportToExcelXlsxNoStyle(HttpServletResponse response, String sheetName, String filePath, String fileName, List<String> tableHeader, Collection<T> data) {
        //生成excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作簿
        XSSFSheet mySheet = workBook.createSheet();
        mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
        //设置工作簿的名字
        workBook.setSheetName(0, sheetName);
        //创建第一行，标题行
        int rowNomber = -1;
        XSSFRow myRow = mySheet.createRow(++rowNomber);
        XSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
        headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        XSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
        bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        //设置字体样式
        XSSFFont headFont = workBook.createFont();
        headFont.setFontName("宋体");//设置字体
        headFont.setFontHeightInPoints((short) 10);//设置字体大小
        headFont.setBold(true);//设置字体是否加粗（表头字体加粗）
        headStyle.setFont(headFont);

        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;

        try {
            //设置标题行，每一列的标题
            XSSFCell cell = null;
            if (tableHeader != null && tableHeader.size() > 0) {
                for (int i = 0; i < tableHeader.size(); i++) {
                    cell = myRow.createCell((short) i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(tableHeader.get(i));
                }
            }

            // 遍历集合数据，产生数据行
            Iterator<T> it = data.iterator();
            //添加数据
            while (it.hasNext()) {
                T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                //创建行
                XSSFRow  Row = mySheet.createRow(++rowNomber);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        // 判断值的类型后进行强制类型转换
                        String textValue = value == null ? "" : value.toString();
                        XSSFCell hssfCell = Row.createCell((short) i);
                        hssfCell.setCellStyle(bodyStyle);
                        hssfCell.setCellValue(textValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            File newPath = new File(filePath);
            if (!newPath.exists()) {//如果存储的临时文件夹不存在
                newPath.mkdirs();//创建文件夹
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
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
            log.info("数据成功写入excel！");
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     *  数据导出到excel中，不使用浏览器下载，保存至指定的本地文件夹中
     *  备注：该方法可以生成 xlsx 文件，除了所有单元格框线和固定表头外不加其他自定义样式，加了自定义样式文件会比较大
     *
     * @param sheetName 导出的excel的sheet页的名称
     * @param filePath  文件的存储路径(临时存储路径，浏览器下载完文件之后会删除该文件)
     * @param fileName  导出的exce的文件名称
     * @param tableHeader   导出的表格的头部，每一列的名称
     * @param data  所有的数据集
     * Collection<T>    这里使用Collection<T>提高方法的可重用性
     */
    public void dataExportToExcelXlsxAndSaveToLocal(String sheetName, String filePath, String fileName, List<String> tableHeader, List<String> fields, Collection<T> data) {
        //生成excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作簿
        XSSFSheet mySheet = workBook.createSheet();
        mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
        //设置工作簿的名字
        workBook.setSheetName(0, sheetName);
        //创建第一行，标题行
        int rowNomber = -1;
        XSSFRow myRow = mySheet.createRow(++rowNomber);
        XSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
        headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        XSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
        bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        //设置字体样式
        XSSFFont headFont = workBook.createFont();
        headFont.setFontName("宋体");//设置字体
        headFont.setFontHeightInPoints((short) 10);//设置字体大小
        headFont.setBold(true);//设置字体是否加粗（表头字体加粗）
        headStyle.setFont(headFont);

        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;

        try {
            //设置标题行，每一列的标题
            XSSFCell cell = null;
            if (tableHeader != null && tableHeader.size() > 0) {
                for (int i = 0; i < tableHeader.size(); i++) {
                    cell = myRow.createCell((short) i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(tableHeader.get(i));
                }
            }

            // 遍历集合数据，产生数据行
            Iterator<T> it = data.iterator();
            //添加数据
            while (it.hasNext()) {
                T t = (T) it.next();
                //创建行
                XSSFRow  Row = mySheet.createRow(++rowNomber);
                for (int i = 0; i < fields.size(); i++) {
                    String getMethodName = "get" + fields.get(i).substring(0, 1).toUpperCase() + fields.get(i).substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        // 判断值的类型后进行强制类型转换
                        String textValue = value == null ? "" : value.toString();
                        XSSFCell hssfCell = Row.createCell((short) i);
                        hssfCell.setCellStyle(bodyStyle);
                        hssfCell.setCellValue(textValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            File newPath = new File(filePath);
            if (!newPath.exists()) {//如果存储的临时文件夹不存在
                newPath.mkdirs();//创建文件夹
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
            log.info("数据成功写入excel！");
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 导出数据到excel，存在多个sheet页的情况
     * @param sheetNameMap  所有sheet页的名称
     * @param filePath  文件存储在本地的路径
     * @param fileName  文件名称
     * @param tableHeaderMap    所有sheet页的表头
     * @param fieldMap  所有sheet页表头对应的字段名称
     * @param dataMap   所有sheet页包含的数据集合
     */
    public void multipleSheetExcelToLocal(Map<String,String> sheetNameMap,String filePath,String fileName,Map<String,List<String>> tableHeaderMap,Map<String,List<String>> fieldMap,Map<String,Collection<T>> dataMap) {
        //生成excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        int sheetIndex = 0;
        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;
        try {
            for (String key : sheetNameMap.keySet()) {
                //创建工作簿
                XSSFSheet mySheet = workBook.createSheet();
                mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
                //设置工作簿的名字
                workBook.setSheetName(sheetIndex, sheetNameMap.get(key));
                sheetIndex++;
                //创建第一行，标题行
                int rowNomber = -1;
                XSSFRow myRow = mySheet.createRow(++rowNomber);
                XSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
                headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
                headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
                headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
                headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
                XSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
                bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
                bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
                bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
                bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
                //设置字体样式
                XSSFFont headFont = workBook.createFont();
                headFont.setFontName("宋体");//设置字体
                headFont.setFontHeightInPoints((short) 10);//设置字体大小
                headFont.setBold(true);//设置字体是否加粗（表头字体加粗）
                headStyle.setFont(headFont);

                List<String> tableHeader = tableHeaderMap.get(key);
                Collection<T> data = dataMap.get(key);
                List<String> fields = fieldMap.get(key);

                //设置标题行，每一列的标题
                XSSFCell cell = null;
                if (tableHeader != null && tableHeader.size() > 0) {
                    for (int i = 0; i < tableHeader.size(); i++) {
                        cell = myRow.createCell((short) i);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(tableHeader.get(i));
                    }
                }

                // 遍历集合数据，产生数据行
                Iterator<T> it = data.iterator();
                //添加数据
                while (it.hasNext()) {
                    T t = (T) it.next();
                    //创建行
                    XSSFRow  Row = mySheet.createRow(++rowNomber);
                    for (int i = 0; i < fields.size(); i++) {
                        String getMethodName = "get" + fields.get(i).substring(0, 1).toUpperCase() + fields.get(i).substring(1);
                        try {
                            Class tCls = t.getClass();
                            Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                            Object value = getMethod.invoke(t, new Object[] {});
                            // 判断值的类型后进行强制类型转换
                            String textValue = value == null ? "" : value.toString();
                            XSSFCell hssfCell = Row.createCell((short) i);
                            hssfCell.setCellStyle(bodyStyle);
                            hssfCell.setCellValue(textValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            File newPath = new File(filePath);
            if (!newPath.exists()) {//如果存储的临时文件夹不存在
                newPath.mkdirs();//创建文件夹
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
            log.info("数据成功写入excel！");
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 判断某个文件夹中是否存在某个文件
     * @param fileName  文件名称
     * @param directoryPath 文件夹路径
     * @return
     */
    public boolean fileIsExistsInDirectory(String fileName,String directoryPath) {
        boolean result = false;
        File directory = new File(directoryPath);
        if (directory != null) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (fileName.equals(files[i].getName())) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void test(String sheetName, String filePath, String fileName, List<String> tableHeader, List<String> fields, Collection<T> data) {
        //生成excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作簿
        XSSFSheet mySheet = workBook.createSheet();
        mySheet.createFreezePane(1,1,1,1);//冻结单元格.第一个参数表示要冻结的列数；第二个参数表示要冻结的行数; 第三个参数表示右边区域可见的首列序号，从1开始计算；第四个参数表示下边区域可见的首行序号，也是从1开始计算；四个参数都是1表示冻结首行首列
        //设置工作簿的名字
        workBook.setSheetName(0, sheetName);
        //创建第一行，标题行
        int rowNomber = -1;
        XSSFRow myRow = mySheet.createRow(++rowNomber);
        XSSFCellStyle headStyle = workBook.createCellStyle();//表头样式
        headStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        headStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        headStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        headStyle.setAlignment(HorizontalAlignment.CENTER);//垂直居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//水平居中
        XSSFCellStyle bodyStyle = workBook.createCellStyle();//表格内容部分样式
        bodyStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        bodyStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);//垂直居中
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);//水平居中
        XSSFCellStyle rangeStyle = workBook.createCellStyle();//合并单元格后的样式
        rangeStyle.setBorderLeft(BorderStyle.THIN);//设置表格的左边框
        rangeStyle.setBorderRight(BorderStyle.THIN);//设置表格的左边框
        rangeStyle.setBorderTop(BorderStyle.THIN);//设置表格的上边框
        rangeStyle.setBorderBottom(BorderStyle.THIN);//设置表格的下边框
        rangeStyle.setAlignment(HorizontalAlignment.CENTER);//垂直居中
        rangeStyle.setVerticalAlignment(VerticalAlignment.CENTER);//水平居中
        //设置字体样式
        XSSFFont headFont = workBook.createFont();
        headFont.setFontName("宋体");//设置字体
        headFont.setFontHeightInPoints((short) 10);//设置字体大小
        headFont.setBold(true);//设置字体是否加粗（表头字体加粗）
        headStyle.setFont(headFont);

        FileOutputStream fos = null;
        FileInputStream in = null;
        OutputStream out = null;

        try {
            //设置标题行，每一列的标题
            XSSFCell cell = null;
            if (tableHeader != null && tableHeader.size() > 0) {
                for (int i = 0; i < tableHeader.size(); i++) {
                    cell = myRow.createCell((short) i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(tableHeader.get(i));
                }
            }

            // 遍历集合数据，产生数据行
            Iterator<T> it = data.iterator();
            int index = 1;
            List<Object> oldd = new ArrayList<>();
            List<Object> olds = new ArrayList<>();
            Map<Integer,Integer> hbhsMap = new HashMap<>();
            for (int i = 0; i < fields.size(); i++) {
                hbhsMap.put(i,0);
            }
            //添加数据
            while (it.hasNext()) {
                T t = (T) it.next();
                //创建行
                XSSFRow  Row = mySheet.createRow(++rowNomber);

                for (int i = 0; i < fields.size(); i++) {
                    String getMethodName = "get" + fields.get(i).substring(0, 1).toUpperCase() + fields.get(i).substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});

                        if (index > 1) {
                            if (index % 2 == 0) {
                                if (value == oldd.get(i) && index != data.size()) {
                                    hbhsMap.put(i,hbhsMap.get(i) + 1);
                                } else if (value != oldd.get(i) && hbhsMap.get(i) > 0) {
                                    CellRangeAddress cra = new CellRangeAddress(index - hbhsMap.get(i) - 1,index - 1,i,i);
                                    mySheet.addMergedRegion(cra);
                                    XSSFCell rangeCell = mySheet.getRow(index - hbhsMap.get(i) - 1).getCell(i);//获取合并后的单元格对象
                                    rangeCell.setCellStyle(rangeStyle);//合并后的单元格设置样式
                                    hbhsMap.put(i,0);
                                } else if (value == oldd.get(i) && index == data.size()) {
                                    CellRangeAddress cra = new CellRangeAddress(index - hbhsMap.get(i) - 1,index,i,i);
                                    mySheet.addMergedRegion(cra);
                                    XSSFCell rangeCell = mySheet.getRow(index - hbhsMap.get(i) - 1).getCell(i);//获取合并后的单元格对象
                                    rangeCell.setCellStyle(rangeStyle);//合并后的单元格设置样式
                                    hbhsMap.put(i,0);
                                }
                            } else {
                                if (value == olds.get(i) && index != data.size()) {
                                    hbhsMap.put(i,hbhsMap.get(i) + 1);
                                } else if (value != olds.get(i) && hbhsMap.get(i) > 0) {
                                    CellRangeAddress cra = new CellRangeAddress(index - hbhsMap.get(i) - 1,index - 1,i,i);
                                    mySheet.addMergedRegion(cra);
                                    XSSFCell rangeCell = mySheet.getRow(index - hbhsMap.get(i) - 1).getCell(i);//获取合并后的单元格对象
                                    rangeCell.setCellStyle(rangeStyle);//合并后的单元格设置样式
                                    hbhsMap.put(i,0);
                                } else if (value == olds.get(i) && index == data.size()) {
                                    CellRangeAddress cra = new CellRangeAddress(index - hbhsMap.get(i) - 1,index,i,i);
                                    mySheet.addMergedRegion(cra);
                                    XSSFCell rangeCell = mySheet.getRow(index - hbhsMap.get(i) - 1).getCell(i);//获取合并后的单元格对象
                                    rangeCell.setCellStyle(rangeStyle);//合并后的单元格设置样式
                                    hbhsMap.put(i,0);
                                }
                            }
                        }

                        if (index % 2 == 0) {
                            olds.add(value);
                        } else {
                            oldd.add(value);
                        }


                        // 判断值的类型后进行强制类型转换
                        String textValue = value == null ? "" : value.toString();
                        XSSFCell hssfCell = Row.createCell((short) i);
                        hssfCell.setCellStyle(bodyStyle);
                        hssfCell.setCellValue(textValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (index % 2 == 0) {
                    oldd.clear();
                } else {
                    olds.clear();
                }
                index++;
            }
            File newPath = new File(filePath);
            if (!newPath.exists()) {//如果存储的临时文件夹不存在
                newPath.mkdirs();//创建文件夹
            }
            File file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            workBook.write(fos);// 写文件
            log.info("数据成功写入excel！");
        } catch (Exception e) {
            log.info("数据写入失败，请重试！");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

}
