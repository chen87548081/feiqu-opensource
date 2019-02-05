package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class FqTheme implements Serializable {
    private Integer id;

    private Integer userId;

    private String title;

    private Date createTime;

    private Integer delFlag;

    private Integer commentCount;

    private String label;

    private Integer type;

    private String lastPubNickname;

    private Date lastPubTime;

    private Integer seeCount;

    private String area;

    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLastPubNickname() {
        return lastPubNickname;
    }

    public void setLastPubNickname(String lastPubNickname) {
        this.lastPubNickname = lastPubNickname == null ? null : lastPubNickname.trim();
    }

    public Date getLastPubTime() {
        return lastPubTime;
    }

    public void setLastPubTime(Date lastPubTime) {
        this.lastPubTime = lastPubTime;
    }

    public Integer getSeeCount() {
        return seeCount;
    }

    public void setSeeCount(Integer seeCount) {
        this.seeCount = seeCount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", createTime=").append(createTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", label=").append(label);
        sb.append(", type=").append(type);
        sb.append(", lastPubNickname=").append(lastPubNickname);
        sb.append(", lastPubTime=").append(lastPubTime);
        sb.append(", seeCount=").append(seeCount);
        sb.append(", area=").append(area);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}