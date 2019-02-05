package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.JobTalk;
import com.feiqu.system.model.JobTalkExample;
import com.feiqu.system.pojo.response.JobTalkUserDetail;

import java.util.List;

/**
* JobTalkService接口
* created by cwd on 2017/10/28.
*/
public interface JobTalkService extends BaseService<JobTalk, JobTalkExample> {

    List<JobTalkUserDetail> selectWithUserByExample(JobTalkExample example);
}