package com.feiqu.system.mapper;

import com.feiqu.system.model.FqUserPayWay;
import com.feiqu.system.model.FqUserPayWayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FqUserPayWayMapper {
    long countByExample(FqUserPayWayExample example);

    int deleteByExample(FqUserPayWayExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqUserPayWay record);

    int insertSelective(FqUserPayWay record);

    List<FqUserPayWay> selectByExample(FqUserPayWayExample example);

    FqUserPayWay selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqUserPayWay record, @Param("example") FqUserPayWayExample example);

    int updateByExample(@Param("record") FqUserPayWay record, @Param("example") FqUserPayWayExample example);

    int updateByPrimaryKeySelective(FqUserPayWay record);

    int updateByPrimaryKey(FqUserPayWay record);
}