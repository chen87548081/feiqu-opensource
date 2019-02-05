package com.feiqu.common.enums;

/**
 * Created by Administrator on 2018/8/11.
 */
public enum OrderEnum {
    DESC("desc","降序"),
    ASC("asc","升序"),;

    private String code;
    private String desc;

    OrderEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
