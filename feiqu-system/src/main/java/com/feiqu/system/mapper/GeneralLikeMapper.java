package com.feiqu.system.mapper;

import com.feiqu.system.model.GeneralLike;
import com.feiqu.system.model.GeneralLikeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralLikeMapper {
    long countByExample(GeneralLikeExample example);

    int deleteByExample(GeneralLikeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GeneralLike record);

    int insertSelective(GeneralLike record);

    List<GeneralLike> selectByExample(GeneralLikeExample example);

    GeneralLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GeneralLike record, @Param("example") GeneralLikeExample example);

    int updateByExample(@Param("record") GeneralLike record, @Param("example") GeneralLikeExample example);

    int updateByPrimaryKeySelective(GeneralLike record);

    int updateByPrimaryKey(GeneralLike record);
}