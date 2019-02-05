package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.CMessageMapper;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.CMessageExample;
import com.feiqu.system.pojo.response.Dialog;
import com.feiqu.system.pojo.response.MessageUserDetail;
import com.feiqu.system.service.CMessageService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* CMessageService实现
* Created by cwd on 2017/10/17.
*/
@Service
@Transactional
@BaseService

public class CMessageServiceImpl extends BaseServiceImpl<CMessageMapper, CMessage, CMessageExample> implements CMessageService {

//    private static Logger _log = LoggerFactory.getLogger(CMessageServiceImpl.class);

    @Autowired
    CMessageMapper cMessageMapper;

    public List<MessageUserDetail> selectMyMsgsByMessage(CMessageExample example) {
        return cMessageMapper.selectMyMsgsByMessage(example);
    }

    public List<Dialog> selectDialogsByUserId(Integer userId) {
        return cMessageMapper.selectDialogsByUserId(userId);
    }

    public List<MessageUserDetail> selectDialogDetail(int myUserId, int friendUserId,String order) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("myUserId",myUserId);
        map.put("friendUserId",friendUserId);
        map.put("dialogOrder",order);
        return cMessageMapper.selectDialogDetail(map);
    }
}