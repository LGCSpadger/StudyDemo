package com.test.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class NewExcelUtil {
    private static TimeZone tz = TimeZone.getTimeZone("GMT+8");

    /**
     * 导出复合数据到Excel (支持把map中各元素一个sheet)
     *
     * @param title
     *            各sheet中共同的标题
     * @param colName
     *            数据list中对应的列名，顺序与title对应。
     * @param mutilSheetDataMap
     *            mutilSheetDataMap中一个map元素对应一个sheet，并sheet名称为map中key
     * @param os
     *            输出流
     */
    public static void exportToExcel(String[] title, String[] colName,
                                     Map<String, List<Map>> mutilSheetDataMap, OutputStream os)
    {
        if (title == null || title.length == 0)
        {
            throw new NullPointerException("没有定义标题集合，或者没有提供get方法");
        }
        if (colName == null || colName.length == 0)
        {
            throw new NullPointerException("没有定义字段集合，或者没有提供get方法");
        }
        if (mutilSheetDataMap == null)
        {
            throw new NullPointerException("没有定义导出数据结果集合，或者没有提供get方法");
        }
        HSSFWorkbook wwb = null;
        try
        {
            // 创建工作表
            wwb = new HSSFWorkbook();
            HSSFSheet ws = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            String sheetName;
            List<Map> sheetDataList = null;
            Iterator<String> it = mutilSheetDataMap.keySet().iterator();
            while (it.hasNext())
            {
                // 按照sheet数据map的key来创建sheet
                sheetName = it.next();
                ws = wwb.createSheet(replaceStr(sheetName));
                // 获取sheet数据list
                sheetDataList = mutilSheetDataMap.get(sheetName);
                /**
                 * 写标题
                 */
                row = ws.createRow(0);
                for (int m = 0; m < title.length; m++)
                {
                    cell = row.createCell((short) m);
                    cell.setCellValue(new HSSFRichTextString(title[m]));
                }
                /**
                 * 写数据
                 */
                int rownum = 1;
                for (Map dataMap : sheetDataList)
                {
                    row = ws.createRow(rownum);
                    for (int j = 0; j < colName.length; j++)
                    {
                        cell = row.createCell((short) j);
                        // 按字段取值
                        String columnName = colName[j];
                        cell.setCellValue(new HSSFRichTextString(String.valueOf(dataMap
                                .get(columnName))));
                    }
                    rownum++;
                }
            }
            // 写入流
            wwb.write(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换特殊字符串
     *
     * @param str
     * @return
     */
    private static String replaceStr(String str)
    {
        String msg = str.replaceAll(":", "").replaceAll("/", "").replaceAll("\\?", "")
                .replaceAll("\\*", "").replaceAll("\\[", "{").replaceAll("\\]", "}");
        if (msg.length() > 31)
        {
            msg = msg.substring(0, 30);
        }
        return msg;
    }

    /**
     * 根据传入的数组生成excel文件流，每10000行分一页 （该方法暂时不使用，保留）
     *
     * @param title
     *            表格显示的标题，默认作为每一页的第一行
     * @param dataList
     *            结果集数组
     * @param os
     *            输出流
     */
    public static void exportToExcel(String[] title, String[][] dataList, OutputStream os)
    {
        // 然后将结果集转化为Excel输出
        // 初始化工作
        WritableWorkbook wwb = null;
        Label labelC = null;
        int k = -1;
        try
        {
            // 创建工作表
            wwb = Workbook.createWorkbook(os);
            WritableSheet ws = null;
            // 逐行添加数据
            for (int i = 0; i < dataList.length; i++)
            {
                // 每10000条记录分一页
                if (i / 10000 > k)
                {
                    k = i / 10000;
                    ws = wwb.createSheet("Sheet" + k, k);
                    // 在每页的第一行输入标题
                    for (int l = 0; l < title.length; l++)
                    {
                        labelC = new Label(l, 0, title[l]);
                        ws.addCell(labelC);
                    }
                }
                // 输出数据
                for (int j = 0; j < dataList[i].length; j++)
                {
                    labelC = new Label(j, i - 10000 * k + 1, dataList[i][j]);
                    ws.addCell(labelC);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.write();
                    wwb.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输入2007版以上excel文件，解析后返回ArrayList
     *
     * @param file
     * @return
     */
    private static ArrayList<Map> importToExcel2007(File file)
    {
        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        // 初始化
        FileInputStream readFile = null;
        XSSFWorkbook wb = null;
        XSSFRow row = null;
        XSSFSheet st = null;
        XSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new XSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            for (int k = 0; k < numOfSheets; k++)
            {
                // 获取当前的
                st = wb.getSheetAt(k);
                // 当前页的行数
                int rows = st.getLastRowNum();
                // 如果行数大于0，则先取第一行为字段名
                if (rows > 0)
                {
                    row = st.getRow(0);
                    int cells = row.getLastCellNum();
                    title = new String[cells];
                    for (int j = 0; j < cells; j++)
                    {
                        // 列为空则输入空字符串
                        if (row.getCell(j) == null)
                        {
                            title[j] = "";
                            continue;
                        }
                        cell = row.getCell(j);
                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                            {
                                Integer num = new Integer((int) cell
                                        .getNumericCellValue());
                                title[j] = String.valueOf(num);
                                break;
                            }
                            case STRING:
                            {
                                title[j] = cell.getRichStringCellValue().toString();
                                break;
                            }
                            default:
                                title[j] = "";
                        }
                    }
                    // 分行解析
                    for (int i = 1; i <= rows; i++)
                    {
                        // 行为空则执行下一行
                        if (st.getRow(i) == null)
                        {
                            continue;
                        }
                        // 将每行数据放入map中
                        row = st.getRow(i);
                        arr.add(getCellMap(row, cells, title));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    private static ArrayList<ArrayList> importToExcel2007(File file, int startIndex)
    {
        ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
        // 初始化
        FileInputStream readFile = null;
        XSSFWorkbook wb = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new XSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            for (int k = 0; k < numOfSheets; k++)
            {
                // 获取当前的
                XSSFSheet st = wb.getSheetAt(k);
                // 当前页的行数
                int rows = st.getLastRowNum();
                // 分行解析
                for (int i = startIndex; i <= rows; i++)
                {
                    // 行为空则执行下一行
                    if (st.getRow(i) == null)
                    {
                        continue;
                    }
                    row = st.getRow(i);
                    int cells = row.getLastCellNum();
                    ArrayList<String> data = new ArrayList<String>();
                    // 分列
                    for (int j = 0; j < cells; j++)
                    {
                        // 列为空则输入空字符串
                        if (row.getCell((short) j) == null)
                        {
                            data.add("");
                            continue;
                        }
                        cell = row.getCell((short) j);
                        // 对字段分类处理
                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                            {
                                Integer num = new Integer((int) cell
                                        .getNumericCellValue());
                                data.add(String.valueOf(num));
                                break;
                            }
                            case STRING:
                            {
                                data.add(cell.getRichStringCellValue().toString());
                                break;
                            }
                            default:
                                data.add("");
                        }
                    }
                    arr.add(data);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    private static ArrayList<Map> importToExcel2007(File file, int sheetNum,
                                                    int startIndex)
    {
        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        // 初始化
        FileInputStream readFile = null;
        XSSFWorkbook wb = null;
        XSSFRow row = null;
        XSSFSheet st = null;
        XSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new XSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            st = wb.getSheetAt(sheetNum);
            // 当前页的行数
            int rows = st.getLastRowNum();
            // 如果行数大于0，则先取第一行为字段名
            if (rows > 0)
            {
                row = st.getRow(0);
                int cells = row.getLastCellNum();
                title = new String[cells];

                for (int j = 0; j < cells; j++)
                {
                    // 列为空则输入空字符串
                    if (row.getCell(j) == null)
                    {
                        title[j] = "";
                        continue;
                    }
                    cell = row.getCell(j);
                    switch (cell.getCellType())
                    {
                        case NUMERIC:
                        {
                            Integer num = new Integer((int) cell.getNumericCellValue());
                            title[j] = String.valueOf(num);
                            break;
                        }
                        case STRING:
                        {
                            title[j] = cell.getRichStringCellValue().toString();
                            break;
                        }
                        default:
                            title[j] = "";
                    }
                }
                if (rows > (startIndex + 1))
                {
                    // 分行解析
                    for (int i = startIndex + 1; i <= rows; i++)
                    {
                        // 行为空则执行下一行
                        if (st.getRow(i) == null)
                        {
                            continue;
                        }
                        // 将每行数据放入map中
                        row = st.getRow(i);
                        arr.add(getCellMap(row, cells, title));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * @param file
     * @return
     */
    private static ArrayList<Map> importToExcel2007_col(File file)
    {
        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        String[] tmp;
        // 初始化
        FileInputStream readFile = null;
        XSSFWorkbook wb = null;
        XSSFRow row = null;
        XSSFSheet st = null;
        XSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new XSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            for (int k = 0; k < numOfSheets; k++)
            {
                // 获取当前的
                st = wb.getSheetAt(k);
                // 当前页的行数
                int rows = st.getLastRowNum();
                // 如果行数大于0，则先取第一行为字段名            改成如果大于1
                if (rows > 1)
                {
                    row = st.getRow(1);            //改了
                    int cells = row.getLastCellNum();
                    title = new String[cells];
                    tmp = new String[cells];
                    int count = 0;
                    for (int j = 0; j < cells; j++)
                    {
                        // 列为空则输入空字符串
                        if (row.getCell(j) == null)
                        {
                            title[j] = "";
                            continue;
                        }
                        cell = row.getCell(j);
                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                            {
                                Integer num = new Integer((int) cell
                                        .getNumericCellValue());
                                title[j] = String.valueOf(num);
                                break;
                            }
                            case STRING:
                            {
                                title[j] = cell.getRichStringCellValue().toString();
                                break;
                            }
                            default:
                                title[j] = "";
                        }
                        tmp[j] = title[j];
                        count = checkExit(tmp, tmp[j]);
                        if (count <= 1)
                        {
                            title[j] = tmp[j];
                        }
                        else
                        {
                            title[j] = tmp[j] + "_" + count;
                        }
                    }
                    // 分行解析
                    for (int i = 2; i <= rows; i++)                 //改
                    {
                        // 行为空则执行下一行
                        if (st.getRow(i) == null)
                        {
                            continue;
                        }
                        // 将每行数据放入map中
                        row = st.getRow(i);
                        arr.add(getCellMap(row, cells, title));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * 根据文件扩展名判断是否Excel2007以上
     *
     * @param file
     * @return
     */
    private static boolean checkExcel2007(File file)
    {
        String extendName = file.getName().substring(file.getName().lastIndexOf("."));
        if (".xlsx".equals(extendName.trim().toLowerCase()))
        {
            return true;
        }
        return false;
    }

    /**
     * 输入excel文件，解析后返回ArrayList
     *
     * @param file
     *            输入的excel文件
     * @return ArrayList<Map>，其中的map以第一行的内容为键值
     */
    public static ArrayList<Map> importToExcel(File file)
    {
        if (checkExcel2007(file))
        {
            return importToExcel2007(file);
        }

        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        Workbook wwb = null;
        try
        {
            // 读取excel文件
            wwb = Workbook.getWorkbook(file);
            // 总sheet数
            int sheetNumber = wwb.getNumberOfSheets();
            System.out.println("sheetNumber:" + sheetNumber);
            for (int m = 0; m < sheetNumber; m++)
            {
                Sheet ws = wwb.getSheet(m);
                // 当前页总记录行数和列数
                int rowCount = ws.getRows();
                int columeCount = ws.getColumns();
                System.out.println("rowCount:" + rowCount);
                System.out.println("columeCount:" + columeCount);
                // 第一行为字段名，所以行数大于1才执行
                if (rowCount > 1 && columeCount > 0)
                {
                    // 取第一列为字段名
                    title = new String[columeCount];
                    for (int k = 0; k < columeCount; k++)
                    {
                        title[k] = ws.getCell(k, 0).getContents().trim();
                    }
                    // 取当前页所有值放入list中
                    for (int i = 1; i < rowCount; i++)
                    {
                        Map dataMap = new HashMap();
                        for (int j = 0; j < columeCount; j++)
                        {
                            if (ws.getCell(j, i).getType() == CellType.DATE)
                            {
                                Date date = ((DateCell) ws.getCell(j, i)).getDate();
                                long offset = tz.getOffset(date.getTime());
                                date.setTime(date.getTime() - offset);
                                dataMap.put(title[j], date);
                            }
                            else
                            {
                                dataMap.put(title[j], ws.getCell(j, i).getContents());
                            }
                        }
                        arr.add(dataMap);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (wwb != null)
            {
                wwb.close();
                wwb = null;
            }
        }
        return arr;
    }

    /**
     * 输入excel文件，解析后返回ArrayList
     *
     * @param file
     *            输入的excel文件
     * @param sheetNum
     *            数据所在的sheet页，从0开始计数
     * @param startIndex
     *            起始行，从0开始计数
     * @return ArrayList<Map>，其中的map以第一行的内容为键值
     */
    public static ArrayList<Map> importToExcel(File file, int sheetNum, int startIndex)
    {
        if (checkExcel2007(file))
        {
            return importToExcel2007(file, sheetNum, startIndex);
        }
        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        Workbook wwb = null;
        try
        {
            // 读取excel文件
            wwb = Workbook.getWorkbook(file);
            // 总sheet数
            int sheetNumber = wwb.getNumberOfSheets();
            System.out.println("sheetNumber:" + sheetNumber);
            // 如果指定sheet过大，直接返回空
            if (sheetNum >= sheetNumber)
            {
                return new ArrayList<Map>();
            }
            Sheet ws = wwb.getSheet(sheetNum);
            // 当前页总记录行数和列数
            int rowCount = ws.getRows();
            int columeCount = ws.getColumns();
            System.out.println("rowCount:" + rowCount);
            System.out.println("columeCount:" + columeCount);
            // 第一行为字段名，所以行数大于1才执行
            if (rowCount > (startIndex + 1) && columeCount > 0)
            {
                // 取第一列为字段名
                title = new String[columeCount];
                for (int k = 0; k < columeCount; k++)
                {
                    title[k] = ws.getCell(k, startIndex).getContents().trim();
                }
                // 取当前页所有值放入list中
                for (int i = (startIndex + 1); i < rowCount; i++)
                {
                    Map dataMap = new HashMap();
                    for (int j = 0; j < columeCount; j++)
                    {
                        if (ws.getCell(j, i).getType() == CellType.DATE)
                        {
                            Date date = ((DateCell) ws.getCell(j, i)).getDate();
                            long offset = tz.getOffset(date.getTime());
                            date.setTime(date.getTime() - offset);
                            dataMap.put(title[j], date);
                        }
                        else
                        {
                            dataMap.put(title[j], ws.getCell(j, i).getContents());
                        }
                    }
                    arr.add(dataMap);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (wwb != null)
            {
                wwb.close();
                wwb = null;
            }
        }
        return arr;
    }

    /**
     * 输入excel文件，解析后返回ArrayList
     *
     * @param file
     *            输入的excel文件
     * @return ArrayList<Map>，其中的map以第一行的内容为键值，若有重复则添加数字标记
     */
    public static ArrayList<Map> importToExcel_col(File file)
    {
        if (checkExcel2007(file))
        {
            return importToExcel2007_col(file);
        }
        // 初始化返回值和字段名数组
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        String[] tmp;
        Workbook wwb = null;
        try
        {
            // 读取excel文件
            wwb = Workbook.getWorkbook(file);
            // 总sheet数
            int sheetNumber = wwb.getNumberOfSheets();
            for (int m = 0; m < sheetNumber; m++)
            {
                Sheet ws = wwb.getSheet(m);
                // 当前页总记录行数和列数
                int rowCount = ws.getRows();
                int columeCount = ws.getColumns();
                // 第一行为字段名，所以行数大于1才执行               改成了第2行开始
                if (rowCount > 2 && columeCount > 0)
                {
                    // 取第一列为字段名，若有重复则增加数字做标记
                    title = new String[columeCount];
                    tmp = new String[columeCount];
                    int count = 0;
                    for (int k = 0; k < columeCount; k++)
                    {
                        tmp[k] = ws.getCell(k, 1).getContents();
                        count = checkExit(tmp, tmp[k]);
                        if (count <= 1)
                        {
                            title[k] = ws.getCell(k, 1).getContents().trim();
                        }
                        else
                        {
                            title[k] = ws.getCell(k, 1).getContents().trim() + "_"
                                    + count;
                        }
                    }
                    // 取当前页所有值放入list中
                    for (int i = 2; i < rowCount; i++)
                    {
                        Map dataMap = new HashMap();
                        for (int j = 0; j < columeCount; j++)
                        {
                            if (ws.getCell(j, i).getType() == CellType.DATE)
                            {
                                Date date = ((DateCell) ws.getCell(j, i)).getDate();
                                long offset = tz.getOffset(date.getTime());
                                date.setTime(date.getTime() - offset);
                                dataMap.put(title[j], date);
                            }
                            else
                            {
                                dataMap.put(title[j], ws.getCell(j, i).getContents());
                            }
                        }
                        arr.add(dataMap);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (wwb != null)
            {
                wwb.close();
                wwb = null;
            }
        }
        return arr;
    }

    /**
     * 根据传入的数组生成excel文件流，每10000行分一页 （使用poi方法）
     *
     * @param title
     *            表格显示的标题，默认作为每一页的第一行
     * @param dataList
     *            结果集数组
     * @param os
     *            输出流
     */
    public static void exportToExcel_poi(String[] title, String[][] dataList,
                                         OutputStream os)
    {
        // 将结果集转化为Excel输出
        int k = -1;
        HSSFWorkbook wwb = null;
        try
        {
            // 创建工作表
            wwb = new HSSFWorkbook();
            HSSFSheet ws = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            // 逐行添加数据
            for (int i = 0; i < dataList.length; i++)
            {
                // 每10000条记录分一页
                if (i / 10000 > k)
                {
                    k = i / 10000;
                    ws = wwb.createSheet("Sheet" + k);
                    row = ws.createRow(0);
                    // 在每页的第一行输入标题
                    for (int l = 0; l < title.length; l++)
                    {
                        cell = row.createCell((short) l);
                        cell.setCellValue(new HSSFRichTextString(title[l]));
                    }
                }
                // 输出数据
                for (int j = 0; j < dataList[i].length; j++)
                {
                    row = ws.createRow(i - 10000 * k + 1);
                    cell = row.createCell((short) j);
                    cell.setCellValue(new HSSFRichTextString(dataList[i][j]));
                }
            }
            wwb.write(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                os.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传入excel文件，解析后返回ArrayList
     *
     * @param file
     * @param startIndex
     *            从第几行开始解析
     */
    public static ArrayList<ArrayList> importExcel_poi(File file, int startIndex)
    {
        if (checkExcel2007(file))
        {
            return importToExcel2007(file, startIndex);
        }
        ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
        // 初始化
        FileInputStream readFile = null;
        HSSFWorkbook wb = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new HSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            for (int k = 0; k < numOfSheets; k++)
            {
                // 获取当前的
                HSSFSheet st = wb.getSheetAt(k);
                // 当前页的行数
                int rows = st.getLastRowNum();
                // 分行解析
                for (int i = startIndex; i <= rows; i++)
                {
                    // 行为空则执行下一行
                    if (st.getRow(i) == null)
                    {
                        continue;
                    }
                    row = st.getRow(i);
                    int cells = row.getLastCellNum();
                    ArrayList<String> data = new ArrayList<String>();
                    // 分列
                    for (int j = 0; j < cells; j++)
                    {
                        // 列为空则输入空字符串
                        if (row.getCell((short) j) == null)
                        {
                            data.add("");
                            continue;
                        }
                        cell = row.getCell((short) j);
                        // 对字段分类处理
                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                            {
                                Integer num = new Integer((int) cell
                                        .getNumericCellValue());
                                data.add(String.valueOf(num));
                                break;
                            }
                            case STRING:
                            {
                                data.add(cell.getRichStringCellValue().toString());
                                break;
                            }
                            default:
                                data.add("");
                        }
                    }
                    arr.add(data);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * 传入excel文件，解析后返回ArrayList。文件的第一行表示字段的名称
     *
     * @param file
     */
    public static ArrayList<Map> importExcel_poi(File file)
    {
        if (checkExcel2007(file))
        {
            return importToExcel2007(file);
        }
        ArrayList<Map> arr = new ArrayList<Map>();
        String[] title;
        // 初始化
        FileInputStream readFile = null;
        HSSFWorkbook wb = null;
        HSSFRow row = null;
        HSSFSheet st = null;
        HSSFCell cell = null;
        try
        {
            // 读取文件
            readFile = new FileInputStream(file);
            wb = new HSSFWorkbook(readFile);
            // 文档的页数
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets:" + numOfSheets);
            for (int k = 0; k < numOfSheets; k++)
            {
                // 获取当前的
                st = wb.getSheetAt(k);
                // 当前页的行数
                int rows = st.getLastRowNum();
                // 如果行数大于0，则先取第一行为字段名
                if (rows > 0)
                {
                    row = st.getRow(0);
                    int cells = row.getLastCellNum();
                    title = new String[cells];
                    for (int j = 0; j < cells; j++)
                    {
                        // 列为空则输入空字符串
                        if (row.getCell((short) j) == null)
                        {
                            title[j] = "";
                            continue;
                        }
                        cell = row.getCell((short) j);
                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                            {
                                Integer num = new Integer((int) cell
                                        .getNumericCellValue());
                                title[j] = String.valueOf(num);
                                break;
                            }
                            case STRING:
                            {
                                title[j] = cell.getRichStringCellValue().toString();
                                break;
                            }
                            default:
                                title[j] = "";
                        }
                    }
                    // 分行解析
                    for (int i = 1; i <= rows; i++)
                    {
                        // 行为空则执行下一行
                        if (st.getRow(i) == null)
                        {
                            continue;
                        }
                        // 将每行数据放入map中
                        row = st.getRow(i);
                        arr.add(getCellMap(row, cells, title));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                readFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * 将数据写入EXCEL文件的指定位置
     *
     * @param file
     *            EXCEL文件
     * @param data
     *            需要写入的数据，二维表格的数据
     * @param sheetNum
     *            sheet序号，从0开始计数
     * @param startRow
     *            其实行，从0开始计数
     * @param startCol
     *            其实列，从0开始计数
     * @return 修改完成的EXCEL
     */
    public static File insertExcelContent(File file, List<List<String>> data,
                                          int sheetNum, int startRow, int startCol)
    {
        if (null == data || data.size() == 0)
        {
            System.err.println("[insertExcelContent]data is null");
            return file;
        }
        System.out.println("[insertExcelContent]data:" + data);
        WritableWorkbook wwb = null;
        Label labelC = null;
        try
        {
            // 读取excel文件
            Workbook book = Workbook.getWorkbook(file);
            // 打开这个文件的副本，并且指定数据写回到原文件
            wwb = Workbook.createWorkbook(file, book);
            // 总sheet数
            int sheetNumber = wwb.getNumberOfSheets();
            System.out.println("sheetNumber:" + sheetNumber);
            // 如果指定sheet过大，直接原文件
            if (sheetNum >= sheetNumber)
            {
                return file;
            }
            WritableSheet ws = wwb.getSheet(sheetNum);
            // 写文件
            int i = startRow;
            int j = startCol;
            for (List<String> rowData : data)
            {
                for (String cellData : rowData)
                {
                    labelC = new Label(j, i, cellData, ws.getCell(j, i).getCellFormat());
                    System.out.println("startRow:" + i + ",startCol:" + j + ",value:"
                            + cellData);
                    try
                    {
                        ws.addCell(labelC);
                    }
                    catch (NullPointerException e)
                    {
                        labelC = new Label(j, i, cellData);
                        ws.addCell(labelC);
                    }
                    j++;
                }
                i++;
                j = startCol;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.write();
                    wwb.close();
                    wwb = null;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 根据传入的ｅｘｃｅｌ行数据得到数据Ｍａｐ
     *
     * @param row
     * @param cells
     * @param title
     * @return
     */
    private static Map getCellMap(HSSFRow row, int cells, String[] title)
    {
        // 初始化
        HSSFCell cell = null;
        Map data = new HashMap();
        // 分列
        for (int j = 0; j < cells; j++)
        {
            // 列为空则输入空字符串
            if (row.getCell((short) j) == null)
            {
                data.put(title[j], "");
                continue;
            }
            cell = row.getCell((short) j);
            // 对字段分类处理
            switch (cell.getCellType())
            {
                case NUMERIC:
                {
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        data.put(title[j], cell.getDateCellValue());
                    }
                    else
                    {
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setGroupingUsed(false);
                        // Integer num = new Integer((int)
                        // cell.getNumericCellValue());
                        data.put(title[j], nf.format(cell.getNumericCellValue()));
                    }
                    break;
                }
                case STRING:
                {
                    data.put(title[j], cell.getRichStringCellValue().toString());
                    break;
                }
                case BOOLEAN:
                {
                    System.out.println("CELL_TYPE_BOOLEAN:" + cell.getBooleanCellValue());
                    data.put(title[j], cell.getBooleanCellValue());
                    break;
                }
                default:
                    data.put(title[j], "");
            }
        }
        return data;
    }

    /**
     * 根据传入的ｅｘｃｅｌ行数据得到数据Ｍａｐ
     *
     * @param row
     * @param cells
     * @param title
     * @return
     */
    private static Map getCellMap(XSSFRow row, int cells, String[] title)
    {
        // 初始化
        XSSFCell cell = null;
        Map data = new HashMap();
        // 分列
        for (int j = 0; j < cells; j++)
        {
            // 列为空则输入空字符串
            if (row.getCell(j) == null)
            {
                data.put(title[j], "");
                continue;
            }
            cell = row.getCell(j);
            // 对字段分类处理
            switch (cell.getCellType())
            {
                case NUMERIC:
                {
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        data.put(title[j], cell.getDateCellValue());
                    }
                    else
                    {
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setGroupingUsed(false);
                        // Integer num = new Integer((int)
                        // cell.getNumericCellValue());
                        data.put(title[j], nf.format(cell.getNumericCellValue()));
                    }
                    // Integer num = new Integer((int) cell.getNumericCellValue());
                    // data.put(title[j], String.valueOf(num));
                    break;
                }
                case STRING:
                {
                    data.put(title[j], cell.getRichStringCellValue());
                    break;
                }
                case BOOLEAN:
                {
                    data.put(title[j], cell.getBooleanCellValue());
                    break;
                }
                default:
                    data.put(title[j], "");
            }
        }
        return data;
    }

    /**
     * 返回数组中有几个重复纪录
     *
     * @param arr
     * @param value
     * @return
     */
    private static int checkExit(String[] arr, String value)
    {
        int j = 0;
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] != null && value.equals(arr[i]))
            {
                j++;
            }
        }
        return j;
    }

    /**
     * 导出复合数据到Excel (支持把map中各元素一个sheet)
     *
     * @param title
     *            各sheet中各个对应的标题
     * @param colName
     *            数据list中对应的列名，顺序与title对应。
     * @param mutilSheetDataMap
     *            mutilSheetDataMap中一个map元素对应一个sheet，并sheet名称为map中key
     * @param os
     *            输出流
     */
    public static void exportToExcel2(List<String[]> title, List<String[]> colName,
                                      Map<String, List<Map>> mutilSheetDataMap, OutputStream os)
    {
        if (title == null || title.size() == 0)
        {
            throw new NullPointerException("没有定义标题集合，或者没有提供get方法");
        }
        if (colName == null || colName.size() == 0)
        {
            throw new NullPointerException("没有定义字段集合，或者没有提供get方法");
        }
        if (mutilSheetDataMap == null)
        {
            throw new NullPointerException("没有定义导出数据结果集合，或者没有提供get方法");
        }
        HSSFWorkbook wwb = null;
        try
        {
            // 创建工作表
            wwb = new HSSFWorkbook();
            HSSFSheet ws = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            String sheetName;
            List<Map> sheetDataList = null;
            Iterator<String> it = mutilSheetDataMap.keySet().iterator();
            Iterator<String[]> titleIt = title.iterator();
            String[] titleArr = null;
            Iterator<String[]> colNameIt = colName.iterator();
            String[] colNameArr = null;
            while (it.hasNext())
            {
                // 按照sheet数据map的key来创建sheet
                sheetName = it.next();
                ws = wwb.createSheet(replaceStr(sheetName));
                // 获取sheet数据list
                sheetDataList = mutilSheetDataMap.get(sheetName);
                /**
                 * 写标题
                 */
                row = ws.createRow(0);
                titleArr = titleIt.next();
                for (int m = 0; m < titleArr.length; m++)
                {
                    cell = row.createCell((short) m);
                    cell.setCellValue(new HSSFRichTextString(titleArr[m]));
                }
                /**
                 * 写数据
                 */
                int rownum = 1;
                colNameArr = colNameIt.next();
                for (Map dataMap : sheetDataList)
                {
                    row = ws.createRow(rownum);
                    for (int j = 0; j < colNameArr.length; j++)
                    {
                        cell = row.createCell((short) j);
                        // 按字段取值
                        String columnName = colNameArr[j];
                        cell.setCellValue(new HSSFRichTextString(String.valueOf(dataMap
                                .get(columnName))));
                    }
                    rownum++;
                }
            }
            // 写入流
            wwb.write(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
