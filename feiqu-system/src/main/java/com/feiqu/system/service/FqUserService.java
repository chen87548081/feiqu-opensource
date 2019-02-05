package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.ThirdPartyUser;

/**
* FqUserService接口
* created by cwd on 2017/11/7.
*/
public interface FqUserService extends BaseService<FqUser, FqUserExample> {

    int insertThirdPartyUser(FqUser fqUser, ThirdPartyUser thirdUser);
}