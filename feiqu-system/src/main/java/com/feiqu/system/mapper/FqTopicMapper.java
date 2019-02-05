package com.feiqu.system.mapper;

import com.feiqu.system.model.FqTopic;
import com.feiqu.system.model.FqTopicExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqTopicMapper {
    long countByExample(FqTopicExample example);

    int deleteByExample(FqTopicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqTopic record);

    int insertSelective(FqTopic record);

    List<FqTopic> selectByExampleWithBLOBs(FqTopicExample example);

    List<FqTopic> selectByExample(FqTopicExample example);

    FqTopic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqTopic record, @Param("example") FqTopicExample example);

    int updateByExampleWithBLOBs(@Param("record") FqTopic record, @Param("example") FqTopicExample example);

    int updateByExample(@Param("record") FqTopic record, @Param("example") FqTopicExample example);

    int updateByPrimaryKeySelective(FqTopic record);

    int updateByPrimaryKeyWithBLOBs(FqTopic record);

    int updateByPrimaryKey(FqTopic record);
}