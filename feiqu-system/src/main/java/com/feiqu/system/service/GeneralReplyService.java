package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.GeneralReply;
import com.feiqu.system.model.GeneralReplyExample;
import com.feiqu.system.pojo.response.DetailReplyResponse;

import java.util.List;

/**
* GeneralReplyService接口
* created by cwd on 2017/10/19.
*/
public interface GeneralReplyService extends BaseService<GeneralReply, GeneralReplyExample> {

    List<DetailReplyResponse> selectWithUserByExample(GeneralReplyExample replyExample);
}