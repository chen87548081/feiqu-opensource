package com.feiqu.system.pojo.response;

import com.feiqu.system.model.Thought;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */
public class ThoughtWithUser extends Thought {

    private String username;

    private String nickname;

    private String icon;

    private Integer role;

    private boolean collected = false;

    private List<String> pictures;

    public ThoughtWithUser() {
    }

    public ThoughtWithUser(Thought thought) {
        super();
        this.setArea(thought.getArea());
        this.setCommentCount(thought.getCommentCount());
        this.setCreateTime(thought.getCreateTime());
        this.setId(thought.getId());
        this.setLikeCount(thought.getLikeCount());
        this.setThoughtContent(thought.getThoughtContent());
        this.setUserId(thought.getUserId());
        if(StringUtils.isNotEmpty(thought.getPicList())){
            this.setPictures(Arrays.asList(thought.getPicList().split(",")));
        }
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
