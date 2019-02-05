package com.feiqu.web.controller;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqUserActiveNum;
import com.feiqu.system.model.FqUserActiveNumExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqUserActiveNumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;




/**
* FqUserActiveNumcontroller
* Created by cwd on 2019/2/1.
*/
@Controller
@RequestMapping("/fqUserActiveNum")
public class FqUserActiveNumController extends BaseController {

private static Logger logger = LoggerFactory.getLogger(FqUserActiveNumController.class);

@Resource
private FqUserActiveNumService fqUserActiveNumService;

/**
* 跳转到FqUserActiveNum首页
*/
@RequestMapping("")
public String index() {
return "/fqUserActiveNum/index.html";
}

/**
* 添加FqUserActiveNum页面
*/
@RequestMapping("/fqUserActiveNum_add")
public String fqUserActiveNum_add() {
return "/fqUserActiveNum/add.html";
}

/**
* ajax删除FqUserActiveNum
*/
@ResponseBody
@RequestMapping("/delete")
public Object delete(@RequestParam Long id) {
BaseResult result = new BaseResult();
    try{
        fqUserActiveNumService.deleteByPrimaryKey(id);
    } catch (Exception e) {
        logger.error("error", e);
        result.setCode("1");
    }
return result;
}

/**
* 更新FqUserActiveNum页面
*/
@RequestMapping("/edit/{fqUserActiveNumId}")
public Object fqUserActiveNumEdit(@PathVariable Long fqUserActiveNumId, Model model) {
    FqUserActiveNum fqUserActiveNum = fqUserActiveNumService.selectByPrimaryKey(fqUserActiveNumId);
    model.addAttribute("fqUserActiveNum",fqUserActiveNum);
    return "/fqUserActiveNum/edit.html";
}

/**
* ajax更新FqUserActiveNum
*/
@ResponseBody
@PostMapping("/save")
public Object save(FqUserActiveNum fqUserActiveNum) {
BaseResult result = new BaseResult();
    try{
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
        result.setResult(ResultEnum.USER_NOT_LOGIN);
        return result;
        }
        if(fqUserActiveNum.getId() == null){
        fqUserActiveNumService.insert(fqUserActiveNum);
        }else{
        fqUserActiveNumService.updateByPrimaryKeySelective(fqUserActiveNum);
        }
    } catch (Exception e) {
    logger.error("error", e);
    result.setCode("1");
    }
    return result;
}


/**
* 查询FqUserActiveNum首页
*/
@RequestMapping("list")
@ResponseBody
public Object list(@RequestParam(defaultValue = "0") Integer index,
@RequestParam(defaultValue = "10") Integer size) {
    BaseResult result = new BaseResult();
    try{
        PageHelper.startPage(index, size);
        FqUserActiveNumExample example = new FqUserActiveNumExample();
        example.setOrderByClause("create_time desc");
        List<FqUserActiveNum> list = fqUserActiveNumService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
    } catch (Exception e) {
        logger.error("error", e);
        result.setCode("1");
    }
    return result;
}
}