package com.feiqu.common.enums;

/**
 * Created by Administrator on 2017/10/19.
 */
public enum TopicTypeEnum {
    THOUGHT_TYPE("想法类型",1),
    ARTICLE_TYPE("文章类型",2),
    COMMENT_TYPE("评论类型",3),
    JOB_TYPE("工作类型",4),
    NOTICE_TYPE("通知类型",5),
    BBS_TYPE("BBS类型",6),
    MUSIC_TYPE("音频类型",7),
    PIC_TYPE("图片类型",8),
    NEWS_TYPE("新闻类型",9),
    TOPIC_TYPE("话题类型",10),

    ;

    String desc;
    Integer value;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    TopicTypeEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
