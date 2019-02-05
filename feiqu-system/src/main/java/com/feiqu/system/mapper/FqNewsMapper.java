package com.feiqu.system.mapper;

import com.feiqu.system.model.FqNews;
import com.feiqu.system.model.FqNewsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqNewsMapper {
    long countByExample(FqNewsExample example);

    int deleteByExample(FqNewsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqNews record);

    int insertSelective(FqNews record);

    List<FqNews> selectByExampleWithBLOBs(FqNewsExample example);

    List<FqNews> selectByExample(FqNewsExample example);

    FqNews selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqNews record, @Param("example") FqNewsExample example);

    int updateByExampleWithBLOBs(@Param("record") FqNews record, @Param("example") FqNewsExample example);

    int updateByExample(@Param("record") FqNews record, @Param("example") FqNewsExample example);

    int updateByPrimaryKeySelective(FqNews record);

    int updateByPrimaryKeyWithBLOBs(FqNews record);

    int updateByPrimaryKey(FqNews record);
}