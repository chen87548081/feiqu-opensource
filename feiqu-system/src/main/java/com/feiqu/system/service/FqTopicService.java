package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqTopic;
import com.feiqu.system.model.FqTopicExample;
import com.feiqu.system.model.FqTopicReply;

import java.util.List;

/**
* FqTopicService接口
* created by cwd on 2018/11/29.
*/
public interface FqTopicService extends BaseService<FqTopic, FqTopicExample> {

    List<FqTopicReply> listReplies(Long id);

    void insertFqTopicReply(FqTopicReply fqTopic);
}