package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class FqVisitRecord implements Serializable {
    private Integer id;

    private Integer visitUserId;

    private Integer visitedUserId;

    private Date visitTime;

    private Integer delFlag;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVisitUserId() {
        return visitUserId;
    }

    public void setVisitUserId(Integer visitUserId) {
        this.visitUserId = visitUserId;
    }

    public Integer getVisitedUserId() {
        return visitedUserId;
    }

    public void setVisitedUserId(Integer visitedUserId) {
        this.visitedUserId = visitedUserId;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", visitUserId=").append(visitUserId);
        sb.append(", visitedUserId=").append(visitedUserId);
        sb.append(", visitTime=").append(visitTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}