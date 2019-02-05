package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.ApiDocProjectUserMapper;
import com.feiqu.system.model.ApiDocProjectUser;
import com.feiqu.system.model.ApiDocProjectUserExample;
import com.feiqu.system.service.ApiDocProjectUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* ApiDocProjectUserService实现
* Created by cwd on 2019/1/17.
*/
@Service
@Transactional
@BaseService
public class ApiDocProjectUserServiceImpl extends BaseServiceImpl<ApiDocProjectUserMapper, ApiDocProjectUser, ApiDocProjectUserExample> implements ApiDocProjectUserService {

    private static Logger _log = LoggerFactory.getLogger(ApiDocProjectUserServiceImpl.class);

    @Autowired
    ApiDocProjectUserMapper apiDocProjectUserMapper;

}