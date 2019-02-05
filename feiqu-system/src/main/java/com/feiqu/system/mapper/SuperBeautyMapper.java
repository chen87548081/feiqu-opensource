package com.feiqu.system.mapper;

import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.SuperBeautyExample;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuperBeautyMapper {
    long countByExample(SuperBeautyExample example);

    int deleteByExample(SuperBeautyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuperBeauty record);

    int insertSelective(SuperBeauty record);

    List<SuperBeauty> selectByExample(SuperBeautyExample example);

    SuperBeauty selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuperBeauty record, @Param("example") SuperBeautyExample example);

    int updateByExample(@Param("record") SuperBeauty record, @Param("example") SuperBeautyExample example);

    int updateByPrimaryKeySelective(SuperBeauty record);

    int updateByPrimaryKey(SuperBeauty record);

    List<BeautyUserResponse> selectDetailByExample(SuperBeautyExample example);

    List<BeautyUserResponse> selectDetailById(Integer beautyId);
}