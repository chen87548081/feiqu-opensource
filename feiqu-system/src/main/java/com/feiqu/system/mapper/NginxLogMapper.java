package com.feiqu.system.mapper;

import com.feiqu.system.model.NginxLog;
import com.feiqu.system.model.NginxLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NginxLogMapper {
    long countByExample(NginxLogExample example);

    int deleteByExample(NginxLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NginxLog record);

    int insertSelective(NginxLog record);

    List<NginxLog> selectByExample(NginxLogExample example);

    NginxLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NginxLog record, @Param("example") NginxLogExample example);

    int updateByExample(@Param("record") NginxLog record, @Param("example") NginxLogExample example);

    int updateByPrimaryKeySelective(NginxLog record);

    int updateByPrimaryKey(NginxLog record);

    long countUvByExample(NginxLogExample example);
}