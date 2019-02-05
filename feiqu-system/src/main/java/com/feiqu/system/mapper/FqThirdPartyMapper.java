package com.feiqu.system.mapper;

import com.feiqu.system.model.FqThirdParty;
import com.feiqu.system.model.FqThirdPartyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqThirdPartyMapper {
    long countByExample(FqThirdPartyExample example);

    int deleteByExample(FqThirdPartyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqThirdParty record);

    int insertSelective(FqThirdParty record);

    List<FqThirdParty> selectByExample(FqThirdPartyExample example);

    FqThirdParty selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqThirdParty record, @Param("example") FqThirdPartyExample example);

    int updateByExample(@Param("record") FqThirdParty record, @Param("example") FqThirdPartyExample example);

    int updateByPrimaryKeySelective(FqThirdParty record);

    int updateByPrimaryKey(FqThirdParty record);
}