package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqWebsiteDirMapper;
import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;
import com.feiqu.system.service.FqWebsiteDirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqWebsiteDirService实现
* Created by cwd on 2018/1/23.
*/
@Service
@Transactional
@BaseService

public class FqWebsiteDirServiceImpl extends BaseServiceImpl<FqWebsiteDirMapper, FqWebsiteDir, FqWebsiteDirExample> implements FqWebsiteDirService {

    private static Logger _log = LoggerFactory.getLogger(FqWebsiteDirServiceImpl.class);

    @Autowired
    FqWebsiteDirMapper fqWebsiteDirMapper;

    public List<String> selectTypes() {
        return fqWebsiteDirMapper.selectTypes();
    }
}