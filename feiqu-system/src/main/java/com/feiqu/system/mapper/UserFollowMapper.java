package com.feiqu.system.mapper;

import com.feiqu.system.model.UserFollow;
import com.feiqu.system.model.UserFollowExample;
import com.feiqu.system.pojo.response.FollowUserResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowMapper {
    long countByExample(UserFollowExample example);

    int deleteByExample(UserFollowExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFollow record);

    int insertSelective(UserFollow record);

    List<UserFollow> selectByExample(UserFollowExample example);

    UserFollow selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFollow record, @Param("example") UserFollowExample example);

    int updateByExample(@Param("record") UserFollow record, @Param("example") UserFollowExample example);

    int updateByPrimaryKeySelective(UserFollow record);

    int updateByPrimaryKey(UserFollow record);

    List<FollowUserResponse> selectFollowees(UserFollowExample example);

    List<FollowUserResponse> selectFans(UserFollowExample example);
}