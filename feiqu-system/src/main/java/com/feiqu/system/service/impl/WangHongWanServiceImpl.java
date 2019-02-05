package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.WangHongWanMapper;
import com.feiqu.system.model.WangHongWan;
import com.feiqu.system.model.WangHongWanExample;
import com.feiqu.system.service.WangHongWanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* WangHongWanService实现
* Created by cwd on 2018/12/22.
*/
@Service
@Transactional
@BaseService
public class WangHongWanServiceImpl extends BaseServiceImpl<WangHongWanMapper, WangHongWan, WangHongWanExample> implements WangHongWanService {

    private static Logger _log = LoggerFactory.getLogger(WangHongWanServiceImpl.class);

    @Autowired
    WangHongWanMapper wangHongWanMapper;

}