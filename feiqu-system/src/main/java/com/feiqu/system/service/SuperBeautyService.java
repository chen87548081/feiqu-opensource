package com.feiqu.system.service;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.SuperBeautyExample;
import com.feiqu.system.pojo.response.BeautyUserResponse;

import java.util.List;

/**
* SuperBeautyService接口
* created by cwd on 2017/10/16.
*/
public interface SuperBeautyService extends BaseService<SuperBeauty, SuperBeautyExample> {

    List<BeautyUserResponse> selectDetailByExample(SuperBeautyExample example);

    List<BeautyUserResponse> selectDetailById(Integer beautyId);
}