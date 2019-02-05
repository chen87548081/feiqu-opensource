package com.feiqu.system.model;

import com.feiqu.system.pojo.response.wangyi.NewsResponse;

import java.io.Serializable;
import java.util.Date;

public class FqNews implements Serializable {
    private Long id;

    private String title;

    private String source;

    private Integer commentCount;

    private String imgSrc;

    private String pTime;

    private Date gmtCreate;

    private String content;

    private static final long serialVersionUID = 1L;

    public FqNews() {
    }

    public FqNews(NewsResponse newsResponse) {
        this.setImgSrc(newsResponse.getImgsrc());
        this.setpTime(newsResponse.getPtime());
        this.setSource(newsResponse.getSource());
        this.setTitle(newsResponse.getTitle());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc == null ? null : imgSrc.trim();
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime == null ? null : pTime.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", source=").append(source);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", imgSrc=").append(imgSrc);
        sb.append(", pTime=").append(pTime);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}