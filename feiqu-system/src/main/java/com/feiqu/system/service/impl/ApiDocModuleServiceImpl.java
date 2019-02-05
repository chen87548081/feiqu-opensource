package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.ApiDocModuleMapper;
import com.feiqu.system.model.ApiDocModule;
import com.feiqu.system.model.ApiDocModuleExample;
import com.feiqu.system.service.ApiDocModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* ApiDocModuleService实现
* Created by cwd on 2019/1/13.
*/
@Service
@Transactional
@BaseService
public class ApiDocModuleServiceImpl extends BaseServiceImpl<ApiDocModuleMapper, ApiDocModule, ApiDocModuleExample> implements ApiDocModuleService {

    private static Logger _log = LoggerFactory.getLogger(ApiDocModuleServiceImpl.class);

    @Autowired
    ApiDocModuleMapper apiDocModuleMapper;

}