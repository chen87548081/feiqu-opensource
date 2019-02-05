package com.feiqu.system.pojo.response;

import com.feiqu.system.model.FqUser;

/**
 * @author cwd
 * @version UserActiveNumResponse, v 0.1 2018/10/10 cwd 1049766
 * 用户活跃度
 */
public class UserActiveNumResponse {
    private Integer userId;
    private Integer score;
    private String icon;
    private String name;
    private String city;
    private Integer rank;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public UserActiveNumResponse() {
    }

    public UserActiveNumResponse(FqUser fqUser, double score,Integer rank) {
        this.setCity(fqUser.getCity());
        this.setScore((int) score);
        this.setIcon(fqUser.getIcon());
        this.setName(fqUser.getNickname());
        this.setUserId(fqUser.getId());
        this.setRank(rank);
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
