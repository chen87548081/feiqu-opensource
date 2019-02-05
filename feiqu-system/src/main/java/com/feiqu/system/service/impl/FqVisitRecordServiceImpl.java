package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqVisitRecordMapper;
import com.feiqu.system.model.FqVisitRecord;
import com.feiqu.system.model.FqVisitRecordExample;
import com.feiqu.system.pojo.response.FqVisitRecordResponse;
import com.feiqu.system.service.FqVisitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqVisitRecordService实现
* Created by cwd on 2017/12/17.
*/
@Service
@Transactional
@BaseService

public class FqVisitRecordServiceImpl extends BaseServiceImpl<FqVisitRecordMapper, FqVisitRecord, FqVisitRecordExample> implements FqVisitRecordService {

    private static Logger _log = LoggerFactory.getLogger(FqVisitRecordServiceImpl.class);

    @Autowired
    FqVisitRecordMapper fqVisitRecordMapper;

    public List<FqVisitRecordResponse> selectVisitsByExample(FqVisitRecordExample example) {
        return fqVisitRecordMapper.selectVisitsByExample(example);
    }
}