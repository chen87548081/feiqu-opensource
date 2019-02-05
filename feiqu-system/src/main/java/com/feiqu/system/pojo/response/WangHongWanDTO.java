package com.feiqu.system.pojo.response;

import com.feiqu.system.model.WangHongWan;

import java.util.List;

/**
 * Created by Administrator on 2018/12/22.
 */
public class WangHongWanDTO extends WangHongWan {
    private List<String> imgs;

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
