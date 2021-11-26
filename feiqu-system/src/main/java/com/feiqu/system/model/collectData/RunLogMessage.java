package com.feiqu.system.model.collectData;

import java.io.Serializable;

public class RunLogMessage implements Serializable {
    private Long id;

    private String traceId;

    private String appName;

    private String method;

    private String serverName;

    private Long dtTime;

    private String logLevel;

    private String className;

    private String logType;

    private String dateTime;

    private String content;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getDtTime() {
        return dtTime;
    }

    public void setDtTime(Long dtTime) {
        this.dtTime = dtTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", traceId=").append(traceId);
        sb.append(", appName=").append(appName);
        sb.append(", method=").append(method);
        sb.append(", serverName=").append(serverName);
        sb.append(", dtTime=").append(dtTime);
        sb.append(", logLevel=").append(logLevel);
        sb.append(", className=").append(className);
        sb.append(", logType=").append(logType);
        sb.append(", dateTime=").append(dateTime);
        sb.append(", content=").append(content);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RunLogMessage other = (RunLogMessage) that;
        return (this.getId() == null ? other.getId() == null :
                this.getId().equals(other.getId()))
                && (this.getTraceId() == null ? other.getTraceId() == null :
                this.getTraceId().equals(other.getTraceId()))
                && (this.getAppName() == null ? other.getAppName() == null :
                this.getAppName().equals(other.getAppName()))
                && (this.getMethod() == null ? other.getMethod() == null :
                this.getMethod().equals(other.getMethod()))
                && (this.getServerName() == null ?
                other.getServerName() == null :
                this.getServerName().equals(other.getServerName()))
                && (this.getDtTime() == null ? other.getDtTime() == null :
                this.getDtTime().equals(other.getDtTime()))
                && (this.getLogLevel() == null ? other.getLogLevel() == null
                : this.getLogLevel().equals(other.getLogLevel()))
                && (this.getClassName() == null ?
                other.getClassName() == null :
                this.getClassName().equals(other.getClassName()))
                && (this.getLogType() == null ? other.getLogType() == null :
                this.getLogType().equals(other.getLogType()))
                && (this.getDateTime() == null ? other.getDateTime() == null
                : this.getDateTime().equals(other.getDateTime()))
                && (this.getContent() == null ? other.getContent() == null :
                this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTraceId() == null) ? 0 :
                getTraceId().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 :
                getAppName().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 :
                getMethod().hashCode());
        result = prime * result + ((getServerName() == null) ? 0 :
                getServerName().hashCode());
        result = prime * result + ((getDtTime() == null) ? 0 :
                getDtTime().hashCode());
        result = prime * result + ((getLogLevel() == null) ? 0 :
                getLogLevel().hashCode());
        result = prime * result + ((getClassName() == null) ? 0 :
                getClassName().hashCode());
        result = prime * result + ((getLogType() == null) ? 0 :
                getLogType().hashCode());
        result = prime * result + ((getDateTime() == null) ? 0 :
                getDateTime().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }
}