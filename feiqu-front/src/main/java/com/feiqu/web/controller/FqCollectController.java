package com.feiqu.web.controller;

import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.FqCollect;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.ArticleCollectResponse;
import com.feiqu.system.pojo.response.ThoughtCollectResponse;
import com.feiqu.system.service.FqCollectService;
import com.feiqu.framwork.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* FqCollectcontroller
* Created by cwd on 2018/1/15.
*/
@Controller
@RequestMapping("/collection")
public class FqCollectController  {

    private static Logger logger = LoggerFactory.getLogger(FqCollectController.class);

    @Autowired
    private FqCollectService collectService;
    @Autowired
    private WebUtil webUtil;

    @GetMapping("/find")
    public String collectArticle(){
        return "/collection/collections";

    }

    /**
     * 我收藏的文章
     * @return
     */
    @PostMapping("/find/article")
    @ResponseBody
    public Object collectArticle(HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = webUtil.currentUser(request,response);
            if(fqUserCache == null){
                return result;
            }
            FqCollect fqCollect = new FqCollect();
            fqCollect.setUserId(fqUserCache.getId());
            fqCollect.setDelFlag(YesNoEnum.NO.getValue());
            fqCollect.setTopicType(TopicTypeEnum.ARTICLE_TYPE.getValue());

            List<ArticleCollectResponse> collectResponseList =  collectService.selectWithArticleByEntity(fqCollect);
            result.setData(collectResponseList);
        } catch (Exception e) {
            logger.error("文章的收藏查询出错",e);
        }
        return result;
    }

    /**
     * 我收藏的想法
     * @return
     */
    @PostMapping("/find/thought")
    @ResponseBody
    public Object collectThought(HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = webUtil.currentUser(request,response);
            if(fqUserCache == null){
                return result;
            }
            FqCollect fqCollect = new FqCollect();
            fqCollect.setUserId(fqUserCache.getId());
            fqCollect.setDelFlag(YesNoEnum.NO.getValue());
            fqCollect.setTopicType(TopicTypeEnum.THOUGHT_TYPE.getValue());
            List<ThoughtCollectResponse> collectResponseList =  collectService.selectWithThoughtByEntity(fqCollect);
            result.setData(collectResponseList);
        } catch (Exception e) {
            logger.error("文章的收藏查询出错",e);
        }
        return result;
    }

}
