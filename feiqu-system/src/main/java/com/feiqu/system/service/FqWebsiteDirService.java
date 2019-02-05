package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;

import java.util.List;

/**
* FqWebsiteDirService接口
* created by cwd on 2018/1/23.
*/
public interface FqWebsiteDirService extends BaseService<FqWebsiteDir, FqWebsiteDirExample> {

    List<String> selectTypes();
}