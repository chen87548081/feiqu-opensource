package com.feiqu.system.pojo.response;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/17.
 */
public class ArticleCollectResponse {

    private String articleTitle;
    private Integer topicId;
    private Date collectTime;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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
