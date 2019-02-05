package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.*;
import com.feiqu.system.service.FqLabelService;
import com.feiqu.system.service.FqTopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqTopiccontroller
 * Created by cwd on 2018/11/29.
 */
@Controller
@RequestMapping("/fqTopic")
public class FqTopicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqTopicController.class);

    @Resource
    private FqTopicService fqTopicService;
    @Resource
    private FqLabelService fqLabelService;



    /**
     * 跳转到FqTopic首页
     */
    @RequestMapping("")
    public String index(@RequestParam(defaultValue = "desc") String order, Model model,
                        @RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(required = false) String type) {
        PageHelper.startPage(pageIndex,20);
        FqTopicExample fqTopicExample = new FqTopicExample();
        fqTopicExample.setOrderByClause("gmt_create "+order);
        if(StringUtils.isNoneEmpty(type)){
            fqTopicExample.createCriteria().andTypeEqualTo(type);
        }
        List<FqTopic> fqTopics = fqTopicService.selectByExample(fqTopicExample);
        model.addAttribute("fqTopics",fqTopics);
        PageInfo page = new PageInfo(fqTopics);
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",20);
        model.addAttribute("count",page.getTotal());
        FqLabelExample fqLabelExample = new FqLabelExample();
        fqLabelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.TOPIC_TYPE.getValue());
        List<FqLabel> fqLabels = fqLabelService.selectByExample(fqLabelExample);
        model.addAttribute("labels",fqLabels);
        model.addAttribute("type",type);
        return "/fqTopic/index.html";
    }

    /**
     * 添加FqTopic页面
     */
    @RequestMapping("/fqTopic_add")
    public String fqTopic_add() {
        return "/system/FqTopic/add.html";
    }

    /**
     * ajax添加FqTopic
     */
    @ResponseBody
    @RequestMapping("/add")
    public Object add(FqTopic fqTopic) {
        BaseResult result = new BaseResult();
        fqTopicService.insert(fqTopic);
        return result;
    }

    /**
     * ajax删除FqTopic
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer fqTopicId) {
        BaseResult result = new BaseResult();
        fqTopicService.deleteByPrimaryKey(fqTopicId);
        return result;
    }

    /**
     * 更新FqTopic页面
     */
    @RequestMapping("/detail/{fqTopicId}")
    public String fqTopicEdit(@PathVariable Long fqTopicId, Model model) {
        try {
            FqTopic fqTopic = fqTopicService.selectByPrimaryKey(fqTopicId);
            if(fqTopic == null){
                return GENERAL_NOT_FOUNF_404_URL;
            }
            model.addAttribute("fqTopic", fqTopic);
            List<FqTopicReply> replies = fqTopicService.listReplies(fqTopicId);
            if(replies == null){
                replies = Lists.newArrayList();
            }
            model.addAttribute("replies",replies);
            PageHelper.startPage(1,10,false);
            FqTopicExample fqTopicExample = new FqTopicExample();
            fqTopicExample.createCriteria().andTypeEqualTo(fqTopic.getType());
            fqTopicExample.setOrderByClause("GMT_CREATE desc");
            List<FqTopic> fqTopics = fqTopicService.selectByExample(fqTopicExample);
            model.addAttribute("sameSource",fqTopics);
        } catch (Exception e) {
            logger.error("话题详情页",e);
            return GENERAL_ERROR_URL;
        }
        return "/fqTopic/detail.html";
    }

    /**
     * ajax更新FqTopic
     */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqTopic fqTopic) {
        BaseResult result = new BaseResult();
        fqTopicService.updateByPrimaryKey(fqTopic);
        return result;
    }

    /**
     * ajax更新FqTopic
     */
    @ResponseBody
    @RequestMapping("/comment")
    public Object comment(FqTopicReply fqTopicReply) {
        BaseResult result = new BaseResult();
        if(StringUtils.isBlank(fqTopicReply.getContent())){
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }
        fqTopicReply.setGmtCreate(new Date());
        fqTopicService.insertFqTopicReply(fqTopicReply);
        return result;
    }


    /**
     * 查询FqTopic首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqTopicExample example = new FqTopicExample();
        example.setOrderByClause("create_time desc");
        List<FqTopic> list = fqTopicService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }
}