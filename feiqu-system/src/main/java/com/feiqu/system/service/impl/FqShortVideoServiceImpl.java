package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqShortVideoMapper;
import com.feiqu.system.model.FqShortVideo;
import com.feiqu.system.model.FqShortVideoExample;
import com.feiqu.system.service.FqShortVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* FqShortVideoService实现
* Created by cwd on 2018/10/28.
*/
@Service
@Transactional
@BaseService

public class FqShortVideoServiceImpl extends BaseServiceImpl<FqShortVideoMapper, FqShortVideo, FqShortVideoExample> implements FqShortVideoService {

    private static Logger _log = LoggerFactory.getLogger(FqShortVideoServiceImpl.class);

    @Autowired
    FqShortVideoMapper fqShortVideoMapper;

}