package com.feiqu.system.pojo.response;

/**
 * @author cwd
 * @version IpVisitDTO, v 0.1 2018/12/26 cwd 1049766
 */
public class IpVisitDTO {
    private String ip;
    private String visitNum;
    private String visitArea;

    public IpVisitDTO(String ip, String visitNum, String visitArea) {
        this.ip = ip;
        this.visitNum = visitNum;
        this.visitArea = visitArea;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }

    public String getVisitArea() {
        return visitArea;
    }

    public void setVisitArea(String visitArea) {
        this.visitArea = visitArea;
    }
}
