package com.feiqu.system.mapper;

import com.feiqu.system.model.FqMusic;
import com.feiqu.system.model.FqMusicExample;
import com.feiqu.system.pojo.response.FqMusicResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqMusicMapper {
    long countByExample(FqMusicExample example);

    int deleteByExample(FqMusicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqMusic record);

    int insertSelective(FqMusic record);

    List<FqMusic> selectByExample(FqMusicExample example);

    FqMusic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqMusic record, @Param("example") FqMusicExample example);

    int updateByExample(@Param("record") FqMusic record, @Param("example") FqMusicExample example);

    int updateByPrimaryKeySelective(FqMusic record);

    int updateByPrimaryKey(FqMusic record);

    List<FqMusicResponse> selectWithUserByExample(FqMusicExample example);
}