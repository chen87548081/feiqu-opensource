package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqTheme;
import com.feiqu.system.model.FqThemeExample;
import com.feiqu.system.pojo.response.FqThemeUserResponse;

import java.util.List;

/**
* FqThemeService接口
* created by cwd on 2017/11/24.
*/
public interface FqThemeService extends BaseService<FqTheme, FqThemeExample> {

    List<FqThemeUserResponse> selectWithUserByExample(FqThemeExample themeExample);
}