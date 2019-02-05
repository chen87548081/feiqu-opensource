package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class ApiDocInterface implements Serializable {
    private Long id;

    /**
     * api链接
     *
     * @mbg.generated
     */
    private String url;

    /**
     *  请求方式
     *
     * @mbg.generated
     */
    private String method;

    /**
     * 是否可用;0不可用；1可用;-1 删除
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     * 所属模块ID
     *
     * @mbg.generated
     */
    private Long moduleid;

    /**
     * 接口名
     *
     * @mbg.generated
     */
    private String interfacename;

    private String updateby;

    private Date updatetime;

    private Date createtime;

    /**
     * 版本号
     *
     * @mbg.generated
     */
    private String version;

    /**
     * 排序，越大越靠前
     *
     * @mbg.generated
     */
    private Integer sequence;

    private String fullurl;

    /**
     * 是否是模板
     *
     * @mbg.generated
     */
    private Boolean istemplate;

    private Long projectid;

    /**
     * 接口返回contentType
     *
     * @mbg.generated
     */
    private String contenttype;

    /**
     * 参数列表
     *
     * @mbg.generated
     */
    private String param;

    /**
     * 请求参数备注
     *
     * @mbg.generated
     */
    private String paramremark;

    /**
     * 请求示例
     *
     * @mbg.generated
     */
    private String requestexam;

    /**
     * 返回参数说明
     *
     * @mbg.generated
     */
    private String responseparam;

    /**
     * 接口错误码列表
     *
     * @mbg.generated
     */
    private String errorlist;

    /**
     * 正确返回示例
     *
     * @mbg.generated
     */
    private String trueexam;

    /**
     * 错误返回示例
     *
     * @mbg.generated
     */
    private String falseexam;

    private String remark;

    /**
     * 错误码、错误码信息
     *
     * @mbg.generated
     */
    private String errors;

    private String header;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getModuleid() {
        return moduleid;
    }

    public void setModuleid(Long moduleid) {
        this.moduleid = moduleid;
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }

    public Boolean getIstemplate() {
        return istemplate;
    }

    public void setIstemplate(Boolean istemplate) {
        this.istemplate = istemplate;
    }

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParamremark() {
        return paramremark;
    }

    public void setParamremark(String paramremark) {
        this.paramremark = paramremark;
    }

    public String getRequestexam() {
        return requestexam;
    }

    public void setRequestexam(String requestexam) {
        this.requestexam = requestexam;
    }

    public String getResponseparam() {
        return responseparam;
    }

    public void setResponseparam(String responseparam) {
        this.responseparam = responseparam;
    }

    public String getErrorlist() {
        return errorlist;
    }

    public void setErrorlist(String errorlist) {
        this.errorlist = errorlist;
    }

    public String getTrueexam() {
        return trueexam;
    }

    public void setTrueexam(String trueexam) {
        this.trueexam = trueexam;
    }

    public String getFalseexam() {
        return falseexam;
    }

    public void setFalseexam(String falseexam) {
        this.falseexam = falseexam;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", url=").append(url);
        sb.append(", method=").append(method);
        sb.append(", status=").append(status);
        sb.append(", moduleid=").append(moduleid);
        sb.append(", interfacename=").append(interfacename);
        sb.append(", updateby=").append(updateby);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", createtime=").append(createtime);
        sb.append(", version=").append(version);
        sb.append(", sequence=").append(sequence);
        sb.append(", fullurl=").append(fullurl);
        sb.append(", istemplate=").append(istemplate);
        sb.append(", projectid=").append(projectid);
        sb.append(", contenttype=").append(contenttype);
        sb.append(", param=").append(param);
        sb.append(", paramremark=").append(paramremark);
        sb.append(", requestexam=").append(requestexam);
        sb.append(", responseparam=").append(responseparam);
        sb.append(", errorlist=").append(errorlist);
        sb.append(", trueexam=").append(trueexam);
        sb.append(", falseexam=").append(falseexam);
        sb.append(", remark=").append(remark);
        sb.append(", errors=").append(errors);
        sb.append(", header=").append(header);
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
        ApiDocInterface other = (ApiDocInterface) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getModuleid() == null ? other.getModuleid() == null : this.getModuleid().equals(other.getModuleid()))
            && (this.getInterfacename() == null ? other.getInterfacename() == null : this.getInterfacename().equals(other.getInterfacename()))
            && (this.getUpdateby() == null ? other.getUpdateby() == null : this.getUpdateby().equals(other.getUpdateby()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSequence() == null ? other.getSequence() == null : this.getSequence().equals(other.getSequence()))
            && (this.getFullurl() == null ? other.getFullurl() == null : this.getFullurl().equals(other.getFullurl()))
            && (this.getIstemplate() == null ? other.getIstemplate() == null : this.getIstemplate().equals(other.getIstemplate()))
            && (this.getProjectid() == null ? other.getProjectid() == null : this.getProjectid().equals(other.getProjectid()))
            && (this.getContenttype() == null ? other.getContenttype() == null : this.getContenttype().equals(other.getContenttype()))
            && (this.getParam() == null ? other.getParam() == null : this.getParam().equals(other.getParam()))
            && (this.getParamremark() == null ? other.getParamremark() == null : this.getParamremark().equals(other.getParamremark()))
            && (this.getRequestexam() == null ? other.getRequestexam() == null : this.getRequestexam().equals(other.getRequestexam()))
            && (this.getResponseparam() == null ? other.getResponseparam() == null : this.getResponseparam().equals(other.getResponseparam()))
            && (this.getErrorlist() == null ? other.getErrorlist() == null : this.getErrorlist().equals(other.getErrorlist()))
            && (this.getTrueexam() == null ? other.getTrueexam() == null : this.getTrueexam().equals(other.getTrueexam()))
            && (this.getFalseexam() == null ? other.getFalseexam() == null : this.getFalseexam().equals(other.getFalseexam()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getErrors() == null ? other.getErrors() == null : this.getErrors().equals(other.getErrors()))
            && (this.getHeader() == null ? other.getHeader() == null : this.getHeader().equals(other.getHeader()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getModuleid() == null) ? 0 : getModuleid().hashCode());
        result = prime * result + ((getInterfacename() == null) ? 0 : getInterfacename().hashCode());
        result = prime * result + ((getUpdateby() == null) ? 0 : getUpdateby().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSequence() == null) ? 0 : getSequence().hashCode());
        result = prime * result + ((getFullurl() == null) ? 0 : getFullurl().hashCode());
        result = prime * result + ((getIstemplate() == null) ? 0 : getIstemplate().hashCode());
        result = prime * result + ((getProjectid() == null) ? 0 : getProjectid().hashCode());
        result = prime * result + ((getContenttype() == null) ? 0 : getContenttype().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getParamremark() == null) ? 0 : getParamremark().hashCode());
        result = prime * result + ((getRequestexam() == null) ? 0 : getRequestexam().hashCode());
        result = prime * result + ((getResponseparam() == null) ? 0 : getResponseparam().hashCode());
        result = prime * result + ((getErrorlist() == null) ? 0 : getErrorlist().hashCode());
        result = prime * result + ((getTrueexam() == null) ? 0 : getTrueexam().hashCode());
        result = prime * result + ((getFalseexam() == null) ? 0 : getFalseexam().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getErrors() == null) ? 0 : getErrors().hashCode());
        result = prime * result + ((getHeader() == null) ? 0 : getHeader().hashCode());
        return result;
    }
}