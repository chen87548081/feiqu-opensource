package com.feiqu.system.mapper;

import com.feiqu.system.model.FqSign;
import com.feiqu.system.model.FqSignExample;
import com.feiqu.system.pojo.response.SignUserResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqSignMapper {
    long countByExample(FqSignExample example);

    int deleteByExample(FqSignExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqSign record);

    int insertSelective(FqSign record);

    List<FqSign> selectByExample(FqSignExample example);

    FqSign selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqSign record, @Param("example") FqSignExample example);

    int updateByExample(@Param("record") FqSign record, @Param("example") FqSignExample example);

    int updateByPrimaryKeySelective(FqSign record);

    int updateByPrimaryKey(FqSign record);

    List<SignUserResponse> selectWithUserByExample(FqSignExample example);
}