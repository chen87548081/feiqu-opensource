package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.ArticleExample;
import com.feiqu.system.pojo.response.ArticleUserDetail;

import java.util.List;

/**
* ArticleService接口
* created by cwd on 2017/9/29.
*/
public interface ArticleService extends BaseService<Article, ArticleExample> {
    List<ArticleUserDetail> selectUserByExampleWithBLOBs(ArticleExample example);
}