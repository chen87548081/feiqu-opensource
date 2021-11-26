package com.feiqu.web.controller;

import cn.hutool.core.date.DateUtil;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.CommentsWithThoughtResponse;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author cwd
 * @create 2017-10-10:17
 **/
@Controller
@RequestMapping("comment")
public class CommentController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private GeneralLikeService likeService;
    @Autowired
    private GeneralReplyService replyService;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private CMessageService messageService;

    /*
   我的想法
    */
    @GetMapping(value = "/my")
    public String myThoughtsPage(HttpServletRequest request, HttpServletResponse response){
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                return USER_LOGIN_REDIRECT_URL;
            }
        } catch (Exception e) {
            logger.error("thought 获取我的回复失败！",e);
        }
        return "/comment/comments";
    }

    @PostMapping(value = "/my/{page}")
    @ResponseBody
    public BaseResult myThoughts(Integer userId, @PathVariable Integer page, @RequestParam(defaultValue = "1") Integer topicType){
        BaseResult baseResult = new BaseResult();
        try {
            PageHelper.startPage(page,20);
            if(TopicTypeEnum.THOUGHT_TYPE.getValue().equals(topicType)){
                List<CommentsWithThoughtResponse> comments = commentService.selectCommentsWithThought(userId);
                PageInfo pageInfo = new PageInfo(comments);
                baseResult.setData(pageInfo);
            }
        } catch (Exception e) {
            logger.error("thought 获取我的回复失败！",e);
            baseResult.setResult(ResultEnum.FAIL);
        }
        return baseResult;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer commentId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            GeneralComment comment = commentService.selectByPrimaryKey(commentId);
            if(comment == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(commentId).andTopicTypeEqualTo(TopicTypeEnum.COMMENT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(commentId, TopicTypeEnum.COMMENT_TYPE.getValue(),1,user.getId(),new Date(), YesNoEnum.NO.getValue());
            likeService.insert(like);

            comment.setLikeCount(comment.getLikeCount() == null? 1: comment.getLikeCount()+1);
            commentService.updateByPrimaryKey(comment);

        } catch (Exception e) {
            logger.error("comment like error",e);
            e.printStackTrace();
        }
        return result;
    }


    @PostMapping("reply")
    @ResponseBody
    public Object reply(GeneralReply reply, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            reply.setPostUserId(user.getId());
            GeneralComment comment = commentService.selectByPrimaryKey(reply.getCommentId());
            if(comment == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            comment.setHasReply(YesNoEnum.YES.getValue());
            commentService.updateByPrimaryKeySelective(comment);
            String content = reply.getContent();
            if(StringUtils.isBlank(content)){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(content.startsWith("@")){
                int split = content.indexOf(" ");
                if(split == -1){
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                }
                String aite = StringUtils.substring(content,0,split);
                String aiteNickname = aite.substring(1);
                aite = "<a href=\"javascript:;\" class=\"fly-aite\">"+aite+"</a>";
                FqUserExample example = new FqUserExample();

                example.createCriteria().andNicknameEqualTo(aiteNickname);
                FqUser aiteUser = fqUserService.selectFirstByExample(example);
                if(aiteUser != null){
                    if(!aiteUser.getId().equals(user.getId())){
                        String themeDes = "在<a class=\"c-fly-link\" href=\""+CommonConstant.DOMAIN_URL+ "/knowfriend/themeDetail/"+comment.getTopicId()+   "\">帖子</a>中";
                        CMessage message = new CMessage();
                        message.setPostUserId(-1);
                        message.setCreateTime(new Date());
                        message.setDelFlag(YesNoEnum.NO.getValue());
                        message.setReceivedUserId(aiteUser.getId());
                        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                        message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                themeDes+"回复了你的评论 "+ DateUtil.formatDateTime(new Date()));
                        messageService.insert(message);
                    }
                    reply.setContent(aite+ StringUtils.substring(content,split));
                }
            }
            reply.setCreateTime(new Date());
            if(null == reply.getType()){
                reply.setType(TopicTypeEnum.COMMENT_TYPE.getValue());
            }
            reply.setDelFlag(YesNoEnum.NO.getValue());
            replyService.insert(reply);
            result.setData(reply);
        } catch (Exception e) {
            logger.error("comment reply error",e);
        }
        return result;
    }

    @GetMapping("/list/{userId}")
    @ResponseBody
    public Object list(@PathVariable Integer userId, HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") Integer p){
        BaseResult result = new BaseResult();
        try {
            GeneralCommentExample example = new GeneralCommentExample();
            example.setOrderByClause("create_time desc");
            example.createCriteria().andPostUserIdEqualTo(userId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(p,20,true);
            List<GeneralComment> comments = commentService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(comments);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("comment reply error",e);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(Integer id, HttpServletRequest request, HttpServletResponse response){
        logger.info("news comment delete id:"+id);
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            GeneralComment comment = commentService.selectByPrimaryKey(id);
            if(comment != null){
                if(user.getId().equals(comment.getPostUserId())){
                    comment.setDelFlag(YesNoEnum.YES.getValue());
                    commentService.updateByPrimaryKey(comment);
                }else {
                    result.setResult(ResultEnum.DELETE_NOT_MY);
                }
            }else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("news comment delete error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }
}
