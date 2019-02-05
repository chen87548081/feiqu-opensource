package com.feiqu.system.mapper;

import com.feiqu.system.model.UserActivate;
import com.feiqu.system.model.UserActivateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserActivateMapper {
    long countByExample(UserActivateExample example);

    int deleteByExample(UserActivateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserActivate record);

    int insertSelective(UserActivate record);

    List<UserActivate> selectByExample(UserActivateExample example);

    UserActivate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserActivate record, @Param("example") UserActivateExample example);

    int updateByExample(@Param("record") UserActivate record, @Param("example") UserActivateExample example);

    int updateByPrimaryKeySelective(UserActivate record);

    int updateByPrimaryKey(UserActivate record);
}