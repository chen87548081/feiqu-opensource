package com.feiqu.system.pojo.response;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/17.
 */
public class ThoughtCollectResponse {

    private String content;
    private Integer topicId;
    private Date collectTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
