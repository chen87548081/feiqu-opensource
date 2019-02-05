package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqThemeMapper;
import com.feiqu.system.model.FqTheme;
import com.feiqu.system.model.FqThemeExample;
import com.feiqu.system.pojo.response.FqThemeUserResponse;
import com.feiqu.system.service.FqThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqThemeService实现
* Created by cwd on 2017/11/24.
*/
@Service
@Transactional
@BaseService

public class FqThemeServiceImpl extends BaseServiceImpl<FqThemeMapper, FqTheme, FqThemeExample> implements FqThemeService {

    private static Logger _log = LoggerFactory.getLogger(FqThemeServiceImpl.class);

    @Autowired
    FqThemeMapper fqThemeMapper;

    public List<FqThemeUserResponse> selectWithUserByExample(FqThemeExample themeExample) {
        return fqThemeMapper.selectWithUserByExample(themeExample);
    }
}