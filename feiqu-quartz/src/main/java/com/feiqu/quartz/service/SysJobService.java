package com.feiqu.quartz.service;

import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import com.feiqu.system.base.BaseService;
import org.quartz.SchedulerException;

/**
* SysJobService接口
* created by cwd on 2019/3/13.
*/
public interface SysJobService extends BaseService<SysJob, SysJobExample> {

    boolean checkCronExpressionIsValid(String cronExpression);

    void run(SysJob job) throws SchedulerException;

    /**
     * 批量删除调度信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public void deleteJobByIds(String ids) throws SchedulerException;

    void changeStatus(String ids, String status) throws SchedulerException;
}