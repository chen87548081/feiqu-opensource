package com.feiqu.system.pojo.response.baidu;

/**
 * @author cwd
 * @create 2017-10-13:54
 **/
public class BaiduMapResponse {
    private String address;
    private Integer status;
    private BaiduMapContent content;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BaiduMapContent getContent() {
        return content;
    }

    public void setContent(BaiduMapContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BaiduMapResponse{" +
                "address='" + address + '\'' +
                ", status=" + status +
                ", content=" + content +
                '}';
    }
}
