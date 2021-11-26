package com.feiqu.quartz.mapper;

import com.feiqu.quartz.model.SysJobLog;
import com.feiqu.quartz.model.SysJobLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysJobLogMapper {
    long countByExample(SysJobLogExample example);

    int deleteByExample(SysJobLogExample example);

    int deleteByPrimaryKey(Integer jobLogId);

    int insert(SysJobLog record);

    int insertSelective(SysJobLog record);

    List<SysJobLog> selectByExample(SysJobLogExample example);

    SysJobLog selectByPrimaryKey(Integer jobLogId);

    int updateByExampleSelective(@Param("record") SysJobLog record, @Param("example") SysJobLogExample example);

    int updateByExample(@Param("record") SysJobLog record, @Param("example") SysJobLogExample example);

    int updateByPrimaryKeySelective(SysJobLog record);

    int updateByPrimaryKey(SysJobLog record);
}