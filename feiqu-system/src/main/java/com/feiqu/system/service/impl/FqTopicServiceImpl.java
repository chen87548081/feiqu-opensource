package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.FqTopicMapper;
import com.feiqu.system.mapper.FqTopicReplyMapper;
import com.feiqu.system.model.FqTopic;
import com.feiqu.system.model.FqTopicExample;
import com.feiqu.system.model.FqTopicReply;
import com.feiqu.system.model.FqTopicReplyExample;
import com.feiqu.system.service.FqTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* FqTopicService实现
* Created by cwd on 2018/11/29.
*/
@Service
@Transactional
@BaseService

public class FqTopicServiceImpl extends BaseServiceImpl<FqTopicMapper, FqTopic, FqTopicExample> implements FqTopicService {

    private static Logger _log = LoggerFactory.getLogger(FqTopicServiceImpl.class);

    @Autowired
    FqTopicMapper fqTopicMapper;
    @Autowired
    FqTopicReplyMapper fqTopicReplyMapper;

    @Override
    public List<FqTopicReply> listReplies(Long id) {
        FqTopicReplyExample example = new FqTopicReplyExample();
        example.createCriteria().andTopicIdEqualTo(id);
        return fqTopicReplyMapper.selectByExample(example);
    }

    @Override
    public void insertFqTopicReply(FqTopicReply fqTopic) {
        fqTopicReplyMapper.insert(fqTopic);
    }
}