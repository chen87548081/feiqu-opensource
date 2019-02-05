package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqCollect;
import com.feiqu.system.model.FqCollectExample;
import com.feiqu.system.pojo.response.ArticleCollectResponse;
import com.feiqu.system.pojo.response.ThoughtCollectResponse;

import java.util.List;

/**
* FqCollectService接口
* created by cwd on 2018/1/15.
*/
public interface FqCollectService extends BaseService<FqCollect, FqCollectExample> {

    List<ArticleCollectResponse> selectWithArticleByEntity(FqCollect fqCollect);

    List<Integer> selectTopicIdsByTypeAndUid(Integer type, Integer uid);

    List<ThoughtCollectResponse> selectWithThoughtByEntity(FqCollect fqCollect);
}