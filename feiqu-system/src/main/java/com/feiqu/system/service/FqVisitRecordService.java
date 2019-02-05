package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqVisitRecord;
import com.feiqu.system.model.FqVisitRecordExample;
import com.feiqu.system.pojo.response.FqVisitRecordResponse;

import java.util.List;

/**
* FqVisitRecordService接口
* created by cwd on 2017/12/17.
*/
public interface FqVisitRecordService extends BaseService<FqVisitRecord, FqVisitRecordExample> {

    List<FqVisitRecordResponse> selectVisitsByExample(FqVisitRecordExample example);
}