package com.feiqu.common.enums;

/**
 * Created by Administrator on 2017/10/22.
 */
public enum SexEnum {
    BOY("男生",1),
    GIRL("女生",2),
    UNKONW("未知",3),

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

    SexEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
