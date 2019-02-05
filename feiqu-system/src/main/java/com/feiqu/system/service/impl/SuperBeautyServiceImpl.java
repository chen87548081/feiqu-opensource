package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.SuperBeautyMapper;
import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.SuperBeautyExample;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.service.SuperBeautyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* SuperBeautyService实现
* Created by cwd on 2017/10/16.
*/
@Service
@Transactional
@BaseService

public class SuperBeautyServiceImpl extends BaseServiceImpl<SuperBeautyMapper, SuperBeauty, SuperBeautyExample> implements SuperBeautyService {

    private static Logger _log = LoggerFactory.getLogger(SuperBeautyServiceImpl.class);

    @Autowired
    SuperBeautyMapper superBeautyMapper;

    public List<BeautyUserResponse> selectDetailByExample(SuperBeautyExample example) {
        return superBeautyMapper.selectDetailByExample(example);
    }

    @Override
    public List<BeautyUserResponse> selectDetailById(Integer beautyId) {
        return superBeautyMapper.selectDetailById(beautyId);
    }
}