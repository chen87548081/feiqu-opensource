package com.feiqu.system.mapper;

import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqWebsiteDirMapper {
    long countByExample(FqWebsiteDirExample example);

    int deleteByExample(FqWebsiteDirExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqWebsiteDir record);

    int insertSelective(FqWebsiteDir record);

    List<FqWebsiteDir> selectByExample(FqWebsiteDirExample example);

    FqWebsiteDir selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqWebsiteDir record, @Param("example") FqWebsiteDirExample example);

    int updateByExample(@Param("record") FqWebsiteDir record, @Param("example") FqWebsiteDirExample example);

    int updateByPrimaryKeySelective(FqWebsiteDir record);

    int updateByPrimaryKey(FqWebsiteDir record);

    List<String> selectTypes();
}