package com.feiqu.system.model;

import java.util.Date;

public class GeneralLike {
    private Integer id;

    private Integer topicId;

    private Integer topicType;

    private Integer likeValue;

    private Integer postUserId;

    private Date createTime;

    private Integer delFlag;

    public GeneralLike() {
    }

    public GeneralLike(Integer topicId, Integer topicType, Integer likeValue, Integer postUserId, Date createTime, Integer delFlag) {
        this.topicId = topicId;
        this.topicType = topicType;
        this.likeValue = likeValue;
        this.postUserId = postUserId;
        this.createTime = createTime;
        this.delFlag = delFlag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getLikeValue() {
        return likeValue;
    }

    public void setLikeValue(Integer likeValue) {
        this.likeValue = likeValue;
    }

    public Integer getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(Integer postUserId) {
        this.postUserId = postUserId;
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


}