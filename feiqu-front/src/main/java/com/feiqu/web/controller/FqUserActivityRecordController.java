package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.system.model.FqUserActivityRecord;
import com.feiqu.system.model.FqUserActivityRecordExample;
import com.feiqu.system.service.FqUserActivityRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


/**
* FqUserActivityRecordcontroller
* Created by cwd on 2019/1/10.
*/
@Controller
@RequestMapping("/fqUserActivityRecord")
public class FqUserActivityRecordController extends BaseController {

private static Logger logger = LoggerFactory.getLogger(FqUserActivityRecordController.class);

@Resource
private FqUserActivityRecordService fqUserActivityRecordService;

/**
* 跳转到FqUserActivityRecord首页
*/
@RequestMapping("")
public String index() {
return "/system/FqUserActivityRecord/index.html";
}

/**
* 添加FqUserActivityRecord页面
*/
@RequestMapping("/fqUserActivityRecord_add")
public String fqUserActivityRecord_add() {
return "/system/FqUserActivityRecord/add.html";
}

/**
* ajax添加FqUserActivityRecord
*/
@ResponseBody
@RequestMapping("/add")
public Object add(FqUserActivityRecord fqUserActivityRecord) {
BaseResult result = new BaseResult();
fqUserActivityRecordService.insert(fqUserActivityRecord);
return result;
}

/**
* ajax删除FqUserActivityRecord
*/
@ResponseBody
@RequestMapping("/delete")
public Object delete(@RequestParam Integer fqUserActivityRecordId) {
BaseResult result = new BaseResult();
fqUserActivityRecordService.deleteByPrimaryKey(fqUserActivityRecordId);
return result;
}

/**
* 更新FqUserActivityRecord页面
*/
@RequestMapping("/edit/{fqUserActivityRecordId}")
public Object fqUserActivityRecordEdit(@PathVariable Integer fqUserActivityRecordId, Model model) {
FqUserActivityRecord fqUserActivityRecord = fqUserActivityRecordService.selectByPrimaryKey(fqUserActivityRecordId);
model.addAttribute("fqUserActivityRecord",fqUserActivityRecord);
return "/system/FqUserActivityRecord/edit.html";
}

/**
* ajax更新FqUserActivityRecord
*/
@ResponseBody
@RequestMapping("/edit")
public Object edit(FqUserActivityRecord fqUserActivityRecord) {
BaseResult result = new BaseResult();
fqUserActivityRecordService.updateByPrimaryKey(fqUserActivityRecord);
return result;
}


/**
* 查询FqUserActivityRecord首页
*/
@RequestMapping("list")
@ResponseBody
public Object list(@RequestParam(defaultValue = "0") Integer index,
@RequestParam(defaultValue = "10") Integer size) {
BaseResult result = new BaseResult();
PageHelper.startPage(index, size);
FqUserActivityRecordExample example = new FqUserActivityRecordExample();
example.setOrderByClause("create_time desc");
List<FqUserActivityRecord> list = fqUserActivityRecordService.selectByExample(example);
PageInfo page = new PageInfo(list);
result.setData(page);
return result;
}
}