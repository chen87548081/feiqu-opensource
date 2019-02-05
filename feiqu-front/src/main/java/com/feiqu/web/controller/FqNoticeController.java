package com.feiqu.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqNoticeService;
import com.feiqu.system.service.FqUserService;
import com.feiqu.system.service.GeneralCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
* FqNoticecontroller
* Created by cwd on 2017/11/9.
*/
@Controller
@RequestMapping("/notice")
public class FqNoticeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqNoticeController.class);

    @Autowired
    private FqNoticeService fqNoticeService;
    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private CMessageService messageService;

    /**
    * 跳转到FqNotice首页
    */
    @RequestMapping("")
    public String index() {
        return "/FqNotice/index.html";
    }

    /**
    * 添加FqNotice页面
    */
    @RequestMapping("/fqNotice_add")
    public String fqNotice_add() {
        return "/FqNotice/add.html";
    }

    /**
    * ajax添加FqNotice
    */
    @ResponseBody
    @PostMapping("/add")
    public Object add(FqNotice fqNotice, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            if(!fqUser.getRole().equals(UserRoleEnum.ADMIN_USER_ROLE.getValue())){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            fqNotice.setUserId(fqUser.getId());
            fqNotice.setNickname(fqUser.getNickname());
            fqNotice.setCreateTime(new Date());
            fqNotice.setIcon(fqUser.getIcon());
            fqNoticeService.insert(fqNotice);
            FqNoticeExample noticeExample = new FqNoticeExample();
            noticeExample.createCriteria().andIdNotEqualTo(fqNotice.getId()).andIsShowEqualTo(YesNoEnum.YES.getValue());
            List<FqNotice> notices = fqNoticeService.selectByExample(noticeExample);
            for(FqNotice fqNotice1 : notices){
                fqNotice1.setFqOrder(fqNotice1.getFqOrder()+1);
                fqNoticeService.updateByPrimaryKey(fqNotice1);
            }
            //更新缓存
            FqNoticeExample fqNoticeExample = new FqNoticeExample();
            fqNoticeExample.setOrderByClause("fq_order asc");
            fqNoticeExample.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
            List<FqNotice> list = fqNoticeService.selectByExample(fqNoticeExample);
            if(CollectionUtil.isNotEmpty(list)){
                CommonConstant.FQ_NOTICE_LIST = list;
            }
        } catch (Exception e) {
            logger.error("notice add error",e);
        }
        return result;
    }

    /**
    * ajax删除FqNotice
    */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer fqNoticeId) {
        BaseResult result = new BaseResult();
        fqNoticeService.deleteByPrimaryKey(fqNoticeId);
        return result;
    }

    /**
     * 查看FqNotice页面
     */
    @RequestMapping("/view/{fqNoticeId}")
    public Object fqNoticeView(@PathVariable Integer fqNoticeId, Model model) {

        FqNotice fqNotice = fqNoticeService.selectByPrimaryKey(fqNoticeId);
        if(fqNotice != null){
            FqUser fqUser = fqUserService.selectByPrimaryKey(fqNotice.getUserId());
            model.addAttribute("oUser",fqUser);
        }else {
            return "/404.html";
        }
        model.addAttribute("notice",fqNotice);
        FqNoticeExample example = new FqNoticeExample();
        example.createCriteria().andIsShowEqualTo(YesNoEnum.NO.getValue());
        example.setOrderByClause("fq_order desc");
        List<FqNotice> oldNotices = fqNoticeService.selectByExample(example);
        model.addAttribute("oldNotices",oldNotices);
        GeneralCommentExample commentExample = new GeneralCommentExample();
        commentExample.setOrderByClause("create_time");
        commentExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                .andTopicIdEqualTo(fqNoticeId).andTopicTypeEqualTo(TopicTypeEnum.NOTICE_TYPE.getValue());
        List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
        model.addAttribute("commentList",comments);
        return "/FqNotice/detail.html";
    }

    @ResponseBody
    @PostMapping(value = "comment")
    public Object articleReply(GeneralComment comment, Model model, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            FqNotice notice = fqNoticeService.selectByPrimaryKey(comment.getTopicId());
            if(notice == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            notice.setCommentNum(notice.getCommentNum() == null?1:notice.getCommentNum()+1);
            FqNotice toUpdate = new FqNotice();
            toUpdate.setId(notice.getId());
            toUpdate.setCommentNum(notice.getCommentNum());
            fqNoticeService.updateByPrimaryKeySelective(toUpdate);

            String commentContent= comment.getContent();
            if(org.apache.commons.lang3.StringUtils.isBlank(commentContent)){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if(aiteNames.size() > 0){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if (aiteUser != null && !aiteUser.getId().equals(user.getId())) {
                        CMessage message = new CMessage();
                        message.setPostUserId(-1);
                        message.setCreateTime(new Date());
                        message.setDelFlag(YesNoEnum.NO.getValue());
                        message.setReceivedUserId(aiteUser.getId());
                        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                        message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() + "/peopleIndex\" target=\"_blank\">" + user.getNickname() + " </a> " +
                                "在此<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/notice/view/" + notice.getId() + "\" target=\"_blank\">通知</a>中回复了你 " + DateUtil.formatDateTime(new Date()));
                        messageService.insert(message);
                    }
                }
            }
            comment.setCreateTime(new Date());
            comment.setTopicType(TopicTypeEnum.NOTICE_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            comment.setPostUserId(user.getId());
            commentService.insert(comment);
            result.setData(comment);
        } catch (Exception e) {
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
    * 更新FqNotice页面
    */
    @RequestMapping("/edit/{fqNoticeId}")
    public Object fqNoticeEdit(@PathVariable Integer fqNoticeId, Model model) {
        FqNotice fqNotice = fqNoticeService.selectByPrimaryKey(fqNoticeId);
        model.addAttribute("fqNotice",fqNotice);
        return "/FqNotice/edit.html";
    }

    /**
    * ajax更新FqNotice
    */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqNotice fqNotice) {
        BaseResult result = new BaseResult();
        fqNoticeService.updateByPrimaryKey(fqNotice);
        return result;
    }


    /**
    * 查询FqNotice首页
    */
    @RequestMapping("list")
    @ResponseBody
    public Object list() {
        BaseResult result = new BaseResult();
        /*FqNoticeExample example = new FqNoticeExample();
        example.setOrderByClause("fq_order desc");
        example.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
        List<FqNotice> list = fqNoticeService.selectByExample(example);*/
        result.setData(CommonConstant.FQ_NOTICE_LIST);
        return result;
    }
}