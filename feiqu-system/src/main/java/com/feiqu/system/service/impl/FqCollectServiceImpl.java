package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqCollectMapper;
import com.feiqu.system.model.FqCollect;
import com.feiqu.system.model.FqCollectExample;
import com.feiqu.system.pojo.response.ArticleCollectResponse;
import com.feiqu.system.pojo.response.ThoughtCollectResponse;
import com.feiqu.system.service.FqCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqCollectService实现
* Created by cwd on 2018/1/15.
*/
@Service
@Transactional
@BaseService

public class FqCollectServiceImpl extends BaseServiceImpl<FqCollectMapper, FqCollect, FqCollectExample> implements FqCollectService {

    private static Logger _log = LoggerFactory.getLogger(FqCollectServiceImpl.class);

    @Autowired
    FqCollectMapper fqCollectMapper;

    public List<ArticleCollectResponse> selectWithArticleByEntity(FqCollect fqCollect) {
        return fqCollectMapper.selectWithArticleByEntity(fqCollect);
    }

    public List<Integer> selectTopicIdsByTypeAndUid(Integer type, Integer uid) {
        return fqCollectMapper.selectTopicIdsByTypeAndUid(type,uid);
    }

    public List<ThoughtCollectResponse> selectWithThoughtByEntity(FqCollect fqCollect) {
        return fqCollectMapper.selectWithThoughtByEntity(fqCollect);
    }
}