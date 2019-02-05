package com.feiqu.system.mapper;

import com.feiqu.system.model.FqTopicReply;
import com.feiqu.system.model.FqTopicReplyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqTopicReplyMapper {
    long countByExample(FqTopicReplyExample example);

    int deleteByExample(FqTopicReplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqTopicReply record);

    int insertSelective(FqTopicReply record);

    List<FqTopicReply> selectByExample(FqTopicReplyExample example);

    FqTopicReply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqTopicReply record, @Param("example") FqTopicReplyExample example);

    int updateByExample(@Param("record") FqTopicReply record, @Param("example") FqTopicReplyExample example);

    int updateByPrimaryKeySelective(FqTopicReply record);

    int updateByPrimaryKey(FqTopicReply record);
}