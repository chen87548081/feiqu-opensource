package com.feiqu.system.pojo.response;

import com.feiqu.system.model.FqSign;

/**
 * SignUserResponse
 *
 * @author chenweidong
 * @date 2017/11/17
 */
public class SignUserResponse extends FqSign {

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
