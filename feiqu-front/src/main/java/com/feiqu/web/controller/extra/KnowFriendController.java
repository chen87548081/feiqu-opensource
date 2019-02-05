package com.feiqu.web.controller.extra;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.DetailReplyResponse;
import com.feiqu.system.pojo.response.FqThemeUserResponse;
import com.feiqu.system.service.*;
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
 * KnowFriendController
 *
 * @author chenweidong
 * @date 2017/11/23
 */
@RequestMapping("knowfriend")
@Controller
public class KnowFriendController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(KnowFriendController.class);

    @Autowired
    private FqUserService userService;
    @Autowired
    private FqLabelService labelService;
    @Autowired
    private FqThemeService themeService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private GeneralReplyService replyService;
    @Autowired
    private CMessageService messageService;

    @GetMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response, String order, String label){
        return page(request,response,0,order,label);
    }

    @GetMapping("/index/{page}")
    public String page(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer page, String order, String label){
        FqLabelExample labelExample = new FqLabelExample();
        labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.BBS_TYPE.getValue());
        List<FqLabel> labelList = labelService.selectByExample(labelExample);
        request.setAttribute("labelList",labelList);
        request.setAttribute("areas",CommonConstant.AREA_LIST);

        PageHelper.startPage(page,CommonConstant.DEAULT_PAGE_SIZE,true);
        FqThemeExample themeExample = new FqThemeExample();
        if("comment".equals(order)){
            themeExample.setOrderByClause("comment_count desc");
        }else {
            themeExample.setOrderByClause("create_time desc");
        }
        request.setAttribute("order",order);
        request.setAttribute("label",label);
        if(StringUtils.isBlank(label)){
            themeExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        }else {
            themeExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andLabelEqualTo(label);
        }
        List<FqThemeUserResponse> themes = themeService.selectWithUserByExample(themeExample);
        PageInfo pageInfo = new PageInfo(themes);

        String content = "";//<img.*src=(.*?)[^>]*?>
        Pattern pattern = Pattern.compile("img\\[([^\\[\\]]+)\\]");
        List<String> imgUrlList = null;
        for(FqTheme theme : themes){
            content = theme.getContent();
            int imgIndex = content.indexOf("img");
            if(imgIndex >= 0){
//                int count = ReUtil.findAllGroup0().count(pattern,content);
//                imgUrlList = ReUtil.findAll(pattern,content,0);
               /* Matcher matcher = pattern.matcher(content);
                while (matcher.find()){
                    logger.info(matcher.group());
                }*/
//                int firstimgend = content.indexOf("]")+1;
//                String imgUrl = StringUtils.substring(content,imgIndex,firstimgend);
                imgUrlList = ReUtil.findAll(pattern,content,0);
                content = ReUtil.delAll(pattern,content);
                if(content.length() > 50){
                    content = StringUtils.substring(content,0,50) +"...\n"+ StringUtils.join(imgUrlList.toArray());;
                }else {
                    content+= StringUtils.join(imgUrlList.toArray());
//                    content+=imgUrl;
                }
            }else {
                if(content.length() > 50){
                    content = StringUtils.substring(content,0,50) +"...";
                }
            }
            theme.setContent(content);
        }

        request.setAttribute("themes",themes);
        request.setAttribute("count",pageInfo.getTotal());
        request.setAttribute("page",page);
        request.setAttribute("pageSize",CommonConstant.DEAULT_PAGE_SIZE);
        return "/knowfriend/index.html";
    }

    @ResponseBody
    @PostMapping(value = "pubTheme")
    public Object postbbs(HttpServletRequest request, HttpServletResponse response, FqTheme theme) {
        BaseResult result = new BaseResult();
        Integer userId = 0;
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null || user.getId() == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            userId = user.getId();
            theme.setUserId(userId);
            if(StringUtils.isBlank(theme.getTitle())|| StringUtils.isBlank(theme.getLabel())
                    || StringUtils.isBlank(theme.getContent())|| StringUtils.isBlank(theme.getArea())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            theme.setTitle(HtmlUtils.htmlEscape(theme.getTitle()));
            theme.setCreateTime(new Date());
            theme.setDelFlag(YesNoEnum.NO.getValue());
            themeService.insert(theme);

            result.setResult(ResultEnum.SUCCESS);
        } catch (Exception e) {
            logger.error("postbbs error,userId:"+userId,e);
        }
        return result;
    }

    @GetMapping(value = "/themeDetail/{themeId}")
    public Object themeDetail(@PathVariable Integer themeId, HttpServletRequest request) {
       return themeDetailPage(themeId,0,request);
    }

    @GetMapping(value = "/themeDetail/{themeId}/page/{page}")
    public Object themeDetailPage(@PathVariable Integer themeId, @PathVariable Integer page, HttpServletRequest request) {
        try {
            FqTheme theme = themeService.selectByPrimaryKey(themeId);
            if(theme == null){
                return "/404.html";
            }
            request.setAttribute("theme",theme);
            request.setAttribute("louzhu",userService.selectByPrimaryKey(theme.getUserId()));
            theme.setSeeCount(theme.getSeeCount() == null?0:theme.getSeeCount()+1);
            themeService.updateByPrimaryKey(theme);
            request.setAttribute("page",page);

            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.createCriteria().andTopicIdEqualTo(themeId).andTopicTypeEqualTo(TopicTypeEnum.BBS_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE,true);
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            PageInfo pageInfo = new PageInfo(comments);
            request.setAttribute("comments",comments);
            request.setAttribute("count",pageInfo.getTotal());
            request.setAttribute("pageSize",CommonConstant.DEAULT_PAGE_SIZE);
            GeneralReplyExample replyExample = new GeneralReplyExample();
            for(DetailCommentResponse comment : comments){
                replyExample.clear();
                replyExample.createCriteria().andCommentIdEqualTo(comment.getId());
                List<DetailReplyResponse> replyList = replyService.selectWithUserByExample(replyExample);
                comment.setReplyList(replyList);
            }
        } catch (Exception e) {
            logger.error("themeDetail error themeId:"+themeId,e);
        }
        return "/knowfriend/themeDetail.html";
    }

    @ResponseBody
    @PostMapping(value = "/theme/comment")
    public Object comment(GeneralComment comment, Model model, HttpServletRequest request, HttpServletResponse response){
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
            FqTheme theme = themeService.selectByPrimaryKey(comment.getTopicId());
            if(theme == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            theme.setCommentCount(theme.getCommentCount() == null ? 1: theme.getCommentCount() +1);
            themeService.updateByPrimaryKeySelective(theme);
            comment.setCreateTime(new Date());
            comment.setTopicType(TopicTypeEnum.BBS_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);

            if(!comment.getPostUserId().equals(user.getId())){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(new Date());
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(theme.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                        "回复了<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/knowfriend/themeDetail/" + theme.getId() +"\" target=\"_blank\">你的帖子</a> "+ DateUtil.formatDateTime(new Date()));
                messageService.insert(message);
            }

            result.setData(comment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("/theme/delete/{themeId}")
    @ResponseBody
    public Object deleteTheme(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer themeId){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = webUtil.currentUser(request,response);
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqTheme theme = themeService.selectByPrimaryKey(themeId);
            if(theme == null|| theme.getDelFlag().equals(YesNoEnum.YES.getValue())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(!UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())){
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            FqTheme toUpdate = new FqTheme();
            toUpdate.setId(themeId);
            toUpdate.setDelFlag(YesNoEnum.YES.getValue());
            themeService.updateByPrimaryKeySelective(toUpdate);
            logger.info("帖子删除成功，帖子id :{},用户id：{} ",themeId,fqUserCache.getId());
        } catch (Exception e) {
            logger.error("删除帖子失败，帖子id : "+themeId,e);
        }
        return result;
    }

    @GetMapping("/theme/list/{userId}")
    @ResponseBody
    public Object themes(@PathVariable Integer userId){
         BaseResult result = new BaseResult();
         FqThemeExample example = new FqThemeExample();
         example.createCriteria().andUserIdEqualTo(userId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
         List<FqThemeUserResponse> themes = themeService.selectWithUserByExample(example);
         result.setData(themes);
         return result;
    }

    @PostMapping("/theme/applyToDelete/{themeId}")
    @ResponseBody
    public Object applyToDelete(@PathVariable Integer themeId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache fqUser = webUtil.currentUser(request,response);

        FqTheme theme = themeService.selectByPrimaryKey(themeId);
        if(!theme.getUserId().equals(fqUser.getId())){
            result.setResult(ResultEnum.DELETE_NOT_MY);
            return result;
        }

        FqUserExample example = new FqUserExample();
        example.createCriteria().andRoleEqualTo(UserRoleEnum.ADMIN_USER_ROLE.getValue());
        List<FqUser> officials = userService.selectByExample(example);

        for(FqUser user : officials){
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(new Date());
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(user.getId());
            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+fqUser.getId()+"/peopleIndex\" target=\"_blank\">"+fqUser.getNickname()+" </a> " +
                    "申请删除帖子，帖子id： "+theme.getId()+",帖子标题："+theme.getTitle()+",时间"+ DateUtil.formatDateTime(new Date()));
            messageService.insert(message);
        }
        return result;
    }


}
