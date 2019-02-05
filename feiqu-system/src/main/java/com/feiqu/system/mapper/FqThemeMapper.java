package com.feiqu.system.mapper;

import com.feiqu.system.model.FqTheme;
import com.feiqu.system.model.FqThemeExample;
import com.feiqu.system.pojo.response.FqThemeUserResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqThemeMapper {
    long countByExample(FqThemeExample example);

    int deleteByExample(FqThemeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqTheme record);

    int insertSelective(FqTheme record);

    List<FqTheme> selectByExampleWithBLOBs(FqThemeExample example);

    List<FqTheme> selectByExample(FqThemeExample example);

    FqTheme selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqTheme record, @Param("example") FqThemeExample example);

    int updateByExampleWithBLOBs(@Param("record") FqTheme record, @Param("example") FqThemeExample example);

    int updateByExample(@Param("record") FqTheme record, @Param("example") FqThemeExample example);

    int updateByPrimaryKeySelective(FqTheme record);

    int updateByPrimaryKeyWithBLOBs(FqTheme record);

    int updateByPrimaryKey(FqTheme record);

    List<FqThemeUserResponse> selectWithUserByExample(FqThemeExample themeExample);
}