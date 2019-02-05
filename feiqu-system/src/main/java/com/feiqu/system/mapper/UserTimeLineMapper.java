package com.feiqu.system.mapper;

import com.feiqu.system.model.UserTimeLine;
import com.feiqu.system.model.UserTimeLineExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTimeLineMapper {
    long countByExample(UserTimeLineExample example);

    int deleteByExample(UserTimeLineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserTimeLine record);

    int insertSelective(UserTimeLine record);

    List<UserTimeLine> selectByExample(UserTimeLineExample example);

    UserTimeLine selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserTimeLine record, @Param("example") UserTimeLineExample example);

    int updateByExample(@Param("record") UserTimeLine record, @Param("example") UserTimeLineExample example);

    int updateByPrimaryKeySelective(UserTimeLine record);

    int updateByPrimaryKey(UserTimeLine record);
}