package com.feiqu.log.util;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.feiqu.log.constant.LogMessageConstant;
import com.feiqu.system.model.collectData.RunLogMessage;

/**
 * className：TraceLogMessageFactory
 * description： TODO
 * time：2020-05-13.14:04
 *
 * @author Tank
 * @version 1.0.0
 */
public class TraceLogMessageFactory<T> {

   /* public static TraceLogMessage getTraceLogMessage(TraceMessage traceMessage, String appName, long time) {
        TraceLogMessage traceLogMessage = new TraceLogMessage();
        traceLogMessage.setAppName(appName);
        traceLogMessage.setTraceId(traceMessage.getTraceId());
        traceLogMessage.setMethod(traceMessage.getMessageType());
        traceLogMessage.setTime(time);
        traceLogMessage.setPosition(traceMessage.getPosition());
        traceLogMessage.setPositionNum(traceMessage.getPositionNum().get());
        return traceLogMessage;
    }*/

    public static RunLogMessage getLogMessage(String appName, String message, long time) {
        RunLogMessage logMessage = new RunLogMessage();
        String ip = LogMessageConstant.log_ip;
        logMessage.setServerName(ip);
        logMessage.setAppName(appName);
        logMessage.setContent(message);
        logMessage.setDateTime(DateUtil.date(time).toString(DatePattern.NORM_DATETIME_PATTERN));
        logMessage.setDtTime(time);
//        logMessage.setTraceId(TraceId.logTraceID.get());
        logMessage.setTraceId("");
        return logMessage;
    }

    public static String packageMessage(String message, Object[] args) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(message);
        for (Object arg : args) {
            builder.append("\n").append(arg);
        }
        return builder.toString();
    }
}
