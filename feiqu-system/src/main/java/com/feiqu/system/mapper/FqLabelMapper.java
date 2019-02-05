package com.feiqu.system.mapper;

import com.feiqu.system.model.FqLabel;
import com.feiqu.system.model.FqLabelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqLabelMapper {
    long countByExample(FqLabelExample example);

    int deleteByExample(FqLabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqLabel record);

    int insertSelective(FqLabel record);

    List<FqLabel> selectByExample(FqLabelExample example);

    FqLabel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqLabel record, @Param("example") FqLabelExample example);

    int updateByExample(@Param("record") FqLabel record, @Param("example") FqLabelExample example);

    int updateByPrimaryKeySelective(FqLabel record);

    int updateByPrimaryKey(FqLabel record);
}