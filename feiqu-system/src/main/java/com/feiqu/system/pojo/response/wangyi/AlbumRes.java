package com.feiqu.system.pojo.response.wangyi;

import java.util.Arrays;

/**
 * AlbumRes
 *
 * @author chenweidong
 * @date 2017/12/7
 */
public class AlbumRes {
    private String desc;
    private String createdate;
    private String cover;
    private String[] pics;
    private String setname;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public String getSetname() {
        return setname;
    }

    public void setSetname(String setname) {
        this.setname = setname;
    }

    @Override
    public String toString() {
        return "AlbumRes{" +
                "desc='" + desc + '\'' +
                ", createdate='" + createdate + '\'' +
                ", cover='" + cover + '\'' +
                ", pics=" + Arrays.toString(pics) +
                ", setname='" + setname + '\'' +
                '}';
    }
}
