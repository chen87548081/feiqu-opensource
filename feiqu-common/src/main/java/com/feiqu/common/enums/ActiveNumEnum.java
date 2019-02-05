package com.feiqu.common.enums;

//活跃度 ww
public enum ActiveNumEnum {
    POST_THOUGHT("发表想法",5),
    POST_ARTICLE("发表文章",20),
    POST_COMMENT("发表评论",2),
    POST_LIKE("点赞",1),
    SIGN_IN("签到",2),
    VIDEO_UPLOAD("上传视频",20),
    UPDATE_BG_IMG("更新背景图片",2),
    UPDATE_ICON("更新头像",2),
    ;
    String desc;
    Integer value;//活跃度的值

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

    ActiveNumEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
