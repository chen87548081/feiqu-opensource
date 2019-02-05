package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqThirdPartyMapper;
import com.feiqu.system.model.FqThirdParty;
import com.feiqu.system.model.FqThirdPartyExample;
import com.feiqu.system.service.FqThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* FqThirdPartyService实现
* Created by cwd on 2017/11/18.
*/
@Service
@Transactional
@BaseService

public class FqThirdPartyServiceImpl extends BaseServiceImpl<FqThirdPartyMapper, FqThirdParty, FqThirdPartyExample> implements FqThirdPartyService {

    private static Logger _log = LoggerFactory.getLogger(FqThirdPartyServiceImpl.class);

    @Autowired
    FqThirdPartyMapper fqThirdPartyMapper;

}