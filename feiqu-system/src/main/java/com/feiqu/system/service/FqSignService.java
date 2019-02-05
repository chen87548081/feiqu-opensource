package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqSign;
import com.feiqu.system.model.FqSignExample;
import com.feiqu.system.pojo.response.SignUserResponse;

import java.util.List;

/**
* FqSignService接口
* created by cwd on 2017/11/17.
*/
public interface FqSignService extends BaseService<FqSign, FqSignExample> {

    List<SignUserResponse> selectWithUserByExample(FqSignExample example);
}