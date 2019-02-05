package com.feiqu.common.enums;

/**
 * @author cwd
 * @version PayWayEnum, v 0.1 2019/2/2 cwd 1049766
 */
public enum  PayWayEnum {
    ALIPAY(1,"支付宝支付"),
    WECHAT_PAY(2,"微信支付"),;

    PayWayEnum(Integer payWay, String desc) {
        this.payWay = payWay;
        this.desc = desc;
    }

    private Integer payWay;
    private String desc;

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
