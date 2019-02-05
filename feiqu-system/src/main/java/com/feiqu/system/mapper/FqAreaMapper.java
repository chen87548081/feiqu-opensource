package com.feiqu.system.mapper;

import com.feiqu.system.model.FqArea;
import com.feiqu.system.model.FqAreaExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqAreaMapper {
    long countByExample(FqAreaExample example);

    int deleteByExample(FqAreaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqArea record);

    int insertSelective(FqArea record);

    List<FqArea> selectByExample(FqAreaExample example);

    FqArea selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqArea record, @Param("example") FqAreaExample example);

    int updateByExample(@Param("record") FqArea record, @Param("example") FqAreaExample example);

    int updateByPrimaryKeySelective(FqArea record);

    int updateByPrimaryKey(FqArea record);
}