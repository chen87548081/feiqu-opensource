package com.feiqu.system.mapper;

import com.feiqu.system.model.ApiDocProjectUser;
import com.feiqu.system.model.ApiDocProjectUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiDocProjectUserMapper {
    long countByExample(ApiDocProjectUserExample example);

    int deleteByExample(ApiDocProjectUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ApiDocProjectUser record);

    int insertSelective(ApiDocProjectUser record);

    List<ApiDocProjectUser> selectByExample(ApiDocProjectUserExample example);

    ApiDocProjectUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ApiDocProjectUser record, @Param("example") ApiDocProjectUserExample example);

    int updateByExample(@Param("record") ApiDocProjectUser record, @Param("example") ApiDocProjectUserExample example);

    int updateByPrimaryKeySelective(ApiDocProjectUser record);

    int updateByPrimaryKey(ApiDocProjectUser record);
}