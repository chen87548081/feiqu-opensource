package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.UserFollowMapper;
import com.feiqu.system.model.UserFollow;
import com.feiqu.system.model.UserFollowExample;
import com.feiqu.system.pojo.response.FollowUserResponse;
import com.feiqu.system.service.UserFollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* UserFollowService实现
* Created by cwd on 2017/11/5.
*/
@Service
@Transactional
@BaseService

public class UserFollowServiceImpl extends BaseServiceImpl<UserFollowMapper, UserFollow, UserFollowExample> implements UserFollowService {

    private static Logger _log = LoggerFactory.getLogger(UserFollowServiceImpl.class);

    @Autowired
    UserFollowMapper userFollowMapper;

    public List<FollowUserResponse> selectFollowees(UserFollowExample example) {
        return userFollowMapper.selectFollowees(example);
    }

    public List<FollowUserResponse> selectFans(UserFollowExample example) {
        return userFollowMapper.selectFans(example);
    }
}