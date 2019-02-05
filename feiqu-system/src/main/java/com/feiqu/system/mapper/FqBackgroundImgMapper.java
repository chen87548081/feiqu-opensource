package com.feiqu.system.mapper;

import com.feiqu.system.model.FqBackgroundImg;
import com.feiqu.system.model.FqBackgroundImgExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqBackgroundImgMapper {
    long countByExample(FqBackgroundImgExample example);

    int deleteByExample(FqBackgroundImgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqBackgroundImg record);

    int insertSelective(FqBackgroundImg record);

    List<FqBackgroundImg> selectByExample(FqBackgroundImgExample example);

    FqBackgroundImg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqBackgroundImg record, @Param("example") FqBackgroundImgExample example);

    int updateByExample(@Param("record") FqBackgroundImg record, @Param("example") FqBackgroundImgExample example);

    int updateByPrimaryKeySelective(FqBackgroundImg record);

    int updateByPrimaryKey(FqBackgroundImg record);
}