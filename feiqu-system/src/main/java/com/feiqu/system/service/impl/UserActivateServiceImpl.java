package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.UserActivateMapper;
import com.feiqu.system.model.UserActivate;
import com.feiqu.system.model.UserActivateExample;
import com.feiqu.system.service.UserActivateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* UserActivateService实现
* Created by cwd on 2017/11/2.
*/
@Service
@Transactional
@BaseService

public class UserActivateServiceImpl extends BaseServiceImpl<UserActivateMapper, UserActivate, UserActivateExample> implements UserActivateService {

    private static Logger _log = LoggerFactory.getLogger(UserActivateServiceImpl.class);

    @Autowired
    UserActivateMapper userActivateMapper;

}