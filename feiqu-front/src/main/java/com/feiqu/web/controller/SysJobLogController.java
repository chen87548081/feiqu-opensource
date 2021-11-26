package com.feiqu.web.controller;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.quartz.model.SysJobLog;
import com.feiqu.quartz.model.SysJobLogExample;
import com.feiqu.quartz.service.SysJobLogService;
import com.feiqu.system.pojo.cache.FqUserCache;
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
 * SysJobLogcontroller
 * Created by cwd on 2019/3/13.
 */
@Controller
@RequestMapping("/sysJobLog")
public class SysJobLogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysJobLogController.class);

    @Resource
    private SysJobLogService sysJobLogService;

    /**
     * 跳转到SysJobLog首页
     */
    @RequestMapping("manage")
    public String manage() {
        return "/sysJobLog/manage";
    }

    /**
     * 跳转到SysJobLog首页
     */
    @RequestMapping("")
    public String index() {
        return "/sysJobLog/index";
    }

    /**
     * 添加SysJobLog页面
     */
    @RequestMapping("/sysJobLog_add")
    public String sysJobLog_add() {
        return "/sysJobLog/add";
    }

    /**
     * ajax删除SysJobLog
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            sysJobLogService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新SysJobLog页面
     */
    @RequestMapping("/edit/{sysJobLogId}")
    public Object sysJobLogEdit(@PathVariable Long sysJobLogId, Model model) {
        SysJobLog sysJobLog = sysJobLogService.selectByPrimaryKey(sysJobLogId);
        model.addAttribute("sysJobLog", sysJobLog);
        return "/sysJobLog/edit";
    }

    /**
     * ajax更新SysJobLog
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(SysJobLog sysJobLog) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (sysJobLog.getJobLogId() == null) {
                sysJobLogService.insert(sysJobLog);
            } else {
                sysJobLogService.updateByPrimaryKeySelective(sysJobLog);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询SysJobLog首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, limit);
            SysJobLogExample example = new SysJobLogExample();
            example.setOrderByClause("create_time desc");
            List<SysJobLog> list = sysJobLogService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}