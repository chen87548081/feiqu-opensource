package com.feiqu.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.ActionResult;
import com.feiqu.system.pojo.response.ArticleUserDetail;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.raiyi.AllDataRes;
import com.feiqu.system.pojo.response.raiyi.SingleData;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jeesuite.cache.command.RedisString;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/15.
 */
@Controller
@RequestMapping("article")
public class ArticleController extends BaseController {

    private final static Logger _log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private FqUserService userService;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private GeneralLikeService likeService;
    @Autowired
    private CMessageService messageService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqLabelService fqLabelService;
    @Autowired
    private FqCollectService fqCollectService;
    @Autowired
    private FqUserService fqUserService;

   /* @ResponseBody
    @RequestMapping("/generate")
    public Object generate() {
        BaseResult result = new BaseResult();
        File file = new File("D:\\webmagic\\qdfuns.com");
        File[] files =  file.listFiles();
        Date now = new Date();
        int index = 1;
        for(File file1 : files){
            String all = FileUtil.readString(file1, "UTF-8");
            JSONObject jsonObject = new JSONObject(all);
            if (jsonObject.isEmpty()) {
                continue;
            }
            String title = jsonObject.getStr("title");
            JSONArray authors = jsonObject.getJSONArray("author");
            String category = authors.get(2).toString();
            title = title +"-"+category;
            JSONArray commentCounts = jsonObject.getJSONArray("commentCount");
            String seeCount = commentCounts.get(0).toString();
            seeCount = seeCount.replace("阅读","");
            Integer seeCounti = Integer.valueOf(seeCount);
            JSONObject contentJson = jsonObject.getJSONObject("content");


            String content = contentJson.getStr("firstSourceText");
            Article article = new Article();
            article.setDelFlag(YesNoEnum.NO.getValue());
            article.setArticleTitle(title);
            article.setBrowseCount(seeCounti);
            article.setAnonymousSwitch(0);
            article.setCommentCount(0);
            article.setIsRecommend(0);
//                article.setLabel(17);
            article.setLabel(5);
//                article.setUserId(22);
            article.setUserId(8);
            article.setLikeCount(0);
           *//* int index1 = content.indexOf("id=\"comment\"");
            if(index1 != -1){
                content = content.substring(0,index1-3)+"</dd>";
            }
            content = ReUtil.delAll(pattern,content);*//*
//            article.setArticleContent(HtmlUtils.htmlUnescape(content));
            article.setArticleContent(content);
            article.setCreateTime(now);
            article.setContentType(2);
            articleService.insert(article);
            if(index > 20){
                break;
            }
            index++;
        }

        return result;
    }*/

    @PostMapping(value = "/clickCount/{id}")
    @ResponseBody
    public BaseResult clickCount(@PathVariable Integer id) {
        Article article = articleService.selectByPrimaryKey(id);
        if(article != null){
            Article toUpdate = new Article();
            toUpdate.setBrowseCount(article.getBrowseCount() == null ? 1:article.getBrowseCount()+1);
            toUpdate.setId(id);
            articleService.updateByPrimaryKeySelective(toUpdate);
        }
        return new BaseResult();
    }

    @GetMapping(value = "goWriteArticle")
    public String goWriteArticle(Model model) {
        FqLabelExample example = new FqLabelExample();
        example.createCriteria().andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
        List<FqLabel> labels = fqLabelService.selectByExample(example);
        model.addAttribute("labels",labels);
        return "/article/writeArticle.html";
    }

    @ResponseBody
    @PostMapping(value = "writeArticle")
    public Object writeArticle(HttpServletRequest request, HttpServletResponse response, Article article) {
        ActionResult result = new ActionResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            _log.info("用户：{} 写了一篇文章：{}",user.getId(),article.getArticleTitle());
            if (UserStatusEnum.FROZEN.getValue().equals(user.getStatus())) {
                result.setResult(ResultEnum.USER_FROZEN);
                return result;
            }
            if(article.getLabel() == null){
                result.setResult(ResultEnum.LABEL_NULL);
                return result;
            }
            if(StringUtils.isBlank(article.getArticleTitle()) || StringUtils.isBlank(article.getArticleContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(article.getArticleContent().length() < 50){
                result.setResult(ResultEnum.PARAM_ERROR);
                result.setMessage("文章长度不能小于50，如若请去想法处发表");
                return result;
            }
            String ip = WebUtil.getIP(request);
            String key = "writeArticle_"+article.getId()+"_"+user.getId()+"_"+ip;
            RedisString redisString = new RedisString(key);
            String value = redisString.get();
            if(StringUtils.isEmpty(value)){
                redisString.set("1", 60);
            }else {
                result.setResult(ResultEnum.POST_ARTICLE_FREQUENCY_OVER_LIMIT);
                return result;
            }
            article.setUserId(user.getId());
            article.setDelFlag(-1);//默认 审核不通过
            article.setCreateTime(new Date());
            article.setArticleContent(EmojiUtils.toUnicode(article.getArticleContent()));
            article.setContentType(2);
            articleService.insert(article);
            result.setResult(ResultEnum.SUCCESS);
        } catch (Exception e) {
            _log.error("writeArticle error", e);
            result.setCode("1");
        }
        return result;
    }

    @GetMapping("/edit/{articleId}")
    public String edit(@PathVariable Integer articleId, Model model){
        try {
            FqUserCache user = getCurrentUser();
            if(user == null){
                return USER_LOGIN_REDIRECT_URL;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article == null){
                return GENERAL_NOT_FOUNF_404_URL;
            }
            if(!user.getId().equals(article.getUserId())){
                return "/unauthed.html";
            }
            if(article.getContentType() == 2){
                article.setArticleContent(HtmlUtils.htmlUnescape(article.getArticleContent()));
            }
            model.addAttribute("article",article);
            FqLabelExample example = new FqLabelExample();
            example.createCriteria().andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
            List<FqLabel> labels = fqLabelService.selectByExample(example);
            model.addAttribute("labels",labels);
        } catch (Exception e) {
            _log.error("article edit error",e);
        }
        return "/article/edit.html";
    }

    //编辑操作
    @PostMapping("/doEdit")
    @ResponseBody
    public Object doEdit(Article article, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            _log.info("用户：{} 编辑了一篇文章：{}",fqUser.getId(),article.getArticleTitle());
            if(UserStatusEnum.FROZEN.getValue().equals(fqUser.getStatus())){
                result.setResult(ResultEnum.USER_FROZEN);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(article.getId());
            if(articleDB == null){
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            if(!articleDB.getUserId().equals(fqUser.getId())){
                result.setResult(ResultEnum.USER_NOT_SAME);
                return result;
            }
            if(article.getLabel() == null){
                result.setResult(ResultEnum.LABEL_NULL);
                return result;
            }
            if(StringUtils.isBlank(article.getArticleTitle()) || StringUtils.isBlank(article.getArticleContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            articleDB.setArticleTitle(article.getArticleTitle());
            articleDB.setArticleContent(article.getArticleContent());
            articleDB.setLabel(article.getLabel());
            articleService.updateByPrimaryKeyWithBLOBs(articleDB);
        } catch (Exception e) {
            _log.error("article edit error",e);
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
        }
        return result;
    }

    @RequestMapping("/manage")
    public String manage(Model model){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(fqUserCache.getRole() != 1){
            return GENERAL_ERROR_URL;
        }
        return "/article/manage.html";
    }
    @RequestMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page){
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page,CommonConstant.DEAULT_PAGE_SIZE);
            ArticleExample example = new ArticleExample();
            example.setOrderByClause("create_time desc");
            List<Article> articles = articleService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(articles);
            result.setData(pageInfo);
        } catch (Exception e) {
            _log.error("文章分页出错",e);
            result.setCode("1");
            result.setMessage("文章分页出错");
        }
        return result;
    }

    @RequestMapping("/manage/examine")
    @ResponseBody
    public Object examine(Integer articleId){
        BaseResult result = new BaseResult();
        try {
            FqUserCache currentUser = getCurrentUser();
            if(currentUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(currentUser.getRole() != 1){
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(article.getDelFlag().equals(0)){
                return result;
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(article.getUserId());
            if(fqUser == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            article.setDelFlag(YesNoEnum.NO.getValue());
            articleService.updateByPrimaryKeySelective(article);
            _log.info("审核文章，当前用户：{}，被审核用户：{}，状态：{}",currentUser.getId(),article.getUserId(),0);
            CommonUtils.addActiveNum(article.getUserId(),ActiveNumEnum.POST_ARTICLE.getValue());
            CommonUtils.addOrDelUserQudouNum(currentUser,5);
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(new Date());
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(article.getUserId());
            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
            message.setContent("系统消息通知：您发表的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId() +"\" target=\"_blank\">文章</a>已经审核通过！ "+ DateUtil.formatDateTime(new Date()));
            messageService.insert(message);
            result.setMessage("审核通过");
        } catch (Exception e) {
            _log.error("文章审核出错",e);
            result.setCode("1");
            result.setMessage("文章审核出错");
        }
        return result;
    }

    @PostMapping("/manage/recommend/{articleId}")
    @ResponseBody
    public Object recommend(@PathVariable Integer articleId){
        BaseResult result = new BaseResult();
        try {
            FqUserCache currentUser = getCurrentUser();
            if(currentUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(currentUser.getRole() != 1){
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(YesNoEnum.YES.getValue().equals(article.getIsRecommend())){
                return result;
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(article.getUserId());
            if(fqUser == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            article.setIsRecommend(YesNoEnum.YES.getValue());
            articleService.updateByPrimaryKeySelective(article);
            _log.info("推荐文章，当前用户：{}，被推荐用户：{}，状态：{}",currentUser.getId(),article.getUserId(),1);
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(new Date());
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(article.getUserId());
            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
            message.setContent("系统消息通知：恭喜您，您发表的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId() +"\" target=\"_blank\">文章</a>被推荐！"+ DateUtil.formatDateTime(new Date()));
            messageService.insert(message);
            result.setMessage("推荐成功");
        } catch (Exception e) {
            _log.error("文章推荐出错",e);
            result.setCode("1");
            result.setMessage("文章推荐出错");
        }
        return result;
    }

    @PostMapping("/manage/htmlUnescape/{articleId}")
    @ResponseBody
    public Object htmlUnescape(@PathVariable Integer articleId){
        BaseResult result = new BaseResult();
        try {
            FqUserCache currentUser = getCurrentUser();
            if(currentUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(currentUser.getRole() != 1){
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(article.getUserId());
            if(fqUser == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            Article toUpdate = new Article();
            toUpdate.setId(article.getId());
            toUpdate.setArticleContent(HtmlUtils.htmlUnescape(article.getArticleContent()));
            articleService.updateByPrimaryKeySelective(toUpdate);
            _log.info("反转义文章，当前用户：{}，被转义文章用户：{}",currentUser.getId(),article.getUserId());
        } catch (Exception e) {
            _log.error("文章反转义出错",e);
            result.setCode("1");
            result.setMessage("文章反转义出错");
        }
        return result;
    }


    @RequestMapping("/{articleId}")
    public String articleDetail(@PathVariable Integer articleId, Model model){
        Article article = articleService.selectByPrimaryKey(articleId);
        if(article == null){
            return GENERAL_NOT_FOUNF_404_URL;
        }
        if(YesNoEnum.YES.getValue().equals(article.getDelFlag())){
            return GENERAL_TOPIC_DELETED_URL;
        }
        if(article.getContentType() == 2){
            article.setArticleContent(HtmlUtils.htmlUnescape(article.getArticleContent()));
        }
        FqUser oUser = userService.selectByPrimaryKey(article.getUserId());
        GeneralCommentExample commentExample = new GeneralCommentExample();
        commentExample.setOrderByClause("create_time ");
        commentExample.createCriteria().andTopicIdEqualTo(articleId).andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
        List<DetailCommentResponse> commentList = commentService.selectUserByExample(commentExample);
        model.addAttribute("commentList",commentList);
        model.addAttribute("article",article);
        model.addAttribute("oUser",oUser);

        //查询类似的文章 类别
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andLabelEqualTo(article.getLabel()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        articleExample.setOrderByClause("browse_count desc");
        PageHelper.startPage(1,10,false);
        List<Article> articles = articleService.selectByExample(articleExample);
        articles =  articles.stream().filter(e-> !e.getId().equals(article.getId())).collect(Collectors.toList());
        model.addAttribute("articles",articles);
        return "/article/detail.html";
    }

    @ResponseBody
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public Object articleReply(GeneralComment comment, HttpServletRequest request, HttpServletResponse response){
        _log.info("articleReply:"+JSON.toJSONString(comment));
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(comment.getTopicId());
            if(article == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            if(!user.getId().equals(comment.getPostUserId())){
                result.setResult(ResultEnum.USER_NOT_SAME);
                return result;
            }
            if(StringUtils.isBlank(comment.getContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空");
                return result;
            }
            comment.setCreateTime(new Date());
            comment.setTopicType(TopicTypeEnum.ARTICLE_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);
            article.setCommentCount(article.getCommentCount() == null?1:article.getCommentCount()+1);
            articleService.updateByPrimaryKey(article);
            if(!user.getId().equals(article.getUserId())){
                String commentContent = comment.getContent();
                if(commentContent.startsWith("@")){
                    int split = commentContent.indexOf(" ");
                    String aiteNickname = StringUtils.substring(commentContent,1,split);
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null && !aiteUser.getId().equals(user.getId())){
                        CMessage message = new CMessage();
                        message.setPostUserId(-1);
                        message.setCreateTime(new Date());
                        message.setDelFlag(YesNoEnum.NO.getValue());
                        message.setReceivedUserId(aiteUser.getId());
                        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                        message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                "在此<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId() +"\" target=\"_blank\">文章评论中</a>中回复了你 "+ DateUtil.formatDateTime(new Date()));
                        messageService.insert(message);
                    }
                }else {
                    CMessage message = new CMessage();
                    message.setPostUserId(-1);
                    message.setCreateTime(new Date());
                    message.setDelFlag(YesNoEnum.NO.getValue());
                    message.setReceivedUserId(article.getUserId());
                    message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                    message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+
                            "/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a>评论了你的" +
                            "<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId()+"\" target=\"_blank\">文章</a> "
                            + DateUtil.formatDateTime(new Date()));
                    messageService.insert(message);
                }
                CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
            }
            result.setData(comment);
        } catch (Exception e) {
            _log.error("文章评论出错",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer articleId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(articleId).andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(articleId,TopicTypeEnum.ARTICLE_TYPE.getValue(),1,user.getId(),new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);
            article.setLikeCount(article.getLikeCount() == null? 1: article.getLikeCount()+1);
            articleService.updateByPrimaryKey(article);

            if(!user.getId().equals(article.getUserId())){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(new Date());
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(article.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                if(user.getNickname().length() > 20){
                    user.setNickname(StringUtils.substring(user.getNickname(),0,20));
                }
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+
                        "/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a>赞了你的" +
                        "<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId()+"\" target=\"_blank\">文章</a> "
                        + DateUtil.formatDateTime(new Date()));
                messageService.insert(message);
            }
            CommonUtils.addActiveNum(user.getId(),ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            _log.error("article like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(Integer articleId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if(article != null){
                if(user.getId().equals(article.getUserId()) || UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())){
                    article.setDelFlag(YesNoEnum.YES.getValue());
                    articleService.updateByPrimaryKeySelective(article);
                    CommonUtils.addActiveNum(article.getUserId(),-ActiveNumEnum.POST_ARTICLE.getValue());
                }else {
                    result.setResult(ResultEnum.DELETE_NOT_MY);
                }
            }else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            _log.error("article comment delete error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("deleteComment")
    @ResponseBody
    public Object deleteComment(Integer commentId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            GeneralComment comment = commentService.selectByPrimaryKey(commentId);
            if(comment != null){
                comment.setDelFlag(YesNoEnum.YES.getValue());
                commentService.updateByPrimaryKey(comment);
            }else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            _log.error("comment delete error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /*
    我的文章
     */
    @GetMapping(value = "/my")
    public String myArticles(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(defaultValue = "1") Integer page,
                             Model model){
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                return "/login.html";
            }
            PageHelper.startPage(page,20);
            ArticleExample example = new ArticleExample();
            example.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<ArticleUserDetail> articles = articleService.selectUserByExampleWithBLOBs(example);

            PageInfo pageInfo = new PageInfo(articles);
            FqLabelExample labelExample = new FqLabelExample();
            labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
            List<FqLabel> labels = fqLabelService.selectByExample(labelExample);
            Map<Integer,String> map = Maps.newHashMap();
            for(FqLabel label : labels){
                map.put(label.getId(),label.getName());
            }
            model.addAttribute("labels",map);
            model.addAttribute("articles",articles);
            model.addAttribute("count",pageInfo.getTotal());
            model.addAttribute("p",page);
            model.addAttribute("pageSize",20);
        } catch (Exception e) {
            _log.error("获取我的文章失败！",e);
        }
        return "/article/articles.html";
    }

    @GetMapping("caiji")
    public void caiji(){
        String result = HttpUtil.get("http://hd.zt.raiyi.com/v9/private/682265b8574104c64c262c1b3f7a3eb771f01e126687b1a14b048025a9b639918ae7d834f2c3158c646add7a52ab8e78/weibo/theme/list?appCode=other_browser&tag=hot");
        AllDataRes allDataRes = JSON.parseObject(result, AllDataRes.class);
        List<SingleData> data = allDataRes.getData();
        for(SingleData singleData : data){
            String content = singleData.getContent();
            String html = singleData.getHtml();
            String htmlUn = HtmlUtils.htmlUnescape(html);
            Article article = new Article();
            article.setArticleTitle(content);
            article.setCreateTime(new Date());
            article.setArticleContent(htmlUn);
            article.setUserId(22);
            articleService.insert(article);
        }

    }

    @PostMapping("/collect/{type}")
    @ResponseBody
    public Object collect(@PathVariable String type, Integer aid, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(aid);
            if(articleDB == null){
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(aid)
                    .andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if("add".equals(type)){
                if(fqCollect == null){
                    fqCollect = new FqCollect();
                    fqCollect.setTopicId(aid);
                    fqCollect.setTopicType(TopicTypeEnum.ARTICLE_TYPE.getValue());
                    fqCollect.setCreateTime(new Date());
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollect.setUserId(fqUser.getId());
                    fqCollectService.insert(fqCollect);
                }else {
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                }
            }else if("remove".equals(type)){
                if(fqCollect == null){
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                }else {
                    fqCollect.setDelFlag(YesNoEnum.YES.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                }
            }else {
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            result.setData(fqCollect.getId());
        } catch (Exception e) {
            _log.error("article collect error",e);
        }
        return result;
    }


    @PostMapping("/collection/find")
    @ResponseBody
    public Object collectFind(Integer aid, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setData(null);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(aid);
            if(articleDB == null){
                result.setData(null);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(aid)
                    .andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if(fqCollect == null){
                result.setData(null);
                return result;
            }else {
                if(fqCollect.getDelFlag().equals(YesNoEnum.YES.getValue())){
                    result.setData(null);
                    return result;
                }
                if(fqCollect.getDelFlag().equals(YesNoEnum.NO.getValue())){
                    result.setData(fqCollect.getId());
                    return result;
                }
            }
            result.setData(null);
        } catch (Exception e) {
            _log.error("article collect error",e);
        }
        return result;
    }
}
