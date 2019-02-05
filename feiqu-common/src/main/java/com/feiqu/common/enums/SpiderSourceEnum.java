package com.feiqu.common.enums;

/**
 * Created by Administrator on 2017/10/19.
 */
public enum SpiderSourceEnum {
    V2EX("https://www.v2ex.com","v2ex"),
    READ_HUB("https://readhub.cn","readhub"),


    ;

    String desc;
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    SpiderSourceEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
