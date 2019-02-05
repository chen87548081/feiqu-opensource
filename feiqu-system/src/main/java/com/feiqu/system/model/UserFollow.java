package com.feiqu.system.model;

import java.util.Date;

public class UserFollow {
    private Integer id;

    private Integer followerUserId;

    private Integer followedUserId;

    private Date createTime;

    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Integer followerUserId) {
        this.followerUserId = followerUserId;
    }

    public Integer getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Integer followedUserId) {
        this.followedUserId = followedUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public UserFollow() {
    }

    public UserFollow(Integer followerUserId, Integer followedUserId, Date createTime, Integer delFlag) {
        this.followerUserId = followerUserId;
        this.followedUserId = followedUserId;
        this.createTime = createTime;
        this.delFlag = delFlag;
    }
}