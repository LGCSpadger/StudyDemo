package com.test.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mybatis_plus.entity.BlogArticle;
import com.test.mybatis_plus.mapper.BlogArticleMapper;
import com.test.mybatis_plus.service.BlogArticleService;
import org.springframework.stereotype.Service;

/**
 * (BlogArticle)表服务实现类
 *
 * @author spadger
 * @since 2021-09-01 14:26:17
 */
@Service("blogArticleService")
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {

}