package com.feiqu.web.controller;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ActiveNumEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqShortVideo;
import com.feiqu.system.model.FqShortVideoExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqShortVideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * FqShortVideocontroller
 * Created by cwd on 2018/10/28.
 */
@Controller
@RequestMapping("/shortVideo")
public class FqShortVideoController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqShortVideoController.class);

    @Autowired
    private FqShortVideoService fqShortVideoService;

    /**
     * ajax添加FqShortVideo
     */
    @ResponseBody
    @PostMapping("/add")
    public Object add(FqShortVideo fqShortVideo) {
        logger.info("视频上传"+JSON.toJSONString(fqShortVideo));
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(!Validator.isUrl(fqShortVideo.getUrl())){
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            if(StringUtils.isBlank(fqShortVideo.getTitle())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            fqShortVideo.setDelFlag(YesNoEnum.NO.getValue());
            fqShortVideo.setLikeCount(0);
            fqShortVideo.setCreateTime(new Date());
            fqShortVideo.setUserId(fqUserCache.getId());
            fqShortVideoService.insert(fqShortVideo);
            CommonUtils.addActiveNum(fqUserCache.getId(), ActiveNumEnum.VIDEO_UPLOAD.getValue());
        } catch (Exception e) {
            logger.error("视频上传失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
     * ajax删除FqShortVideo
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer fqShortVideoId) {
        BaseResult result = new BaseResult();
        fqShortVideoService.deleteByPrimaryKey(fqShortVideoId);
        return result;
    }

    /**
     * 更新FqShortVideo页面
     */
    @RequestMapping("/edit/{fqShortVideoId}")
    public Object fqShortVideoEdit(@PathVariable Integer fqShortVideoId, Model model) {
        FqShortVideo fqShortVideo = fqShortVideoService.selectByPrimaryKey(fqShortVideoId);
        model.addAttribute("fqShortVideo", fqShortVideo);
        return "/system/FqShortVideo/edit.html";
    }

    /**
     * ajax更新FqShortVideo
     */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqShortVideo fqShortVideo) {
        BaseResult result = new BaseResult();
        fqShortVideoService.updateByPrimaryKey(fqShortVideo);
        return result;
    }


    /**
     * 查询FqShortVideo首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqShortVideoExample example = new FqShortVideoExample();
        example.setOrderByClause("create_time desc");
        List<FqShortVideo> list = fqShortVideoService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }
}