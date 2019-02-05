package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.GeneralLikeMapper;
import com.feiqu.system.model.GeneralLike;
import com.feiqu.system.model.GeneralLikeExample;
import com.feiqu.system.service.GeneralLikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* GeneralLikeService实现
* Created by cwd on 2017/10/24.
*/
@Service
@Transactional
@BaseService

public class GeneralLikeServiceImpl extends BaseServiceImpl<GeneralLikeMapper, GeneralLike, GeneralLikeExample> implements GeneralLikeService {

    private static Logger _log = LoggerFactory.getLogger(GeneralLikeServiceImpl.class);

    @Autowired
    GeneralLikeMapper generalLikeMapper;

}