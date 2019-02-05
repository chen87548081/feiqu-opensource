package com.feiqu.system.mapper;

import com.feiqu.system.model.WangHongWan;
import com.feiqu.system.model.WangHongWanExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WangHongWanMapper {
    long countByExample(WangHongWanExample example);

    int deleteByExample(WangHongWanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WangHongWan record);

    int insertSelective(WangHongWan record);

    List<WangHongWan> selectByExample(WangHongWanExample example);

    WangHongWan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WangHongWan record, @Param("example") WangHongWanExample example);

    int updateByExample(@Param("record") WangHongWan record, @Param("example") WangHongWanExample example);

    int updateByPrimaryKeySelective(WangHongWan record);

    int updateByPrimaryKey(WangHongWan record);
}