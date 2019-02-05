package com.feiqu.system.mapper;

import com.feiqu.system.model.ApiDocInterface;
import com.feiqu.system.model.ApiDocInterfaceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiDocInterfaceMapper {
    long countByExample(ApiDocInterfaceExample example);

    int deleteByExample(ApiDocInterfaceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ApiDocInterface record);

    int insertSelective(ApiDocInterface record);

    List<ApiDocInterface> selectByExampleWithBLOBs(ApiDocInterfaceExample example);

    List<ApiDocInterface> selectByExample(ApiDocInterfaceExample example);

    ApiDocInterface selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ApiDocInterface record, @Param("example") ApiDocInterfaceExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiDocInterface record, @Param("example") ApiDocInterfaceExample example);

    int updateByExample(@Param("record") ApiDocInterface record, @Param("example") ApiDocInterfaceExample example);

    int updateByPrimaryKeySelective(ApiDocInterface record);

    int updateByPrimaryKeyWithBLOBs(ApiDocInterface record);

    int updateByPrimaryKey(ApiDocInterface record);
}