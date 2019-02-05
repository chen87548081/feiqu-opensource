package com.feiqu.system.mapper;

import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqUserMapper {
    long countByExample(FqUserExample example);

    int deleteByExample(FqUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqUser record);

    int insertSelective(FqUser record);

    List<FqUser> selectByExample(FqUserExample example);

    FqUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqUser record, @Param("example") FqUserExample example);

    int updateByExample(@Param("record") FqUser record, @Param("example") FqUserExample example);

    int updateByPrimaryKeySelective(FqUser record);

    int updateByPrimaryKey(FqUser record);
}