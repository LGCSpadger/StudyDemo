package com.test.pub.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.test.common.utils.DataExportToExcelUtil;
import com.test.common.utils.DataExportToWordUtil;
import com.test.pub.entity.Article;
import com.test.pub.entity.TtuTz;
import com.test.pub.entity.TtuZxl;
import com.test.pub.entity.WxzwZdyxqk;
import com.test.pub.service.ArticleService;
import com.test.pub.service.WxzwZdyxqkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/testApi", description = "test接口Api")
public class TestController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private WxzwZdyxqkService wxzwZdyxqkService;

    //从application.yml文件中取值
    @Value("${TEST_KEY}")
    private String key;

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test01")
    @ApiOperation("文件上传")
    public void test01() {
        String a = "ddgdhgre";
        System.out.println("a.length()="+a.length());
    }

    @RequestMapping("/test02")
    @CrossOrigin
    public Map test02() {
        System.out.println("key: " + key);
        Map map = new HashMap();
        map.put("aa",1);
        map.put("bb","fdkgjrioht");
        return map;
    }

    /**
     * 数据导出到exce，并通过浏览器下载
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     */
    @RequestMapping("/test04")
    @CrossOrigin
    public void test04(HttpServletResponse response) throws IOException, IllegalAccessException {
        List<Article> data = articleService.queryAll(null);
        List<String> tableHead = new ArrayList<>();
        tableHead.add("文章id");
//        tableHead.add("文章主题");
        tableHead.add("文章内容");
        tableHead.add("创作时间");
        tableHead.add("rdl");
        List<String> fields = Arrays.asList("id","content","occurTime","rdl");
        String filePath = "F:\\test";//临时文件存储路径
        String fileName = "文章信息.xls";
        String sheetName = "文章信息";
        //导出数据到excel中，并通过浏览器下载
        articleService.exportDataToExcel(response,filePath,fileName,sheetName);
        //数据导出到excel中，保存至指定的本地文件夹中
//        new DataExportToExcelUtil<Article>().dataExportToExcelXlsxAndSaveToLocal(sheetName,filePath,fileName,tableHead,fields,data);
    }


    @RequestMapping("/test05")
    public void test05(HttpServletResponse response) throws Exception {
        List<Article> data = articleService.queryAll(null);
        Map<String,String> sheetNameMap = new HashMap<>();
        Map<String,List<String>> tableHeaderMap = new HashMap<>();
        Map<String,List<String>> fieldMap = new HashMap<>();
        Map<String,Collection<Article>> dataMap = new HashMap<>();
        List<String> tableHeadOne = new ArrayList<>();
        tableHeadOne.add("文章id");
        tableHeadOne.add("文章内容");
        tableHeadOne.add("创作时间");
        tableHeadOne.add("rdl");
        tableHeaderMap.put("sheet1",tableHeadOne);
        List<String> tableHeadTwo = new ArrayList<>();
        tableHeadTwo.add("文章id");
        tableHeadTwo.add("文章主题");
        tableHeadTwo.add("文章内容");
        tableHeadTwo.add("创作时间");
        tableHeadTwo.add("rdl");
        tableHeaderMap.put("sheet2",tableHeadTwo);
        List<String> fieldOne = Arrays.asList("id","content","occurTime","rdl");
        List<String> fieldTwo = Arrays.asList("id","title","content","occurTime","rdl");
        fieldMap.put("sheet1",fieldOne);
        fieldMap.put("sheet2",fieldTwo);
        String filePath = "F:\\test";//临时文件存储路径
        String fileName = "文章信息.xlsx";
        String sheetNameOne = "文章信息1";
        String sheetNameTwo = "文章信息2";
        sheetNameMap.put("sheet1",sheetNameOne);
        sheetNameMap.put("sheet2",sheetNameTwo);
        dataMap.put("sheet1",data);
        dataMap.put("sheet2",data);
        //数据导出到excel中，保存至指定的本地文件夹中
        new DataExportToExcelUtil<Article>().multipleSheetExcelToLocal(sheetNameMap,filePath,fileName,tableHeaderMap,fieldMap,dataMap);
        new DataExportToExcelUtil<Article>().dataExportToExcelXlsxAndSaveToLocal(sheetNameOne,filePath,"测试文件local.xlsx",tableHeadOne,fieldOne,data);

    }

    @RequestMapping("/test06")
    public List<Article> test06(int cityId,String startTime,String endTime) throws Exception {
        System.out.println("cityId: " + cityId);
        System.out.println("startTime: " + startTime);
        System.out.println("endTime: " + endTime);
        List<Article> articleList = articleService.queryAllByLimit(0, 10);
        return articleList;
    }

    //使用poi动态生成word文件，word中只包含一个表格
    @RequestMapping("/test07")
    public void test07(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Article> articleList = articleService.queryAllByLimit(0, 10);
        //word中表格相关数据
        List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Assert.notNull(article,"文章不存在");
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("id", article.getId());
            detailMap.put("title", article.getArtDesc());
            detailMap.put("content", article.getContent());
            detailMap.put("occurTime", article.getCreateTime());
            detailMap.put("rdl", article.getType());
            excelList.add(detailMap);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("excelList",excelList);
        data.put("tableName","article");
        data.put("tableSize",2000000);
        data.put("total",10);
        String templatePath = "/templates/model12.docx";
        List<String> bindListNames = new ArrayList<>();
        bindListNames.add("excelList");
        new DataExportToWordUtil().exportToWord(response,templatePath,"测试文件.docx","F:/test",data,bindListNames);
    }

    //使用poi动态生成word文件，word中包含多个表格
    @RequestMapping("/test08")
    public void test08(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Article> articleList = articleService.queryAllByLimit(0, 10);
        //word中表格相关数据
        List<Map<String, Object>> excelListOne = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("id", article.getId());
            detailMap.put("title", article.getArtDesc());
            detailMap.put("content", article.getContent());
            detailMap.put("occurTime", article.getCreateTime());
            detailMap.put("rdl", article.getType());
            excelListOne.add(detailMap);
        }
        List<WxzwZdyxqk> wxzwZdyxqkList = wxzwZdyxqkService.queryAllByLimit(0, 15);
        List<Map<String, Object>> excelListTwo = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < wxzwZdyxqkList.size(); i++) {
            WxzwZdyxqk wxzwZdyxqk = wxzwZdyxqkList.get(i);
            Map<String, Object> detailMapTwo = new HashMap<>();
            detailMapTwo.put("cityName", wxzwZdyxqk.getAreaName());
            detailMapTwo.put("termNum", wxzwZdyxqk.getTermNum());
            detailMapTwo.put("offlineTime", wxzwZdyxqk.getOfflineTime());
            detailMapTwo.put("onlineTime", wxzwZdyxqk.getOnlineTime());
            detailMapTwo.put("jlrq", wxzwZdyxqk.getJlrq());
            excelListTwo.add(detailMapTwo);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("excelListOne",excelListOne);
        data.put("excelListTwo",excelListTwo);
        data.put("tableNameOne","article");
        data.put("tableNameTwo","wxzw_zdyxqk");
        data.put("tableSizeOne",2000000);
        data.put("tableSizeTwo",13);
        data.put("totalOne",10);
        data.put("totalTwo",13);
        String templatePath = "/templates/model13.docx";
        List<String> bindListNames = new ArrayList<>();
        bindListNames.add("excelListOne");
        bindListNames.add("excelListTwo");
        //生成word，并通过浏览器下载
//        new DataExportToWordUtil().exportToWord(response,templatePath,"测试文件other.docx","F:/test",data,bindListNames);
        //生成word，保存至指定的本地文件夹中，不通过浏览器下载
        new DataExportToWordUtil().exportToWordAndSaveToLocal(templatePath,"测试文件local.docx","F:/test",data,bindListNames);
    }

    //使用poi动态生成word文件，word中包含多个表格
    @RequestMapping("/test09")
    public void test09(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<TtuTz> tzList = articleService.queryAllTtuTz();
        List<TtuZxl> zxlList = articleService.queryAllTtuZxl();
        //word中表格相关数据
        List<Map<String, Object>> excelListOne = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < tzList.size(); i++) {
            TtuTz ttuTz = tzList.get(i);
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("序号", i + 1);
            detailMap.put("地市", ttuTz.getCityName());
            detailMap.put("单位", ttuTz.getAreaName());
            detailMap.put("项目性质", ttuTz.getXmxzName());
            detailMap.put("变电站", ttuTz.getFacName());
            detailMap.put("线路名称", ttuTz.getFeederName());
            detailMap.put("终端名称", ttuTz.getTermName());
            detailMap.put("psrId", ttuTz.getPsrId());
            detailMap.put("终端类型", ttuTz.getTermTypeName());
            detailMap.put("二/三遥", ttuTz.getTermFlag());
            detailMap.put("是否投运", ttuTz.getRunStatus());
            detailMap.put("通信方式", ttuTz.getTxfsName());
            detailMap.put("备注", ttuTz.getRemarks());
            excelListOne.add(detailMap);
        }
        List<Map<String, Object>> excelListTwo = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < zxlList.size(); i++) {
            TtuZxl ttuZxl = zxlList.get(i);
            Map<String, Object> detailMapTwo = new HashMap<>();
            detailMapTwo.put("地市", ttuZxl.getCityName());
            detailMapTwo.put("单位", ttuZxl.getAreaName());
            detailMapTwo.put("变电站", ttuZxl.getFacName());
            detailMapTwo.put("线路名称", ttuZxl.getFeederName());
            detailMapTwo.put("线路DA模式", ttuZxl.getDaModel());
            detailMapTwo.put("终端名称", ttuZxl.getTermName());
            detailMapTwo.put("psrId", ttuZxl.getPsrId());
            detailMapTwo.put("终端IP", ttuZxl.getTermIp());
            detailMapTwo.put("终端厂家", ttuZxl.getTermSscj());
            detailMapTwo.put("运行方式", ttuZxl.getRunStatus());
            detailMapTwo.put("终端类型", ttuZxl.getTermTypeName());
            detailMapTwo.put("二/三遥", ttuZxl.getTermFlag());
            detailMapTwo.put("在线时长", ttuZxl.getOnTime());
            detailMapTwo.put("离线时长", ttuZxl.getOffTime());
            detailMapTwo.put("在线率", ttuZxl.getZdzxl());
            excelListTwo.add(detailMapTwo);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("excelListOne",excelListOne);
        data.put("excelListTwo",excelListTwo);
        data.put("tableNameOne","article");
        data.put("tableNameTwo","wxzw_zdyxqk");
        data.put("tableSizeOne",2000000);
        data.put("tableSizeTwo",13);
        data.put("totalOne",10);
        data.put("totalTwo",13);
        String templatePath = "/templates/model13.docx";
        List<String> bindListNames = new ArrayList<>();
        bindListNames.add("excelListOne");
        bindListNames.add("excelListTwo");
        //生成word，并通过浏览器下载
//        new DataExportToWordUtil().exportToWord(response,templatePath,"测试文件other.docx","F:/test",data,bindListNames);
        //生成word，保存至指定的本地文件夹中，不通过浏览器下载
        new DataExportToWordUtil().exportToWordAndSaveToLocal(templatePath,"测试文件local.docx","F:/test",data,bindListNames);
    }

    @RequestMapping(value = "/test10", method = RequestMethod.POST)
    public void test10(HttpServletRequest request,HttpServletResponse response,BufferedReader br,@RequestParam("name") String name) throws IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            System.out.println("headName: " + headName);
            String header = request.getHeader(headName);
            System.out.println("header: " + header);
        }
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        System.out.println("str:" + str);
        BufferedReader reader = request.getReader();
        System.out.println(request);
        System.out.println(name);
    }

    /**
     * @CrossOrigin 注解表示此接口允许跨域访问
     * @param pageNum
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @PostMapping("/test11")
    public PageInfo<Article> test11(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){
        PageInfo<Article> queryResult = articleService.getArticlePage(pageNum, pageSize);
        return queryResult;
    }

    @CrossOrigin
    @PostMapping("/test12")
    public PageInfo<Map<String,Object>> test12(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){
        PageInfo<Map<String,Object>> queryResult = articleService.getArticlePageOther(pageNum, pageSize);
        return queryResult;
    }

    @CrossOrigin
    @GetMapping("/test14")
    public List<Article> test14(@RequestParam("page") int page,@RequestParam("limit") int limit){
        List<Article> queryResult = articleService.test("article");
        return queryResult;
    }

    /**
     * jsp 页面访问测试
     * @return
     */
    @CrossOrigin
    @GetMapping("/login")
    public String test13(){
        return "login";
    }

    @CrossOrigin
    @GetMapping("/test15")
    public PageInfo<Map<String,Object>> test15(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){
        PageInfo<Map<String,Object>> queryResult = articleService.test01(pageNum, pageSize);
        return queryResult;
    }

    @CrossOrigin
    @PostMapping("/test16")
//    public String test16(@PathVariable("file") File file,@PathVariable("resourceType") String resourceType){
    public String test16(HttpServletRequest request){
        String resourceType = request.getParameter("resourceType");
//        log.info("File：" + file);
        log.info("resourceType：" + resourceType);
        Map<String,Integer> map = new HashMap<>();
        map.put("id",101);
        return JSON.toJSONString(map);
    }

}
