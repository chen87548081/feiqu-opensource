package com.feiqu.common.base;


import com.feiqu.common.enums.ResultEnum;

/**
 * 统一返回结果类
 * Created by cwd on 2017/2/18.
 */
public class BaseResult {

    // 状态码：1成功，其他为失败
    public String code;

    // 成功为success，其他为失败原因
    public String message;

    // 数据结果集
    public Object data;

    public void setResult(ResultEnum result){
        this.setCode(result.getCode());
        this.setMessage(result.getMsg());
    }

    public BaseResult() {
        this.setResult(ResultEnum.SUCCESS);
    }

    public BaseResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
