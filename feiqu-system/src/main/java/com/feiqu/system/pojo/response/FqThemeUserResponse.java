package com.feiqu.system.pojo.response;


import com.feiqu.system.model.FqTheme;

/**
 * FqThemeUserResponse
 *
 * @author chenweidong
 * @date 2017/11/24
 */
public class FqThemeUserResponse extends FqTheme {
    private String icon;
    private String nickname;

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
}
