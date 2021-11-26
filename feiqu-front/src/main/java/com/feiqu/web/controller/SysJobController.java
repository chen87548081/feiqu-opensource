package com.feiqu.web.controller;


import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import com.feiqu.quartz.service.SysJobService;
import com.feiqu.quartz.util.ScheduleUtils;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * SysJobcontroller
 * Created by cwd on 2019/3/13.
 */
@Controller
@RequestMapping("/sysJob")
public class SysJobController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysJobController.class);

    @Resource
    private Scheduler scheduler;

    @Resource
    private SysJobService sysJobService;

    /**
     * 跳转到SysJob首页
     */
    @RequestMapping("")
    public String index() {
        return "/sysJob/index";
    }

    /**
     * 跳转到SysJob首页
     */
    @RequestMapping("manage")
    public String manage() {
        return "/sysJob/manage";
    }

    /**
     * 添加SysJob页面
     */
    @RequestMapping("/sysJob_add")
    public String sysJob_add() {
        return "/sysJob/add";
    }

    /**
     * ajax删除SysJob
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            sysJobService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新SysJob页面
     */
    @RequestMapping("/edit/{sysJobId}")
    public Object sysJobEdit(@PathVariable Long sysJobId, Model model) {
        SysJob sysJob = sysJobService.selectByPrimaryKey(sysJobId);
        model.addAttribute("sysJob", sysJob);
        return "/sysJob/edit";
    }

    /**
     * 任务调度立即执行一次
     */
    @PostMapping("/run")
    @ResponseBody
    public AjaxResult run(SysJob job) throws SchedulerException {
        sysJobService.run(job);
        return success();
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(String ids, String status)
    {
        try {
            logger.info("ids:{},status:{}",ids,status);
            sysJobService.changeStatus(ids,status);
        } catch (SchedulerException e) {
            logger.error("changeStatus",e);
            return error();
        }
        return success();
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) throws SchedulerException
    {
        sysJobService.deleteJobByIds(ids);
        return success();
    }

    /**
     * ajax更新SysJob
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(SysJob sysJob) {
        BaseResult result = new BaseResult();
        try {
            logger.info("入参：{}", JSON.toJSONString(sysJob));
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            boolean ok = checkCronExpressionIsValid(sysJob.getCronExpression());
            if(!ok){
                result.setResult(ResultEnum.SYSTEM_ERROR);
                result.setMessage("定时任务表达式不对");
                return result;
            }
            if (sysJob.getJobId() == null) {
                sysJob.setStatus("0");
                sysJob.setCreateBy(fqUserCache.getNickname());
                sysJob.setUpdateBy("");
                sysJob.setCreateTime(new Date());
                sysJob.setUpdateTime(new Date());
                sysJob.setMethodParams(StringUtils.trimToEmpty(sysJob.getMethodParams()));
                sysJob.setRemark(StringUtils.trimToEmpty(sysJob.getRemark()));
                sysJobService.insert(sysJob);
                ScheduleUtils.createScheduleJob(scheduler, sysJob);
            } else {
                sysJobService.updateByPrimaryKeySelective(sysJob);
                sysJob = sysJobService.selectByPrimaryKey(sysJob.getJobId());
                ScheduleUtils.updateScheduleJob(scheduler, sysJob);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }


    /**
     * 查询SysJob首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            SysJobExample example = new SysJobExample();
            example.setOrderByClause("create_time desc");
            List<SysJob> list = sysJobService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    private boolean checkCronExpressionIsValid(String cronExpression)
    {
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }
}