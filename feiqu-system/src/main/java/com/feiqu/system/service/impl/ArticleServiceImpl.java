package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.ArticleMapper;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.ArticleExample;
import com.feiqu.system.pojo.response.ArticleUserDetail;
import com.feiqu.system.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* ArticleService实现
* Created by cwd on 2017/9/29.
*/
@Service
@Transactional
@BaseService

public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article, ArticleExample> implements ArticleService {

    private static Logger _log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    ArticleMapper articleMapper;

    /*
    吧content去掉，减小mysql传输的成本 这里我之前写错了。
     */
    public List<ArticleUserDetail> selectUserByExampleWithBLOBs(ArticleExample example) {
        return articleMapper.selectUserByExampleWithBLOBs(example);
    }
}