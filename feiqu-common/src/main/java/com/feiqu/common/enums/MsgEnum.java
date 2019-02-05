package com.feiqu.common.enums;

public enum MsgEnum {
    OFFICIAL_MSG("官方消息",1),
    FRIEND_MSG("朋友消息",2),
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

    MsgEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
