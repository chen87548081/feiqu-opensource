package com.feiqu.system.pojo.response;

import com.feiqu.system.model.FqVisitRecord;

/**
 * Created by Administrator on 2017/12/17.
 */
public class FqVisitRecordResponse extends FqVisitRecord {
    private String visitorName;
    private String visitorIcon;
    private String visitedUserName;
    private String visitedUserIcon;

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorIcon() {
        return visitorIcon;
    }

    public void setVisitorIcon(String visitorIcon) {
        this.visitorIcon = visitorIcon;
    }

    public String getVisitedUserName() {
        return visitedUserName;
    }

    public void setVisitedUserName(String visitedUserName) {
        this.visitedUserName = visitedUserName;
    }

    public String getVisitedUserIcon() {
        return visitedUserIcon;
    }

    public void setVisitedUserIcon(String visitedUserIcon) {
        this.visitedUserIcon = visitedUserIcon;
    }
}
