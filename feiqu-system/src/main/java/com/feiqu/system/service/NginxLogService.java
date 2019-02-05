package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.NginxLog;
import com.feiqu.system.model.NginxLogExample;

/**
* NginxLogService接口
* created by cwd on 2017/11/14.
*/
public interface NginxLogService extends BaseService<NginxLog, NginxLogExample> {

    long countUvByExample(NginxLogExample example);
}