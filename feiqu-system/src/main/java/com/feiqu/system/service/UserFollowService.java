package com.feiqu.system.service;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.UserFollow;
import com.feiqu.system.model.UserFollowExample;
import com.feiqu.system.pojo.response.FollowUserResponse;

import java.util.List;

/**
* UserFollowService接口
* created by cwd on 2017/11/5.
*/
public interface UserFollowService extends BaseService<UserFollow, UserFollowExample> {

    List<FollowUserResponse> selectFollowees(UserFollowExample example);

    List<FollowUserResponse> selectFans(UserFollowExample example);
}