package com.feiqu.log.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.feiqu.log.constant.LogMessageConstant;
import com.feiqu.system.model.collectData.RunLogMessage;
import org.slf4j.helpers.MessageFormatter;


public class LogMessageUtil {

    public static RunLogMessage getLogMessage(final String appName, final ILoggingEvent iLoggingEvent) {
       /* TraceMessage traceMessage = LogMessageThreadLocal.logMessageThreadLocal.get();
        String formattedMessage = iLoggingEvent.getFormattedMessage();
        if (formattedMessage.startsWith(LogMessageConstant.TRACE_PRE)) {
            return TraceLogMessageFactory.getTraceLogMessage(
                    traceMessage, appName, iLoggingEvent.getTimeStamp());
        }*/
        String formattedMessage = getMessage(iLoggingEvent);
        RunLogMessage logMessage =
                TraceLogMessageFactory.getLogMessage(appName, formattedMessage, iLoggingEvent.getTimeStamp());
        if(ObjectUtil.isEmpty(iLoggingEvent.getCallerData())){
            logMessage.setClassName("");
            logMessage.setMethod("");
        }else {
            StackTraceElement stackTraceElement = iLoggingEvent.getCallerData()[0];
            logMessage.setClassName(stackTraceElement.getClassName());
            logMessage.setMethod(stackTraceElement.getMethodName());
        }
        logMessage.setLogLevel(iLoggingEvent.getLevel().toString());

        return logMessage;
    }

    private static String getMessage(ILoggingEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{ExceptionUtil.stacktraceToString(throwableProxy.getThrowable())};
                return packageMessage(logEvent.getMessage(), args);
            } else {
                Object[] args = logEvent.getArgumentArray();
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof Throwable) {
                            args[i] = ExceptionUtil.stacktraceToString((Throwable) args[i]);
                        }
                    }
                    return packageMessage(logEvent.getMessage(), args);
                }
            }
        }
        return logEvent.getFormattedMessage();
    }

    private static String packageMessage(String message, Object[] args) {
        if (message.indexOf(LogMessageConstant.DELIM_STR) > 0) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return TraceLogMessageFactory.packageMessage(message, args);
    }
}
