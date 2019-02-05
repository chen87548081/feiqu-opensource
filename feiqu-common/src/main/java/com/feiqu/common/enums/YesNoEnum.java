package com.feiqu.common.enums;

/**
 * Created by Administrator on 2017/10/22.
 */
public enum YesNoEnum {
    YES("是",1),
    NO("否",0),
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

    YesNoEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
