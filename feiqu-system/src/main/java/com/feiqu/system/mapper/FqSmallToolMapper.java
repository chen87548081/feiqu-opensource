package com.feiqu.system.mapper;

import com.feiqu.system.model.FqSmallTool;
import com.feiqu.system.model.FqSmallToolExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqSmallToolMapper {
    long countByExample(FqSmallToolExample example);

    int deleteByExample(FqSmallToolExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqSmallTool record);

    int insertSelective(FqSmallTool record);

    List<FqSmallTool> selectByExample(FqSmallToolExample example);

    FqSmallTool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqSmallTool record, @Param("example") FqSmallToolExample example);

    int updateByExample(@Param("record") FqSmallTool record, @Param("example") FqSmallToolExample example);

    int updateByPrimaryKeySelective(FqSmallTool record);

    int updateByPrimaryKey(FqSmallTool record);
}