package com.feiqu.system.pojo.response;

import com.feiqu.system.model.CMessage;

/**
 * @author cwd
 * @create 2017-10-10:31
 **/
public class MessageUserDetail extends CMessage {
    private String postNickname;
    private String postIcon;
    private String receivedNickname;
    private String receivedIcon;

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

    public String getReceivedNickname() {
        return receivedNickname;
    }

    public void setReceivedNickname(String receivedNickname) {
        this.receivedNickname = receivedNickname;
    }

    public String getReceivedIcon() {
        return receivedIcon;
    }

    public void setReceivedIcon(String receivedIcon) {
        this.receivedIcon = receivedIcon;
    }
}
