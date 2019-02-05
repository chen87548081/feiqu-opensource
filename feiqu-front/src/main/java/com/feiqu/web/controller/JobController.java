package com.feiqu.web.controller;

import cn.hutool.core.util.ReUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.DetailReplyResponse;
import com.feiqu.system.pojo.response.JobTalkUserDetail;
import com.feiqu.system.service.FqUserService;
import com.feiqu.system.service.GeneralCommentService;
import com.feiqu.system.service.GeneralReplyService;
import com.feiqu.system.service.JobTalkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/28.
 */
@Controller
@RequestMapping("job")
public class JobController extends BaseController {

    private final static Logger _log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobTalkService jobTalkService;
    @Autowired
    private FqUserService userService;

    @Autowired
    private WebUtil webUtil;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private GeneralReplyService replyService;
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") Integer pageIndex,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        JobTalkExample example = new JobTalkExample();
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(pageIndex,pageSize);
        List<JobTalkUserDetail> jobTalkList = jobTalkService.selectWithUserByExample(example);
        PageInfo page = new PageInfo(jobTalkList);
//        Integer count = jobTalkService.countByExample(example);
        request.setAttribute("count",page.getTotal());
        String content = "";
        Pattern pattern = Pattern.compile("<img.*src=(.*?)[^>]*?>");
        List<String> imgUrlList = null;
        for(JobTalkUserDetail jobTalkUserDetail : jobTalkList){
            content = jobTalkUserDetail.getContent();
            int imgIndex = content.indexOf("<img");
            if(imgIndex >= 0){
                imgUrlList = ReUtil.getAllGroups(pattern,content);
                content = ReUtil.delAll(pattern,content);
                if(content.length() > 50){
                    content = StringUtils.substring(content,0,50) +"...<br>"+ StringUtils.join(imgUrlList.toArray());;
                }else {
                    content+= StringUtils.join(imgUrlList.toArray());
                }
            }else {
                if(content.length() > 50){
                    content = StringUtils.substring(content,0,50) +"...";
                }
            }
            jobTalkUserDetail.setContent(content);
        }
        request.setAttribute("jobTalkList",jobTalkList);
        request.setAttribute("pageIndex",pageIndex);
        return "/job/index.html";
    }

    @ResponseBody
    @PostMapping(value = "postTalk")
    public Object writeArticle(HttpServletRequest request, HttpServletResponse response, Model model, @RequestBody JobTalk jobTalk) {
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null || user.getId() == null){
            result.setResult(ResultEnum.USER_NOT_LOGIN);
            return result;
        }
        if(!user.getId().equals(jobTalk.getUserId())){
            result.setResult(ResultEnum.FAIL);
            return result;
        }
        jobTalk.setTitle(HtmlUtils.htmlEscape(jobTalk.getTitle()));
        jobTalk.setDelFlag(YesNoEnum.NO.getValue());
        jobTalk.setCreateTime(new Date());
        jobTalkService.insert(jobTalk);
        result.setResult(ResultEnum.SUCCESS);
        return result;
    }

    @GetMapping("detail/{talkId}")
    public String detail(@PathVariable Integer talkId, HttpServletRequest request, HttpServletResponse response){
        JobTalkExample example = new JobTalkExample();
        example.createCriteria().andIdEqualTo(talkId);
        JobTalk talk = jobTalkService.selectFirstByExampleWithBLOBs(example);
        if(talk != null ){
            request.setAttribute("talk",talk);
            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.createCriteria().andTopicIdEqualTo(talkId).andTopicTypeEqualTo(TopicTypeEnum.JOB_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            request.setAttribute("comments",comments);
            GeneralReplyExample replyExample = new GeneralReplyExample();
            for(DetailCommentResponse comment : comments){
                replyExample.clear();
                replyExample.createCriteria().andCommentIdEqualTo(comment.getId());
                List<DetailReplyResponse> replyList = replyService.selectWithUserByExample(replyExample);
                comment.setReplyList(replyList);
            }
        }
        FqUser louzhu = userService.selectByPrimaryKey(talk.getUserId());
        if(louzhu != null){
            request.setAttribute("louzhu",louzhu);
        }


        return "job/detail.html";
    }

    @ResponseBody
    @PostMapping(value = "comment")
    public Object articleReply(@RequestBody GeneralComment comment, Model model, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null || user.getId() == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(!user.getId().equals(comment.getPostUserId())){
                result.setResult(ResultEnum.USER_NOT_SAME);
                return result;
            }
            if(comment.getContent().length() >250){
                result.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                return result;
            }
            JobTalk jobTalk = jobTalkService.selectByPrimaryKey(comment.getTopicId());
            if(jobTalk == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            jobTalk.setCommentCount(jobTalk.getCommentCount() == null ? 1: jobTalk.getCommentCount() +1);
            jobTalkService.updateByPrimaryKeySelective(jobTalk);
            comment.setCreateTime(new Date());
            comment.setTopicType(TopicTypeEnum.JOB_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);
            result.setData(comment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }
}
