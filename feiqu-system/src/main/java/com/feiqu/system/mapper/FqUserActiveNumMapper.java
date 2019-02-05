package com.feiqu.system.mapper;

import com.feiqu.system.model.FqUserActiveNum;
import com.feiqu.system.model.FqUserActiveNumExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FqUserActiveNumMapper {
    long countByExample(FqUserActiveNumExample example);

    int deleteByExample(FqUserActiveNumExample example);

    int insert(FqUserActiveNum record);

    int insertSelective(FqUserActiveNum record);

    List<FqUserActiveNum> selectByExample(FqUserActiveNumExample example);

    int updateByExampleSelective(@Param("record") FqUserActiveNum record, @Param("example") FqUserActiveNumExample example);

    int updateByExample(@Param("record") FqUserActiveNum record, @Param("example") FqUserActiveNumExample example);
}