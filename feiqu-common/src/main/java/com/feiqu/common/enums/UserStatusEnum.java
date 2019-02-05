package com.feiqu.common.enums;

/**
 * Created by chenweidong on 2018/2/25.
 */
public enum UserStatusEnum {
    NORMAL("正常",0),
    DEL("删除",1),
    FROZEN("冻结",2),
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

    UserStatusEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
