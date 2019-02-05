package com.feiqu.system.mapper;

import com.feiqu.system.model.FqShortVideo;
import com.feiqu.system.model.FqShortVideoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqShortVideoMapper {
    long countByExample(FqShortVideoExample example);

    int deleteByExample(FqShortVideoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqShortVideo record);

    int insertSelective(FqShortVideo record);

    List<FqShortVideo> selectByExample(FqShortVideoExample example);

    FqShortVideo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqShortVideo record, @Param("example") FqShortVideoExample example);

    int updateByExample(@Param("record") FqShortVideo record, @Param("example") FqShortVideoExample example);

    int updateByPrimaryKeySelective(FqShortVideo record);

    int updateByPrimaryKey(FqShortVideo record);
}