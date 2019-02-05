package com.feiqu.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.HttpClientUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.wangyi.NewsResponse;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqNewsService;
import com.feiqu.system.service.FqUserService;
import com.feiqu.system.service.GeneralCommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* FqNewscontroller
* Created by cwd on 2018/11/14.
*/
@Controller
@RequestMapping("/fqNews")
public class FqNewsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqNewsController.class);

    @Autowired
    private FqNewsService fqNewsService;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private CMessageService messageService;

    /**
    * 跳转到FqNews首页
    */
    @RequestMapping("")
    public String index(@RequestParam(defaultValue = "desc") String order, Model model,
                        @RequestParam(defaultValue = "1") Integer pageIndex) {
        PageHelper.startPage(pageIndex,20);
        FqNewsExample fqNewsExample = new FqNewsExample();
        fqNewsExample.setOrderByClause("gmt_create "+order);
        List<FqNews> fqNews = fqNewsService.selectByExample(fqNewsExample);
        model.addAttribute("fqNews",fqNews);
        PageInfo page = new PageInfo(fqNews);
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",20);//文章放多点好，感觉，要不然老是需要翻页
        model.addAttribute("count",page.getTotal());
        return "/news/index.html";
    }

    /**
    * 添加FqNews页面
    */
    @RequestMapping("/fqNews_add")
    public String fqNews_add() {
        return "/system/FqNews/add.html";
    }

    /**
    * ajax添加FqNews
    */
    @ResponseBody
    @RequestMapping("/add")
    public Object add(FqNews fqNews) {
        BaseResult result = new BaseResult();
        fqNewsService.insert(fqNews);
        return result;
    }

    /**
    * ajax删除FqNews
    */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer fqNewsId) {
        BaseResult result = new BaseResult();
        fqNewsService.deleteByPrimaryKey(fqNewsId);
        return result;
    }

    /**
    * 更新FqNews页面
    */
    @RequestMapping("/edit/{fqNewsId}")
    public Object fqNewsEdit(@PathVariable Integer fqNewsId, Model model) {
        FqNews fqNews = fqNewsService.selectByPrimaryKey(fqNewsId);
        model.addAttribute("fqNews", fqNews);
        return "/system/FqNews/edit.html";
    }

    /**
     * 更新FqNews页面
     */
    @RequestMapping("/detail/{fqNewsId}")
    public String detail(@PathVariable Long fqNewsId, Model model) {
        FqNews fqNews = fqNewsService.selectByPrimaryKey(fqNewsId);
        if(fqNews == null){
            return "/404.html";
        }
        model.addAttribute("fqNews", fqNews);
        GeneralCommentExample commentExample = new GeneralCommentExample();
        commentExample.setOrderByClause("create_time desc");
        commentExample.createCriteria().andTopicIdEqualTo(fqNewsId.intValue()).andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTopicTypeEqualTo(TopicTypeEnum.NEWS_TYPE.getValue());
        List<DetailCommentResponse> commentList = commentService.selectUserByExample(commentExample);
        model.addAttribute("commentList",commentList);
        FqNewsExample fqNewsExample = new FqNewsExample();
        fqNewsExample.createCriteria().andSourceEqualTo(fqNews.getSource()).andIdNotEqualTo(fqNewsId);
        PageHelper.startPage(1,CommonConstant.DEAULT_PAGE_SIZE,false);
        List<FqNews> fqNewsSame = fqNewsService.selectByExample(fqNewsExample);
        model.addAttribute("sameSource",fqNewsSame);
        return "/news/detail.html";
    }

    /**
    * ajax更新FqNews
    */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqNews fqNews) {
        BaseResult result = new BaseResult();
        fqNewsService.updateByPrimaryKey(fqNews);
        return result;
    }

    /**
    * 查询FqNews首页
    */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index, @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqNewsExample example = new FqNewsExample();
        example.setOrderByClause("create_time desc");
        List<FqNews> list = fqNewsService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public Object articleReply(GeneralComment comment){
        logger.info("newsReply:"+JSON.toJSONString(comment));
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqNews fqNews = fqNewsService.selectByPrimaryKey(comment.getTopicId().longValue());
            if(fqNews == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            if(StringUtils.isBlank(comment.getContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空");
                return result;
            }
            Date now = new Date();
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.NEWS_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);
            List<String> aiteNames = CommonUtils.findAiteNicknames(comment.getContent());
            if(aiteNames.size() > 0){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null){
                        if(!aiteUser.getId().equals(user.getId())){
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/fqNews/" + comment.getTopicId() +"\" target=\"_blank\">此新闻</a>中回复了你 -"+ DateUtil.formatDateTime(now));
                            messageService.insert(message);
                        }
                    }
                }
            }
            result.setData(comment);
        } catch (Exception e) {
            logger.error("新闻评论出错",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
    * 查询FqNews首页
    */
    @RequestMapping("collect")
    @ResponseBody
    public Object collect(@RequestParam(required = false) Integer loop) {
        BaseResult baseResult = new BaseResult();
        Stopwatch stopwatch = Stopwatch.createStarted();

        String result = null;
        int loopSize = 10,loopCount = 10,loopIndex = 0;
        if(loop != null){
            loopCount = loop;
        }
        String url = "https://3g.163.com/touch/reconstruct/article/list/BBM54PGAwangning/";
        String suffix = "-10.html";
        try {
            Date now = new Date();
            while (loopIndex < loopCount){
                String realUrl = url + loopSize*loopIndex+suffix;
                result = HttpClientUtil.getWebPage(realUrl);
                int index = result.indexOf(":");
                result = result.substring(index + 1, result.lastIndexOf("]") + 1);
                Console.log(result);
                List<NewsResponse> newsResponseList = JSON.parseArray(result, NewsResponse.class);
                for (NewsResponse newsResponse : newsResponseList) {
                    if(newsResponse.getCommentCount() < 1000){
                        continue;
                    }
                    if(StringUtils.isEmpty(newsResponse.getUrl())){
                        continue;
                    }
                    String result2 = HttpClientUtil.getWebPage(newsResponse.getUrl());
                    result2 = result2.substring(result2.indexOf("<article"), result2.lastIndexOf("</article>") + 10);
                    result2 = result2.substring(0, result2.lastIndexOf("<div class=\"footer\">"));
                    result2 += "</article>";
                    result2 = result2.replaceAll("data-src","src");
                    FqNews fqNews = new FqNews(newsResponse);
                    fqNews.setGmtCreate(now);
                    fqNews.setCommentCount(0);
                    fqNews.setContent(result2);
                    fqNewsService.insert(fqNews);
                }
                loopIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("新闻更新完毕,耗时{}秒",seconds);
        return baseResult;
    }
}