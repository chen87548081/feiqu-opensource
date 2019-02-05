package com.feiqu.system.mapper;

import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.CMessageExample;
import com.feiqu.system.pojo.response.Dialog;
import com.feiqu.system.pojo.response.MessageUserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CMessageMapper {
    long countByExample(CMessageExample example);

    int deleteByExample(CMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CMessage record);

    int insertSelective(CMessage record);

    List<CMessage> selectByExample(CMessageExample example);

    CMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CMessage record, @Param("example") CMessageExample example);

    int updateByExample(@Param("record") CMessage record, @Param("example") CMessageExample example);

    int updateByPrimaryKeySelective(CMessage record);

    int updateByPrimaryKey(CMessage record);

    List<MessageUserDetail> selectMyMsgsByMessage(CMessageExample example);

    List<Dialog> selectDialogsByUserId(Integer id);

    List<MessageUserDetail> selectDialogDetail(Map map);
}