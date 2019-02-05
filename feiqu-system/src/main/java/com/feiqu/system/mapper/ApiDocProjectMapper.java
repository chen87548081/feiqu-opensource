package com.feiqu.system.mapper;

import com.feiqu.system.model.ApiDocProject;
import com.feiqu.system.model.ApiDocProjectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiDocProjectMapper {
    long countByExample(ApiDocProjectExample example);

    int deleteByExample(ApiDocProjectExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ApiDocProject record);

    int insertSelective(ApiDocProject record);

    List<ApiDocProject> selectByExample(ApiDocProjectExample example);

    ApiDocProject selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ApiDocProject record, @Param("example") ApiDocProjectExample example);

    int updateByExample(@Param("record") ApiDocProject record, @Param("example") ApiDocProjectExample example);

    int updateByPrimaryKeySelective(ApiDocProject record);

    int updateByPrimaryKey(ApiDocProject record);
}