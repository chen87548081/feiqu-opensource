package com.feiqu.system.model;

import java.util.Date;

public class UploadImgRecord {
    private Integer id;

    private String picUrl;

    private String picMd5;

    private Date createTime;

    private String ip;

    private Integer userId;

    private Long picSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getPicMd5() {
        return picMd5;
    }

    public void setPicMd5(String picMd5) {
        this.picMd5 = picMd5 == null ? null : picMd5.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getPicSize() {
        return picSize;
    }

    public void setPicSize(Long picSize) {
        this.picSize = picSize;
    }

    public UploadImgRecord() {
    }

    public UploadImgRecord( String picUrl, String picMd5, Date createTime, String ip, Integer userId, Long picSize) {
        this.picUrl = picUrl;
        this.picMd5 = picMd5;
        this.createTime = createTime;
        this.ip = ip;
        this.userId = userId;
        this.picSize = picSize;
    }
}