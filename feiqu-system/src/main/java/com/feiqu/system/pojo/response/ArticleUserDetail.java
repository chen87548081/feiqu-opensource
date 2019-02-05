package com.feiqu.system.pojo.response;

import com.feiqu.system.model.Article;

/**
 * @author cwd
 * @create 2017-10-17:18
 **/
public class ArticleUserDetail extends Article {
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
