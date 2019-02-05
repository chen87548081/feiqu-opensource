package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqMusic;
import com.feiqu.system.model.FqMusicExample;
import com.feiqu.system.pojo.response.FqMusicResponse;

import java.util.List;

/**
* FqMusicService接口
* created by cwd on 2017/12/24.
*/
public interface FqMusicService extends BaseService<FqMusic, FqMusicExample> {

    List<FqMusicResponse> selectWithUserByExample(FqMusicExample example);
}