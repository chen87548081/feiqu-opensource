package com.feiqu.system.pojo.response.wangyi;

/**
 * @author cwd
 * @version NewsResponse, v 0.1 2018/11/13 cwd 1049766
 */
public class NewsResponse {
    private Integer hasImg;
    private String digest;
    private String url;
    private Long commentCount;
    private String title;
    private String source;
    private String imgsrc;
    private String ptime;

    public Integer getHasImg() {
        return hasImg;
    }

    public void setHasImg(Integer hasImg) {
        this.hasImg = hasImg;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
}
