package com.feiqu.system.model;

import java.util.Date;

public class UserActivate {

    private Integer id;

    private Integer userId;

    private String token;

    private Date createTime;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserActivate() {
    }

    public UserActivate(Integer userId, String token, Date createTime) {
        this.userId = userId;
        this.token = token;
        this.createTime = createTime;
    }
}