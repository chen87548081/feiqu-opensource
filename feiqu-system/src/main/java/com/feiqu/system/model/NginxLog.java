package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class NginxLog implements Serializable {
    private Integer id;

    private String ip;

    private String localTime;

    private String method;

    private String url;

    private String http;

    private String status;

    private String bytes;

    private String referer;

    private String xforward;

    private Double requestTime;

    private String userAgent;

    private Date createTime;

    private Integer spiderType;

    private static final long serialVersionUID = 1L;

    public NginxLog() {
    }

    public NginxLog(String ip, String localTime, String method, String url, String http, String status, String bytes,
                    String referer, String xforward, Double requestTime, String userAgent,Integer spiderType) {
        this.ip = ip;
        this.localTime = localTime;
        this.method = method;
        this.url = url;
        this.http = http;
        this.status = status;
        this.bytes = bytes;
        this.referer = referer;
        this.xforward = xforward;
        this.requestTime = requestTime;
        this.userAgent = userAgent;
        this.spiderType = spiderType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime == null ? null : localTime.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http == null ? null : http.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes == null ? null : bytes.trim();
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer == null ? null : referer.trim();
    }

    public String getXforward() {
        return xforward;
    }

    public void setXforward(String xforward) {
        this.xforward = xforward == null ? null : xforward.trim();
    }

    public Double getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Double requestTime) {
        this.requestTime = requestTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(Integer spiderType) {
        this.spiderType = spiderType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ip=").append(ip);
        sb.append(", localTime=").append(localTime);
        sb.append(", method=").append(method);
        sb.append(", url=").append(url);
        sb.append(", http=").append(http);
        sb.append(", status=").append(status);
        sb.append(", bytes=").append(bytes);
        sb.append(", referer=").append(referer);
        sb.append(", xforward=").append(xforward);
        sb.append(", requestTime=").append(requestTime);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", createTime=").append(createTime);
        sb.append(", spiderType=").append(spiderType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}