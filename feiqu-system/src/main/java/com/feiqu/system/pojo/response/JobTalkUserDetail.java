package com.feiqu.system.pojo.response;

import com.feiqu.system.model.JobTalk;

/**
 * Created by Administrator on 2017/10/29.
 */
public class JobTalkUserDetail extends JobTalk {
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
