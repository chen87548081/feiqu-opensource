package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.CMessageExample;
import com.feiqu.system.pojo.response.Dialog;
import com.feiqu.system.pojo.response.MessageUserDetail;

import java.util.List;

/**
* CMessageService接口
* created by cwd on 2017/10/17.
*/
public interface CMessageService extends BaseService<CMessage, CMessageExample> {

    List<MessageUserDetail> selectMyMsgsByMessage(CMessageExample example);

    //只拿到自己的好友的对话的信息 排除官方消息！
    List<Dialog> selectDialogsByUserId(Integer id);

    /*
    查看我的对话详情，根据我的用户id和对方用户id查询对话
     */
    List<MessageUserDetail> selectDialogDetail(int myUserId, int friendUserId, String order);
}