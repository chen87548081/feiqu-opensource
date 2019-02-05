package com.feiqu.system.mapper;

import com.feiqu.system.model.FqVisitRecord;
import com.feiqu.system.model.FqVisitRecordExample;
import com.feiqu.system.pojo.response.FqVisitRecordResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqVisitRecordMapper {
    long countByExample(FqVisitRecordExample example);

    int deleteByExample(FqVisitRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqVisitRecord record);

    int insertSelective(FqVisitRecord record);

    List<FqVisitRecord> selectByExample(FqVisitRecordExample example);

    FqVisitRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqVisitRecord record, @Param("example") FqVisitRecordExample example);

    int updateByExample(@Param("record") FqVisitRecord record, @Param("example") FqVisitRecordExample example);

    int updateByPrimaryKeySelective(FqVisitRecord record);

    int updateByPrimaryKey(FqVisitRecord record);

    List<FqVisitRecordResponse> selectVisitsByExample(FqVisitRecordExample example);
}