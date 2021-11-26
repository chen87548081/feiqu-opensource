package com.feiqu.quartz.mapper;

import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysJobMapper {
    long countByExample(SysJobExample example);

    int deleteByExample(SysJobExample example);

    int deleteByPrimaryKey(Integer jobId);

    int insert(SysJob record);

    int insertSelective(SysJob record);

    List<SysJob> selectByExample(SysJobExample example);

    SysJob selectByPrimaryKey(Integer jobId);

    int updateByExampleSelective(@Param("record") SysJob record, @Param("example") SysJobExample example);

    int updateByExample(@Param("record") SysJob record, @Param("example") SysJobExample example);

    int updateByPrimaryKeySelective(SysJob record);

    int updateByPrimaryKey(SysJob record);
}