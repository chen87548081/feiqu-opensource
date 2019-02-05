package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.UploadImgRecordMapper;
import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import com.feiqu.system.service.UploadImgRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* UploadImgRecordService实现
* Created by cwd on 2017/10/31.
*/
@Service
@Transactional
@BaseService

public class UploadImgRecordServiceImpl extends BaseServiceImpl<UploadImgRecordMapper, UploadImgRecord, UploadImgRecordExample> implements UploadImgRecordService {

    private static Logger _log = LoggerFactory.getLogger(UploadImgRecordServiceImpl.class);

    @Autowired
    UploadImgRecordMapper uploadImgRecordMapper;

}