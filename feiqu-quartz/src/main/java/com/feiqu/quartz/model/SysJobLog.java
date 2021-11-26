package com.feiqu.quartz.model;

import java.io.Serializable;
import java.util.Date;

public class SysJobLog implements Serializable {
    /**
     * 任务日志ID
     *
     * @mbg.generated
     */
    private Integer jobLogId;

    /**
     * 任务名称
     *
     * @mbg.generated
     */
    private String jobName;

    /**
     * 任务组名
     *
     * @mbg.generated
     */
    private String jobGroup;

    /**
     * 任务方法
     *
     * @mbg.generated
     */
    private String methodName;

    /**
     * 方法参数
     *
     * @mbg.generated
     */
    private String methodParams;

    /**
     * 日志信息
     *
     * @mbg.generated
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 异常信息
     *
     * @mbg.generated
     */
    private String exceptionInfo;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(Integer jobLogId) {
        this.jobLogId = jobLogId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", jobLogId=").append(jobLogId);
        sb.append(", jobName=").append(jobName);
        sb.append(", jobGroup=").append(jobGroup);
        sb.append(", methodName=").append(methodName);
        sb.append(", methodParams=").append(methodParams);
        sb.append(", jobMessage=").append(jobMessage);
        sb.append(", status=").append(status);
        sb.append(", exceptionInfo=").append(exceptionInfo);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysJobLog other = (SysJobLog) that;
        return (this.getJobLogId() == null ? other.getJobLogId() == null : this.getJobLogId().equals(other.getJobLogId()))
            && (this.getJobName() == null ? other.getJobName() == null : this.getJobName().equals(other.getJobName()))
            && (this.getJobGroup() == null ? other.getJobGroup() == null : this.getJobGroup().equals(other.getJobGroup()))
            && (this.getMethodName() == null ? other.getMethodName() == null : this.getMethodName().equals(other.getMethodName()))
            && (this.getMethodParams() == null ? other.getMethodParams() == null : this.getMethodParams().equals(other.getMethodParams()))
            && (this.getJobMessage() == null ? other.getJobMessage() == null : this.getJobMessage().equals(other.getJobMessage()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getExceptionInfo() == null ? other.getExceptionInfo() == null : this.getExceptionInfo().equals(other.getExceptionInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getJobLogId() == null) ? 0 : getJobLogId().hashCode());
        result = prime * result + ((getJobName() == null) ? 0 : getJobName().hashCode());
        result = prime * result + ((getJobGroup() == null) ? 0 : getJobGroup().hashCode());
        result = prime * result + ((getMethodName() == null) ? 0 : getMethodName().hashCode());
        result = prime * result + ((getMethodParams() == null) ? 0 : getMethodParams().hashCode());
        result = prime * result + ((getJobMessage() == null) ? 0 : getJobMessage().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getExceptionInfo() == null) ? 0 : getExceptionInfo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}