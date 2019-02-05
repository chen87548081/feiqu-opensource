package com.feiqu.system.mapper;

import com.feiqu.system.model.GeneralReply;
import com.feiqu.system.model.GeneralReplyExample;
import com.feiqu.system.pojo.response.DetailReplyResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralReplyMapper {
    long countByExample(GeneralReplyExample example);

    int deleteByExample(GeneralReplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GeneralReply record);

    int insertSelective(GeneralReply record);

    List<GeneralReply> selectByExample(GeneralReplyExample example);

    GeneralReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GeneralReply record, @Param("example") GeneralReplyExample example);

    int updateByExample(@Param("record") GeneralReply record, @Param("example") GeneralReplyExample example);

    int updateByPrimaryKeySelective(GeneralReply record);

    int updateByPrimaryKey(GeneralReply record);

    List<DetailReplyResponse> selectWithUserByExample(GeneralReplyExample replyExample);
}