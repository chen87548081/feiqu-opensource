package com.feiqu.system.mapper;

import com.feiqu.system.model.ApiDocModule;
import com.feiqu.system.model.ApiDocModuleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiDocModuleMapper {
    long countByExample(ApiDocModuleExample example);

    int deleteByExample(ApiDocModuleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ApiDocModule record);

    int insertSelective(ApiDocModule record);

    List<ApiDocModule> selectByExample(ApiDocModuleExample example);

    ApiDocModule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ApiDocModule record, @Param("example") ApiDocModuleExample example);

    int updateByExample(@Param("record") ApiDocModule record, @Param("example") ApiDocModuleExample example);

    int updateByPrimaryKeySelective(ApiDocModule record);

    int updateByPrimaryKey(ApiDocModule record);
}