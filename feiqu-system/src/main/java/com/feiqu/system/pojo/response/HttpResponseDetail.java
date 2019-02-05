package com.feiqu.system.pojo.response;

/**
 * @author cwd
 * @version HttpResponseDetail, v 0.1 2019/1/16 cwd 1049766
 */
public class HttpResponseDetail {
    private String header;
    private String body;

    public HttpResponseDetail() {
    }

    public HttpResponseDetail(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
