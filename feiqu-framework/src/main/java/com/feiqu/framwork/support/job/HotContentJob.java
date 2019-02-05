package com.feiqu.framwork.support.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.spider.TopicInfoPipeline;
import com.feiqu.framwork.support.spider.V2exDTO;
import com.feiqu.framwork.util.HttpClientUtil;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.pojo.response.UserActiveNumResponse;
import com.feiqu.system.pojo.response.wangyi.NewsResponse;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * HotContentJob
 *
 * @author chenweidong
 * @date 2017/11/18
 */
@Component
public class HotContentJob {

    private final static Logger logger = LoggerFactory.getLogger(HotContentJob.class);
    @Resource
    private ArticleService articleService;

    @Resource
    private FqNoticeService fqNoticeService;
    @Resource
    private SuperBeautyService superBeautyService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private FqNewsService fqNewsService;
    @Resource
    private TopicInfoPipeline topicInfoPipeline;
    @Resource
    private FqTopicService fqTopicService;
    @Resource
    private FqUserService fqUserService;



    @Scheduled(cron = "0 0/30 9-18 * * ? ")//从9点到18点 每半个小时更新一次
//    @Scheduled(cron = "0 51 15 * * ? ")
    public void work(){
        Stopwatch stopwatch = Stopwatch.createStarted();

        PageHelper.startPage(1, 5 , false);
        ThoughtExample thoughtExample = new ThoughtExample();
        thoughtExample.setOrderByClause("create_time desc");
        thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<ThoughtWithUser> newThoughts = thoughtService.getThoughtWithUser(thoughtExample);
        if(CollectionUtil.isNotEmpty(newThoughts)){
            newThoughts.forEach(t->{
                if(StringUtils.isNotEmpty(t.getPicList())){
                    t.setPictures(Arrays.asList(t.getPicList().split(",")));
                }
            });
        }
        CommonConstant.NEW_THOUGHT_LIST = newThoughts;

        PageHelper.startPage(1, 5 , false);
        thoughtExample.clear();
        thoughtExample.setOrderByClause("comment_count desc ");
        thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        CommonConstant.HOT_THOUGHT_LIST = thoughtService.getThoughtWithUser(thoughtExample);

        ArticleExample example = new ArticleExample();
        example.setOrderByClause("browse_count desc");
        example.createCriteria().andCreateTimeGreaterThan(DateUtil.offsetDay(new Date(),-31));
        PageHelper.startPage(0,10,false);
        List<Article> hotArticles = articleService.selectByExample(example);
        /*for(Article article : hotArticles){
            if(article.getArticleTitle().length() > 20){
                article.setArticleTitle(StringUtils.substring(article.getArticleTitle(),0,20) + "……");
            }
        }*/

        if(CollectionUtil.isNotEmpty(hotArticles)){
            CommonConstant.HOT_ARTICLE_LIST = hotArticles;
        }

        FqNoticeExample fqNoticeExample = new FqNoticeExample();
        fqNoticeExample.setOrderByClause("fq_order asc");
        fqNoticeExample.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
        List<FqNotice> list = fqNoticeService.selectByExample(fqNoticeExample);
        if(CollectionUtil.isNotEmpty(list)){
            CommonConstant.FQ_NOTICE_LIST = list;
        }

        PageHelper.startPage(0,5,false);
        SuperBeautyExample beautyExample = new SuperBeautyExample();
        beautyExample.setOrderByClause("like_count desc");
        List<BeautyUserResponse> beauties = superBeautyService.selectDetailByExample(beautyExample);
        if(CollectionUtil.isNotEmpty(beauties)){
            CommonConstant.HOT_BEAUTY_LIST = beauties;
        }

        try {
            int month = DateUtil.thisMonth()+1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> userIds =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,4);
            if(CollectionUtils.isNotEmpty(userIds)){
                List<Integer> userIdList = Lists.newArrayList();
                for(String userId : userIds){
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample fqUserExample = new FqUserExample();
                example.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                Map<Integer,FqUser> userMap = Maps.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(),fqUser);
                });
                List<UserActiveNumResponse> responses = Lists.newArrayList();
                for(int i = 0;i<userIdList.size();i++){
                    double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,userIdList.get(i).toString());
                    if(userMap.get(userIdList.get(i)) == null){
                        continue;
                    }
                    UserActiveNumResponse userActiveNumResponse = new UserActiveNumResponse(userMap.get(userIdList.get(i)),score,i+1);
                    responses.add(userActiveNumResponse);
                }
                CommonConstant.FQ_ACTIVE_USER_LIST = responses;
            }
        } catch (Exception e) {
            logger.error("",e);
        }

        Runtime runtime = Runtime.getRuntime();
        String memoryInfo ="freeMemory:"+runtime.freeMemory()+",totalMemory:"+runtime.totalMemory();

        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("热门文章以及通知以及热门图片更新完毕,耗时{}秒,内存信息：{}",seconds,memoryInfo);
    }
    //每3个小时更新一次
    @Scheduled(cron = "1 0 */3 * * ? ")
    public void newsWork(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        String result = null;
        int loopSize = 10,loopCount = 10,loopIndex = 0;
        String url = "https://3g.163.com/touch/reconstruct/article/list/BBM54PGAwangning/";
        String suffix = "-10.html";
        int filterNum = 0;
        int sameTitleNum = 0;
        int insertNum = 0;
        try {
            Date now = new Date();
            FqNewsExample fqNewsExample = new FqNewsExample();

            while (loopIndex <= loopCount){
                String realUrl = url + loopSize*loopIndex+suffix;
                logger.info(realUrl);
                result = HttpClientUtil.getWebPage(realUrl);
                int index = result.indexOf(":");
                result = result.substring(index + 1, result.lastIndexOf("]") + 1);

                List<NewsResponse> newsResponseList = JSON.parseArray(result, NewsResponse.class);
                if(CollectionUtils.isEmpty(newsResponseList)){
                    logger.info(result);
                    filterNum += 10;
                    continue;
                }
                for (NewsResponse newsResponse : newsResponseList) {
                    if(newsResponse.getCommentCount() < 1000){
                        filterNum+=1;
                        continue;
                    }
                    if(StringUtils.isEmpty(newsResponse.getUrl())){
                        filterNum+=1;
                        continue;
                    }
                    if(newsResponse.getUrl().startsWith("00")){
                        filterNum+=1;
                        continue;
                    }
                    if(newsResponse.getUrl().contains("mp.weixin.qq.com") || newsResponse.getUrl().contains("photoview") || newsResponse.getUrl().contains("ithome")){
                        filterNum+=1;
                        continue;
                    }
                    logger.info("新闻下面的url："+newsResponse.getUrl());
                    fqNewsExample.clear();
                    fqNewsExample.createCriteria().andTitleEqualTo(newsResponse.getTitle());
                    int count = fqNewsService.countByExample(fqNewsExample);
                    if(count > 0){
                        filterNum+=1;
                        sameTitleNum+=1;
                        continue;
                    }
                    String result2 = null;
                    try {
                        result2 = HttpClientUtil.getWebPage(newsResponse.getUrl());
                    } catch (Exception e) {
                        logger.error("详情报错：",e);
                        filterNum+=1;
                        continue;
                    }
                    result2 = result2.substring(result2.indexOf("<article"), result2.lastIndexOf("</article>") + 10);
                    result2 = result2.substring(0, result2.lastIndexOf("<div class=\"footer\">"));
                    result2 += "</article>";
                    result2 = result2.replaceAll("data-src","src");

                    FqNews fqNews = new FqNews(newsResponse);
                    fqNews.setGmtCreate(now);
                    fqNews.setCommentCount(0);
                    fqNews.setContent(result2);
                    fqNewsService.insert(fqNews);
                    //https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/E1HA59FP0001899N/comments/hotList?offset=0&limit=2&headLimit=3&tailLimit=1&callback=hotList&ibc=newswap
                    //https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/E1HE234A0001899N/comments/hotList?offset=0&limit=2&headLimit=3&tailLimit=1&callback=hotList&ibc=newswap
                    //抓取热门评论
                    //hotList(
                    //{"commentIds":["336761335","336764402"],"comments":{"336764402":{"against":1,"anonymous":false,"buildLevel":1,"commentId":336764402,"content":"饿狗","createTime":"2018-11-26 10:11:18","ext":{},"favCount":0,"ip":"39.181.*.*","isDel":false,"postId":"E1HE234A0001899N_336764402","productKey":"a2869674571f77b5a0867c3d71db5856","shareCount":0,"siteName":"网易","source":"ph","unionState":false,"user":{"avatar":"http://mobilepics.nosdn.127.net/netease_subject/ee4b25e1484e81d0a57a64ee886903701541929401968170","incentiveInfoList":[],"location":"浙江省台州市","nickname":"秦淮颂金陵美","redNameInfo":[],"userId":105372094,"userType":1,"vipInfo":"vipy"},"vote":895},"336761335":{"against":0,"anonymous":false,"buildLevel":1,"commentId":336761335,"content":"不得不佩服饿渣就是强","createTime":"2018-11-26 10:04:37","favCount":0,"ip":"101.233.*.*","isDel":false,"postId":"E1HE234A0001899N_336761335","productKey":"a2869674571f77b5a0867c3d71db5856","shareCount":0,"siteName":"网易","source":"ph","unionState":false,"user":{"avatar":"http://mobilepics.nosdn.127.net/netease_subject/1cd65ab0c6ec53f43ef80ab572b7420a1525585493339128","incentiveInfoList":[],"location":"广东省深圳","nickname":"身上带有X","redNameInfo":[],"userId":640066,"userType":1,"vipInfo":""},"vote":685}}});
                    insertNum++;
                }
                loopIndex++;
            }
        } catch (Exception e) {
            logger.error("新闻更新报错",e);
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("过滤掉的新闻条数：{}，过滤掉相同标题的新闻条数：{}，插入新闻条数：{}，新闻更新完毕,耗时{}秒",filterNum,sameTitleNum,insertNum,seconds);
    }

    //爬虫
    @Scheduled(cron = "0 3 */6 * * ?")
    public void spider(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            OOSpider ooSpider = OOSpider.create(Site.me()
                            .setUserAgent(CommonConstant.userAgentArray[new Random().nextInt(CommonConstant.userAgentArray.length)])
                            .addHeader("Referer","https://www.v2ex.com/").setSleepTime(30000).setDomain("v2ex.com"),
                    topicInfoPipeline, V2exDTO.class);
            ooSpider.addUrl("https://www.v2ex.com/?tab=hot")
                    .run();
            stopwatch.stop();

            /*String s = HttpClientUtil.getWebPage("https://api.readhub.cn/topic?lastCursor=&pageSize=20");
            JSONObject jsonObject = new JSONObject(s);
            JSONArray data = jsonObject.getJSONArray("data");
            Date now = new Date();
            for (Object d : data) {
                JSONObject j = (JSONObject) d;
                String summary = j.getStr("summary");
                String publishDate = j.getStr("publishDate");
                String title = j.getStr("title");
                String url = "";
                JSONArray newsArray = j.getJSONArray("newsArray");
                if (!newsArray.isEmpty()) {
                    JSONObject ja = (JSONObject) newsArray.get(0);
                    url = ja.getStr("url");
                }
    //                order = ((JSONObject) d).getInt("order");
                FqTopic fqTopic = new FqTopic();
                fqTopic.setAuthor("");
                fqTopic.setAuthorIcon("");
                fqTopic.setCommentCount(0);
                fqTopic.setContent(summary+"<br>发布时间："+publishDate+"<br>相关地址："+url);
                fqTopic.setTitle(title);
                fqTopic.setSource(SpiderSourceEnum.READ_HUB.getValue());
                fqTopic.setGmtCreate(now);
                fqTopic.setType("");
                fqTopicService.insert(fqTopic);
            }*/
        } catch (Exception e) {
            logger.error("爬虫出错",e);
        }
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("爬虫数据更新完毕,耗时{}秒",seconds);
    }
}
