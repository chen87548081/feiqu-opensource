/**
 * 第三方用户实体类
 */
package com.feiqu.system.pojo;

/**
 * @author 陈卫东
 * @version 2017年11月04日
 */
@SuppressWarnings("serial")
public class ThirdPartyUser {

	private String account;// 用户
	private String userName;// 用户昵称
	private String avatarUrl;// 用户头像地址
	private String gender;// 用户性别
	private String token;// 用户认证
	private String openid;// 用户第三方id
	private String provider;// 用户类型
	private Integer userId;// 用户id
	private String location;//位置
	private String description;//描述
	private String birth;//生日

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
}
