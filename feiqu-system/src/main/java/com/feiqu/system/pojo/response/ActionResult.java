package com.feiqu.system.pojo.response;


import com.feiqu.common.base.BaseResult;

/**
 * Created by Administrator on 2017/10/30.
 */
public class ActionResult extends BaseResult {
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionResult() {
    }

    public ActionResult(String code, String message, Object data) {
        super(code, message, data);
    }
}
