package com.feiqu.common.enums;

/**
 * Created by chenweidong on 2018/2/25.
 */
public enum CommonStatusEnum {
    DEL("删除",-1),
    AUTHING("审核中",0),
    AUTHED("审核通过",1),
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

    CommonStatusEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
