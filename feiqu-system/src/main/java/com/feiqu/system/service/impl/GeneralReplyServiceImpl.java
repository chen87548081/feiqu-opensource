package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.GeneralReplyMapper;
import com.feiqu.system.model.GeneralReply;
import com.feiqu.system.model.GeneralReplyExample;
import com.feiqu.system.pojo.response.DetailReplyResponse;
import com.feiqu.system.service.GeneralReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* GeneralReplyService实现
* Created by cwd on 2017/10/19.
*/
@Service
@Transactional
@BaseService

public class GeneralReplyServiceImpl extends BaseServiceImpl<GeneralReplyMapper, GeneralReply, GeneralReplyExample> implements GeneralReplyService {

    private static Logger _log = LoggerFactory.getLogger(GeneralReplyServiceImpl.class);

    @Autowired
    GeneralReplyMapper generalReplyMapper;

    public List<DetailReplyResponse> selectWithUserByExample(GeneralReplyExample replyExample) {
        return generalReplyMapper.selectWithUserByExample(replyExample);
    }
}