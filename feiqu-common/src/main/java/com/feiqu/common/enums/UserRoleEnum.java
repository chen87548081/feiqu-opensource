package com.feiqu.common.enums;

/**
 * UserRoleEnum
 *
 * @author chenweidong
 * @date 2017/11/28
 */
public enum  UserRoleEnum {
    ADMIN_USER_ROLE("官方人员",1),
    GUEST_ROLE("游客",2),
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

    UserRoleEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
