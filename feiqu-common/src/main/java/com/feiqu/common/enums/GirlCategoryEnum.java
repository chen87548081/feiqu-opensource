package com.feiqu.common.enums;

/**
 * Created by Administrator on 2018/10/30.
 */
public enum GirlCategoryEnum {

    NV_SHENG("女神","g_nvsheng"),
    XIAO_QINGXIN("小清新","g_xqx"),
    LONG_LEG("长腿","g_ct"),
    GU_ZHUANG("古装","g_gz"),
    CITY_GIRL("都市白领","g_dsbl"),
    OU_MEI_FAN("欧美范","g_omf"),
    XIAO_HUA("校花","g_xiaohua"),
    MODEL("模特儿","g_moter"),
    YONG_MODEL("嫩模","g_yong_moter"),
    XIEZHEN("写真","g_xiezhen"),
    QINGCHUN("清纯","g_qingchun"),
    STUDENT_GIRL("学生妹","g_stu_girl"),
    TRAVEL_PHOTO("旅拍","g_travel_photo"),
    SEXY("性感","g_sexy"),
    NAN_SHENG("男神","b_nansheng"),
    ;

    GirlCategoryEnum(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    String desc;
    String code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
