package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class GeneralComment implements Serializable {
    private Integer id;

    private Integer topicId;

    private Integer topicType;

    private String content;

    private Integer postUserId;

    private Date createTime;

    private Integer likeCount=0;

    private Integer delFlag=0;

    private Integer hasReply;

    private static final long serialVersionUID = 1L;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getHasReply() {
        return hasReply;
    }

    public void setHasReply(Integer hasReply) {
        this.hasReply = hasReply;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", topicId=").append(topicId);
        sb.append(", topicType=").append(topicType);
        sb.append(", content=").append(content);
        sb.append(", postUserId=").append(postUserId);
        sb.append(", createTime=").append(createTime);
        sb.append(", likeCount=").append(likeCount);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", hasReply=").append(hasReply);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}