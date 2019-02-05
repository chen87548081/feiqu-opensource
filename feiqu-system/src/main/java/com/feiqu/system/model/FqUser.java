package com.feiqu.system.model;


import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.system.pojo.ThirdPartyUser;

import java.io.Serializable;
import java.util.Date;

public class FqUser implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private Date createTime;

    private String nickname;

    private String icon;

    private String createIp;

    private String city;

    private Integer sex;

    private Integer isSingle;

    private Integer isMailBind;

    private String sign;

    private Integer qudouNum;

    private String birth;

    private String education;

    private String school;

    private Integer role;

    private Integer level;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public FqUser() {
    }

    public FqUser(ThirdPartyUser thirdUser) {
        this.setCreateTime(new Date());
        this.setIcon(thirdUser.getAvatarUrl());
        this.setNickname(thirdUser.getUserName());
        this.setSex(Integer.valueOf(thirdUser.getGender()));
        this.setUsername(thirdUser.getAccount());
        this.setCity(thirdUser.getLocation());
        this.setSign(thirdUser.getDescription());
        this.setBirth(thirdUser.getBirth());
        this.setLevel(1);
        this.setStatus(UserStatusEnum.NORMAL.getValue());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp == null ? null : createIp.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    public Integer getIsMailBind() {
        return isMailBind;
    }

    public void setIsMailBind(Integer isMailBind) {
        this.isMailBind = isMailBind;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public Integer getQudouNum() {
        return qudouNum;
    }

    public void setQudouNum(Integer qudouNum) {
        this.qudouNum = qudouNum;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=***");
        sb.append(", createTime=").append(createTime);
        sb.append(", nickname=").append(nickname);
        sb.append(", icon=").append(icon);
        sb.append(", createIp=").append(createIp);
        sb.append(", city=").append(city);
        sb.append(", sex=").append(sex);
        sb.append(", isSingle=").append(isSingle);
        sb.append(", isMailBind=").append(isMailBind);
        sb.append(", sign=").append(sign);
        sb.append(", qudouNum=").append(qudouNum);
        sb.append(", birth=").append(birth);
        sb.append(", education=").append(education);
        sb.append(", school=").append(school);
        sb.append(", role=").append(role);
        sb.append(", level=").append(level);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}