package com.feiqu.system.mapper;

import com.feiqu.system.model.FqUserActivityRecord;
import com.feiqu.system.model.FqUserActivityRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqUserActivityRecordMapper {
    long countByExample(FqUserActivityRecordExample example);

    int deleteByExample(FqUserActivityRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqUserActivityRecord record);

    int insertSelective(FqUserActivityRecord record);

    List<FqUserActivityRecord> selectByExample(FqUserActivityRecordExample example);

    FqUserActivityRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqUserActivityRecord record, @Param("example") FqUserActivityRecordExample example);

    int updateByExample(@Param("record") FqUserActivityRecord record, @Param("example") FqUserActivityRecordExample example);

    int updateByPrimaryKeySelective(FqUserActivityRecord record);

    int updateByPrimaryKey(FqUserActivityRecord record);
}