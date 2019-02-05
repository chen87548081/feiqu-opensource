package com.feiqu.system.service.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.FqUserPayWayMapper;
import com.feiqu.system.model.FqUserPayWay;
import com.feiqu.system.model.FqUserPayWayExample;
import com.feiqu.system.service.FqUserPayWayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* FqUserPayWayService实现
* Created by cwd on 2019/2/2.
*/
@Service
@Transactional
@BaseService
public class FqUserPayWayServiceImpl extends BaseServiceImpl<FqUserPayWayMapper, FqUserPayWay, FqUserPayWayExample> implements FqUserPayWayService {

    private static Logger _log = LoggerFactory.getLogger(FqUserPayWayServiceImpl.class);

    @Autowired
    FqUserPayWayMapper fqUserPayWayMapper;

}