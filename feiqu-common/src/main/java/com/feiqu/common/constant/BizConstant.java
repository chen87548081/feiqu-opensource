package com.feiqu.common.constant;

import cn.hutool.core.map.MapUtil;

import java.util.Map;

public class BizConstant {
    public static Integer SIGN_DAYS_QUDOU_NUM_5 = 1;
    public static Integer SIGN_DAYS_QUDOU_NUM_15 = 5;
    public static Integer SIGN_DAYS_QUDOU_NUM_30 = 10;
    public static Integer SIGN_DAYS_QUDOU_NUM_30_MORE = 20;
    public static String ALIOSS_IMG_SUFFIX = "?x-oss-process=style/low_qua";
    public static Map<Integer,String> FQ_USER_ROLE_MAP = MapUtil.newHashMap();
    public static String LOG_KEY = "feiqu_log_list";
    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    public static Integer INIT_QUDOU_NUM= 20;
    //每个月的 最大活跃度 限制
    public static Integer USER_MAX_ACTIVE_NUM= 1000;
}
