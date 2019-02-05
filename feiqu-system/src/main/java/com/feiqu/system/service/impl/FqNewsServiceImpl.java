package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqNewsMapper;
import com.feiqu.system.model.FqNews;
import com.feiqu.system.model.FqNewsExample;
import com.feiqu.system.service.FqNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* FqNewsService实现
* Created by cwd on 2018/11/14.
*/
@Service
@Transactional
@BaseService

public class FqNewsServiceImpl extends BaseServiceImpl<FqNewsMapper, FqNews, FqNewsExample> implements FqNewsService {

    private static Logger _log = LoggerFactory.getLogger(FqNewsServiceImpl.class);

    @Autowired
    FqNewsMapper fqNewsMapper;

}