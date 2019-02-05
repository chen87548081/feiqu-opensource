package com.feiqu.system.model;


import com.feiqu.common.enums.YesNoEnum;

import java.io.Serializable;
import java.util.Date;

public class CMessage implements Serializable {
    private Integer id;

    private String content;

    private Date createTime;

    private Integer postUserId;

    private Integer receivedUserId;

    private Integer delFlag;

    private Integer type;

    private Integer isRead;

    public CMessage() {
        this.setIsRead(YesNoEnum.NO.getValue());
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(Integer postUserId) {
        this.postUserId = postUserId;
    }

    public Integer getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(Integer receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append(", postUserId=").append(postUserId);
        sb.append(", receivedUserId=").append(receivedUserId);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", type=").append(type);
        sb.append(", isRead=").append(isRead);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}