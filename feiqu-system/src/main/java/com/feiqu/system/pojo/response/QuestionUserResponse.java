package com.feiqu.system.pojo.response;

import com.feiqu.system.model.Question;

/**
 * Created by Administrator on 2017/11/4.
 */
public class QuestionUserResponse extends Question {
    private String nickname;
    private String icon;

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
}
