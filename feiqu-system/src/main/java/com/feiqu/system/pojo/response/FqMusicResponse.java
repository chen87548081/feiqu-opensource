package com.feiqu.system.pojo.response;

import com.feiqu.system.model.FqMusic;

/**
 * Created by Administrator on 2017/12/24.
 */
public class FqMusicResponse extends FqMusic {
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
