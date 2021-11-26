package com.feiqu.log.constant;

import com.feiqu.log.util.IpGetter;

/**
 * className：LogMessageConstant
 * description：
 * time：2020-05-12.10:10
 *
 * @author Tank
 * @version 1.0.0
 */
public interface LogMessageConstant {

    /**
     * 链路日志前缀
     */
    String TRACE_PRE = "TRACE:";
    /**
     * 当前链路开始标志
     */
    String TRACE_START = "<";

    /**
     * 当前链路结束标志
     */
    String TRACE_END = ">";


    /**
     * 链路日志存入ES的索引后缀
     */
    String LOG_KEY_TRACE = "feiqu_log_list_trace";

    String ES_INDEX = "feiqu_log_";

    String ES_TYPE = "feiqu_log";

    String LOG_TYPE_RUN = "run";

    String LOG_TYPE_TRACE = "trace";

    String log_ip = IpGetter.getIp();
    String DELIM_STR = "{}";
}
