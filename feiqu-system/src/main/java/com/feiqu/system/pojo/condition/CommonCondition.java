package com.feiqu.system.pojo.condition;

/**
 * Created by Administrator on 2019/2/24.
 */
public class CommonCondition {
    private String orderByClause;
    private Integer delFlag;
    private Boolean distinct;

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }
}
