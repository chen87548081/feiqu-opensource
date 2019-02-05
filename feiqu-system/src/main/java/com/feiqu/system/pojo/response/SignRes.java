package com.feiqu.system.pojo.response;

import cn.hutool.core.date.DateUtil;
import com.feiqu.system.model.FqSign;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * SignRes
 *
 * @author chenweidong
 * @date 2017/11/17
 */
public class SignRes {
    private Integer days;

    private Integer userId;

    private Date signTime;

    private Boolean signed;

    private Integer experience;

    private Integer forgetDays;

    private String[] signDays;

    public SignRes() {
    }

    public SignRes(FqSign sign) {
        this.setDays(sign.getDays());
        this.setSignTime(sign.getSignTime());
        this.setUserId(sign.getUserId());

    }

    public SignRes(FqSign sign,Date now) {
        this.setDays(sign.getDays());
        this.setSignTime(sign.getSignTime());
        this.setUserId(sign.getUserId());
        String[] signDays = StringUtils.split(sign.getSignDays(),",");
        if(StringUtils.isBlank(sign.getSignDays())){
            this.setForgetDays(DateUtil.dayOfMonth(now));
        }else {
            this.setForgetDays(DateUtil.dayOfMonth(now) - signDays.length);
        }
        this.setSignDays(signDays);
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getForgetDays() {
        return forgetDays;
    }

    public void setForgetDays(Integer forgetDays) {
        this.forgetDays = forgetDays;
    }

    public String[] getSignDays() {
        return signDays;
    }

    public void setSignDays(String[] signDays) {
        this.signDays = signDays;
    }
}
