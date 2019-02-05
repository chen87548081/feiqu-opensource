package com.feiqu.system.pojo.response;

import com.feiqu.system.model.GeneralComment;

/**
 * Created by Administrator on 2018/12/23.
 */
public class CommentsWithThoughtResponse extends GeneralComment {
    private String thoughtContent;
    private Integer thoughtId;

    public String getThoughtContent() {
        return thoughtContent;
    }

    public void setThoughtContent(String thoughtContent) {
        this.thoughtContent = thoughtContent;
    }

    public Integer getThoughtId() {
        return thoughtId;
    }

    public void setThoughtId(Integer thoughtId) {
        this.thoughtId = thoughtId;
    }
}
