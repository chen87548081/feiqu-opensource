package com.feiqu.system.pojo.response;


import com.feiqu.system.model.GeneralComment;

import java.util.Date;
import java.util.List;

/**
 * @author cwd
 * @create 2017-10-10:59
 **/
public class DetailCommentResponse extends GeneralComment {

    public DetailCommentResponse() {
        this.setReplyList(null);
    }

    private String postNickname;
    private String postIcon;
    private Date utime;//评论人的加入时间
    private Integer qudouNum;//趣豆
    private Integer sex;//性别

    private List<DetailReplyResponse> replyList;

    public String getPostNickname() {
        return postNickname;
    }

    public void setPostNickname(String postNickname) {
        this.postNickname = postNickname;
    }

    public String getPostIcon() {
        return postIcon;
    }

    public void setPostIcon(String postIcon) {
        this.postIcon = postIcon;
    }

    public List<DetailReplyResponse> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<DetailReplyResponse> replyList) {
        this.replyList = replyList;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public Integer getQudouNum() {
        return qudouNum;
    }

    public void setQudouNum(Integer qudouNum) {
        this.qudouNum = qudouNum;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
