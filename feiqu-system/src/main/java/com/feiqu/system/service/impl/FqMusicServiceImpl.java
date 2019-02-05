package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqMusicMapper;
import com.feiqu.system.model.FqMusic;
import com.feiqu.system.model.FqMusicExample;
import com.feiqu.system.pojo.response.FqMusicResponse;
import com.feiqu.system.service.FqMusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqMusicService实现
* Created by cwd on 2017/12/24.
*/
@Service
@Transactional
@BaseService

public class FqMusicServiceImpl extends BaseServiceImpl<FqMusicMapper, FqMusic, FqMusicExample> implements FqMusicService {

    private static Logger _log = LoggerFactory.getLogger(FqMusicServiceImpl.class);

    @Autowired
    FqMusicMapper fqMusicMapper;

    public List<FqMusicResponse> selectWithUserByExample(FqMusicExample example) {
        return fqMusicMapper.selectWithUserByExample(example);
    }
}