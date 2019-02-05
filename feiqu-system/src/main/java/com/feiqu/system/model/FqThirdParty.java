package com.feiqu.system.model;


import com.feiqu.system.pojo.ThirdPartyUser;

import java.io.Serializable;
import java.util.Date;

public class FqThirdParty implements Serializable {
    private Integer id;

    private String openid;

    private String provider;

    private Integer userId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public FqThirdParty(ThirdPartyUser thirdUser) {
        this.openid = thirdUser.getOpenid();
        this.provider = thirdUser.getProvider();
        this.userId = thirdUser.getUserId();
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider == null ? null : provider.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public FqThirdParty() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openid=").append(openid);
        sb.append(", provider=").append(provider);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}