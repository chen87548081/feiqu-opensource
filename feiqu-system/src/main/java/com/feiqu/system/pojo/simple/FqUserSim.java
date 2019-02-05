package com.feiqu.system.pojo.simple;

import com.feiqu.system.model.FqUser;

/**
 * Created by Administrator on 2017/12/12.
 */
public class FqUserSim {
    private String nickname;
    private String icon;
    private Integer userId;
    private String city;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FqUserSim(FqUser fqUser) {
        this.setCity(fqUser.getCity());
        this.setIcon(fqUser.getIcon());
        this.setNickname(fqUser.getNickname());
        this.setUserId(fqUser.getId());
    }


}
