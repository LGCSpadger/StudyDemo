package com.test.pub.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.common.utils.DataExportToExcelUtil;
import com.test.pub.controller.TestController;
import com.test.pub.entity.Article;
import com.test.pub.entity.TtuTz;
import com.test.pub.entity.TtuZxl;
import com.test.pub.mapper.ArticleMapper;
import com.test.pub.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (Article)表服务实现类
 *
 * @author LGCSpadger
 * @since 2021-05-14 20:08:18
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    private DataExportToExcelUtil excelUtil = new DataExportToExcelUtil();

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Article queryById(Long id) {
        return this.articleMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Article> queryAllByLimit(int offset, int limit) {
        return this.articleMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<Article> queryAll(Article article) {
        return this.articleMapper.queryAll(article);
    }

    /**
     * 新增数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    @Override
    public Article insert(Article article) {
        this.articleMapper.insert(article);
        return article;
    }

    @Override
    public int insertBatch(List<Article> entities) {
        int result = this.articleMapper.insertBatch(entities);
        return result;
    }

    /**
     * 修改数据
     *
     * @param article 实例对象
     * @return 实例对象
     */
    @Override
    public Article update(Article article) {
        this.articleMapper.update(article);
        return this.queryById(article.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.articleMapper.deleteById(id) > 0;
    }

    @Override
    public void exportDataToExcel(HttpServletResponse response,String filePath,String fileName,String sheetName) {
//        List<Article> list = articleMapper.queryAll(null);
        List<Article> list = articleMapper.queryAllByLimit(1,10);
        List<String> tableHead = new ArrayList<>();
        tableHead.add("文章id");
        tableHead.add("文章主题");
        tableHead.add("文章内容");
        tableHead.add("创作时间");
        tableHead.add("rdl");
        log.info("开始导出数据到excel，数据量为：" + list.size() + " 条");
        excelUtil.dataExportToExcelXlsxNoStyle(response,sheetName,filePath,fileName,tableHead,list);
        boolean delete = fileDelete(filePath + File.separator + fileName);
        if (delete) {
            log.info("临时文件已成功删除！");
        } else {
            log.info("临时文件删除失败！");
        }
    }

    @Override
    public List<TtuTz> queryAllTtuTz() {
        return articleMapper.selectAllTtuTz();
    }

    @Override
    public List<TtuZxl> queryAllTtuZxl() {
        return articleMapper.selectAllTtuZxl();
    }

    @Override
    public List<Article> test(String tableName) {
        return articleMapper.test(tableName);
    }

    @Override
    public PageInfo<Map<String,Object>> test01(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> lists = articleMapper.test01(pageNum,pageSize);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(lists);
        return pageInfo;
    }

    /**
     * 删除某个文件或者某个文件夹
     * @param path
     * @return
     */
    private boolean fileDelete(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                result = file.delete();
            } else if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    result = files[i].delete();
                    if (!result) {
                        break;
                    }
                }
            }
        } else {
            log.info("所删除的文件不存在");
        }
        if (!result) {
            log.info("删除文件失败！");
        }
        return result;
    }

    @Override
    public List<Article> getAllArticleByPageF(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> lists = articleMapper.findAllArticleByPageF(pageNum,pageSize);
        return lists;
    }

    @Override
    public PageInfo<Article> getArticlePage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> lists = articleMapper.findAllArticleByPageF(pageNum,pageSize);
        PageInfo<Article> pageInfo = new PageInfo<Article>(lists);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String,Object>> getArticlePageOther(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> lists = articleMapper.findAllArticleByPageFOther(pageNum,pageSize);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(lists);
        return pageInfo;
    }
}