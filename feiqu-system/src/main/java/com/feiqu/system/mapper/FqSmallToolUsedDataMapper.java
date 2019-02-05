package com.feiqu.system.mapper;

import com.feiqu.system.model.FqSmallToolUsedData;
import com.feiqu.system.model.FqSmallToolUsedDataExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqSmallToolUsedDataMapper {
    long countByExample(FqSmallToolUsedDataExample example);

    int deleteByExample(FqSmallToolUsedDataExample example);

    int insert(FqSmallToolUsedData record);

    int insertSelective(FqSmallToolUsedData record);

    List<FqSmallToolUsedData> selectByExample(FqSmallToolUsedDataExample example);

    int updateByExampleSelective(@Param("record") FqSmallToolUsedData record, @Param("example") FqSmallToolUsedDataExample example);

    int updateByExample(@Param("record") FqSmallToolUsedData record, @Param("example") FqSmallToolUsedDataExample example);
}