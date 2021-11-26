package com.feiqu.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.enums.GirlCategoryEnum;
import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.exception.job.TaskException;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.spider.TopicInfoPipeline;
import com.feiqu.framwork.support.spider.V2exDTO;
import com.feiqu.framwork.util.HttpClientUtil;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.pojo.response.SimThoughtDTO;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.pojo.response.UserActiveNumResponse;
import com.feiqu.system.pojo.response.wangyi.NewsResponse;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;
import redis.clients.jedis.JedisCommands;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("fqTask")
public class FqTask {
    private static Logger logger = LoggerFactory.getLogger(FqTask.class);

    @Resource
    private NginxLogService logService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private FqFriendLinkService fqFriendLinkService;
    @Resource
    private FqBackgroundImgService fqBackgroundImgService;
    @Resource
    private SuperBeautyService superBeautyService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private ArticleService articleService;
    @Resource
    private FqUserActiveNumService fqUserActiveNumService;
    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private TopicInfoPipeline topicInfoPipeline;
    @Resource
    private FqTopicService fqTopicService;
    @Resource
    private FqNewsService fqNewsService;
    @Resource
    private FqNoticeService fqNoticeService;

    //    @Scheduled(cron = "0 0/30 9-18 * * ? ")//从9点到18点 每半个小时更新一次
//    @Scheduled(cron = "0 51 15 * * ? ")
    public void contentUpdateWork() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            PageHelper.startPage(1, 10, false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("create_time desc");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<ThoughtWithUser> newThoughts = thoughtService.getThoughtWithUser(thoughtExample);
            if (CollectionUtil.isNotEmpty(newThoughts)) {
                newThoughts.forEach(t -> {
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(t.getPicList())) {
                        t.setPictures(Arrays.asList(t.getPicList().split(",")));
                    }
                });
            }
            CommonConstant.NEW_THOUGHT_LIST = newThoughts;

            ArticleExample example = new ArticleExample();
            example.setOrderByClause("browse_count desc");
            example.createCriteria().andCreateTimeGreaterThan(DateUtil.offsetDay(new Date(), -31));
            PageHelper.startPage(0, 10, false);
            List<Article> hotArticles = articleService.selectByExample(example);
        /*for(Article article : hotArticles){
            if(article.getArticleTitle().length() > 20){
                article.setArticleTitle(StringUtils.substring(article.getArticleTitle(),0,20) + "……");
            }
        }*/

            if (CollectionUtil.isNotEmpty(hotArticles)) {
                CommonConstant.HOT_ARTICLE_LIST = hotArticles;
            }

            FqNoticeExample fqNoticeExample = new FqNoticeExample();
            fqNoticeExample.setOrderByClause("fq_order asc");
            fqNoticeExample.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
            List<FqNotice> list = fqNoticeService.selectByExample(fqNoticeExample);
            if(CollectionUtil.isNotEmpty(list)){
                CommonConstant.FQ_NOTICE_LIST = list;
            }

            PageHelper.startPage(0, 5, false);
            SuperBeautyExample beautyExample = new SuperBeautyExample();
            beautyExample.setOrderByClause("like_count desc");
            List<BeautyUserResponse> beauties = superBeautyService.selectDetailByExample(beautyExample);
            if (CollectionUtil.isNotEmpty(beauties)) {
                CommonConstant.HOT_BEAUTY_LIST = beauties;
            }

            int month = DateUtil.thisMonth() + 1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> userIds = commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT + month, 0, 4);
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<Integer> userIdList = Lists.newArrayList();
                for (String userId : userIds) {
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample fqUserExample = new FqUserExample();
                example.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                Map<Integer, FqUser> userMap = Maps.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(), fqUser);
                });
                List<UserActiveNumResponse> responses = Lists.newArrayList();
                for (int i = 0; i < userIdList.size(); i++) {
                    double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT + month, userIdList.get(i).toString());
                    if (userMap.get(userIdList.get(i)) == null) {
                        continue;
                    }
                    UserActiveNumResponse userActiveNumResponse = new UserActiveNumResponse(userMap.get(userIdList.get(i)), score, i + 1);
                    responses.add(userActiveNumResponse);
                }
                CommonConstant.FQ_ACTIVE_USER_LIST = responses;
            }
        } catch (Exception e) {
            logger.error("", e);
        }


        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("热门文章以及通知以及热门图片更新完毕,耗时{}秒", seconds);
    }

    //每3个小时更新一次 3-》2
//    @Scheduled(cron = "1 0 */2 * * ? ")
    public void newsWork() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String result = null;
        int loopSize = 10, loopCount = 10, loopIndex = 0;
        String url = "https://3g.163.com/touch/reconstruct/article/list/BBM54PGAwangning/";
        String suffix = "-10.html";
        int filterNum = 0;
        int sameTitleNum = 0;
        int insertNum = 0;
        try {
            Date now = new Date();
            FqNewsExample fqNewsExample = new FqNewsExample();

            while (loopIndex <= loopCount) {
                String realUrl = url + loopSize * loopIndex + suffix;
                logger.info(realUrl);
                result = HttpClientUtil.getWebPage(realUrl);
                int index = result.indexOf(":");
                result = result.substring(index + 1, result.lastIndexOf("]") + 1);

                List<NewsResponse> newsResponseList = JSON.parseArray(result, NewsResponse.class);
                if (CollectionUtils.isEmpty(newsResponseList)) {
                    logger.info(result);
                    filterNum += 10;
                    continue;
                }
                for (NewsResponse newsResponse : newsResponseList) {
                    if (newsResponse.getCommentCount() < 1000) {
                        filterNum += 1;
                        continue;
                    }
                    if (org.apache.commons.lang.StringUtils.isEmpty(newsResponse.getUrl())) {
                        filterNum += 1;
                        continue;
                    }
                    if (newsResponse.getUrl().startsWith("00")) {
                        filterNum += 1;
                        continue;
                    }
                    if (newsResponse.getUrl().contains("mp.weixin.qq.com") || newsResponse.getUrl().contains("photoview") || newsResponse.getUrl().contains("ithome")) {
                        filterNum += 1;
                        continue;
                    }
                    logger.info("新闻下面的url：" + newsResponse.getUrl());
                    fqNewsExample.clear();
                    fqNewsExample.createCriteria().andTitleEqualTo(newsResponse.getTitle());
                    int count = fqNewsService.countByExample(fqNewsExample);
                    if (count > 0) {
                        filterNum += 1;
                        sameTitleNum += 1;
                        continue;
                    }
                    String result2 = null;
                    try {
                        result2 = HttpClientUtil.getWebPage(newsResponse.getUrl());
                    } catch (Exception e) {
                        logger.error("详情报错：" + newsResponse.getUrl(), e);
                        filterNum += 1;
                        continue;
                    }
                    result2 = result2.substring(result2.indexOf("<article"), result2.lastIndexOf("</article>") + 10);
//                    result2 = result2.substring(0, result2.lastIndexOf("<div class=\"footer\">"));
//                    result2 += "</article>";
                    result2 = result2.replaceAll("data-src", "src");

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
            logger.error("新闻更新报错", e);
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("过滤掉的新闻条数：{}，过滤掉相同标题的新闻条数：{}，插入新闻条数：{}，新闻更新完毕,耗时{}秒", filterNum, sameTitleNum, insertNum, seconds);
    }

    //爬虫
//    @Scheduled(cron = "0 3 */6 * * ?")
    public void spider() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int result=0;
        try {
            OOSpider ooSpider = OOSpider.create(Site.me()
                            .setUserAgent(CommonConstant.userAgentArray[new Random().nextInt(CommonConstant.userAgentArray.length)])
                            .addHeader("Referer", "https://www.v2ex.com/").setSleepTime(30000).setDomain("v2ex.com"),
                    topicInfoPipeline, V2exDTO.class);
            ooSpider.addUrl("https://www.v2ex.com/?tab=hot")
                    .run();
            FqTopicExample example = new FqTopicExample();
            //少于10条评论的 全部删除
            example.createCriteria().andCommentCountLessThan(10);
            result = fqTopicService.deleteByExample(example);
            stopwatch.stop();
        } catch (Exception e) {
            logger.error("爬虫出错", e);
        }
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("爬虫数据更新完毕,耗时{}秒,删除数据：{}", seconds,result);
    }

    public void sendLogEmail() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        Date now = new Date();
//        DateUtil.offsetDay(now, -1).toString("yyyyMMdd");

        Date begin = DateUtils.addHours(now, -8);
        Date end = DateUtils.addHours(now, -6);

        NginxLogExample example = new NginxLogExample();
        example.createCriteria().andCreateTimeBetween(begin, end).andSpiderTypeEqualTo(0);
        Integer pv = logService.countByExample(example);
        long uv = logService.countUvByExample(example);

        //1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(1);
        long baiduzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(2);
        long googlezhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(3);
        long bingzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(4);
        long sougouzhizhu = logService.countByExample(example);

        String htmlContent = getEmailHtml(DateUtil.offsetDay(now, -1).toString("yyyy-MM-dd") + "的PV UV", pv, uv, baiduzhizhu,
                googlezhizhu, bingzhizhu, sougouzhizhu);
        String yesterday = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");
        String prefix = "/home/chenweidong/feiqu/logs";
        File logFile = new File(prefix + "/feiqu-info-"+yesterday+".log");
        File errorFile = new File(prefix + "/feiqu-error-"+yesterday+".log");
        File gcLogFile = new File(prefix + "/gc.log");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
            helper.setFrom(mailSender.getUsername());
            //邮件主题
            helper.setSubject("社区前一天的PVUV");
            String[] tos = {"573047307@qq.com", "610589771@qq.com", "758991933@qq.com", "571148352@qq.com"};
            //邮件接收者的邮箱地址
            helper.setTo(tos);
            helper.setText(htmlContent, true);
//            File logFile = new File("D:\\logs\\access_20171113.log");

            if (logFile.exists()) {
                helper.addAttachment("logfiles", logFile);
            }
            if (errorFile.exists()) {
                helper.addAttachment("errorFile", errorFile);
            }

            if (gcLogFile.exists()) {
                helper.addAttachment("gcLogFile", gcLogFile);
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("发送邮件失败 ", e);
        } finally {
            String yesterday2 = DateUtil.offsetDay(new Date(), -7).toString("yyyy-MM-dd");
            File logFile2 = new File(prefix + "/feiqu-info-"+yesterday2+".log");
            File logFile3 = new File(prefix + "/feiqu-error-"+yesterday2+".log");
            if (logFile2.exists()) {
                boolean del = logFile2.delete();
                if (!del) {
                    logger.error("删除日志文件失败，{}", logFile2.getPath());
                }
            }
            if (logFile3.exists()) {
                boolean del = logFile3.delete();
                if (!del) {
                    logger.error("删除日志文件失败，{}", logFile3.getPath());
                }
            }
            if (gcLogFile.exists()) {
                FileUtil.writeBytes(("每天更新\r\n").getBytes(),
                        gcLogFile);
            }
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("nginx日志分析发送邮件,耗时{}秒", seconds);
    }

    private String getEmailHtml(String time, long pv, long uv, long baiduzhizhu, long googlezhizhu, long bingzhizhu, long sougouzhizhu) {
        String html = "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">flyfun社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\">飞趣社区开发者</em>： </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  " + time + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  PV:" + pv + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  UV:" + uv + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  百度蜘蛛爬取次数:" + baiduzhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  google蜘蛛爬取次数:" + googlezhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  bing蜘蛛爬取次数:" + bingzhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  搜狗蜘蛛爬取次数:" + sougouzhizhu + " </p> " +
                "<div style=\"\"></div>  </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";
        return html;
    }

    //活跃度计算
    public void activeCount() {
        try {
            Date now = new Date();
            int day = DateUtil.dayOfMonth(now);
            DateTime tbegin = DateUtil.beginOfDay(now);
//            DateTime ybegin = DateUtil.beginOfDay(DateUtil.yesterday());
            boolean isfirstDay = day == 1;
            int month = DateUtil.thisMonth() + 1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> userIds = commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT + month, 0, -1);
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<Integer> userIdList = Lists.newArrayList();
                for (String userId : userIds) {
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample fqUserExample = new FqUserExample();
                fqUserExample.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                Map<Integer, FqUser> userMap = Maps.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(), fqUser);
                });
                FqUserActiveNumExample example = new FqUserActiveNumExample();
                for (int i = 0; i < userIdList.size(); i++) {
                    Double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT + month, userIdList.get(i).toString());
                    if (isfirstDay) {
                        FqUserActiveNum fqUserActiveNum = new FqUserActiveNum();
                        fqUserActiveNum.setGmtCreate(now);
                        fqUserActiveNum.setActiveNum(score == null ? 0 : score.intValue());
                        fqUserActiveNum.setUserId(userIdList.get(i));
                        fqUserActiveNum.setMark(userMap.get(userIdList.get(i)).getNickname() + ":当天活跃度：" + fqUserActiveNum.getActiveNum());
                        fqUserActiveNumService.insert(fqUserActiveNum);
                    } else {
                        example.clear();
                        example.setOrderByClause("ID desc");
                        example.createCriteria().andUserIdEqualTo(userIdList.get(i)).andGmtCreateBetween(DateUtil.beginOfMonth(now).toJdkDate(), tbegin.toJdkDate());
                        FqUserActiveNum yesactive = fqUserActiveNumService.selectFirstByExample(example);
                        if (yesactive == null) {
                            logger.info("没查到数据,用户：{}",userIdList.get(i));
                            yesactive = new FqUserActiveNum();
                            yesactive.setGmtCreate(now);
                            yesactive.setActiveNum(score == null ? 0 : score.intValue());
                            yesactive.setUserId(userIdList.get(i));
                            yesactive.setMark(userMap.get(userIdList.get(i)).getNickname() + ":昨天活跃度：" + yesactive.getActiveNum());
                            fqUserActiveNumService.insert(yesactive);
                        } else {
                            if (yesactive.getActiveNum() != score.intValue()) {
                                //如果昨天的和今天的数据不一样 进行插入
                                logger.info("昨天之前的和今天的数据不一样,用户：{}",userIdList.get(i));
                                yesactive = new FqUserActiveNum();
                                yesactive.setGmtCreate(now);
                                yesactive.setActiveNum(score.intValue());
                                yesactive.setUserId(userIdList.get(i));
                                yesactive.setMark(userMap.get(userIdList.get(i)).getNickname() + ":昨天活跃度：" +
                                        (score.intValue() - (yesactive.getActiveNum() == null ? 0 : yesactive.getActiveNum())));
                                fqUserActiveNumService.insert(yesactive);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("活跃度计算出错！", e);
        } finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
    }

    //nginx日志
    public void nginxLogWork() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Date now = new Date();
            String format = DateUtil.formatDate(now);
            CommonConstant.FQ_IP_DATA_THIS_DAY_FORMAT = "fq_ip_data_" + format;
            CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST = "fq_7_days_hot_thoughts_" + format;
            File file = FileUtil.file("/usr/local/nginx/logs/" + DateUtil.offsetDay(now, -1).toString("yyyyMM"),
                    "access_" + DateUtil.offsetDay(now, -1).toString("yyyyMMdd") + ".log");
//        File file = new File("d://logs//host.access.log");
            if (!file.exists()) {
                throw new TaskException("nginx的log日志文件不存在", TaskException.Code.UNKNOWN);
            }
            List<String> lines = FileUtil.readLines(file, "utf-8");
            String ip;
            String localTime;
            String method;
            String url;
            String http;
            String status;
            String bytes;
            String referer;
            StringBuilder userAgent;
            String xforward;
            String requestTime;
            for (String line : lines) {
                int spiderType = 0;//0代表没有爬虫 1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗
                if (line.contains("Baiduspider")) {
                    spiderType = 1;
                }
                if (line.contains("Googlebot")) {
                    spiderType = 2;
                }
                if (line.contains("Sogouwebspider") || line.contains("www.sogou.com")) {
                    spiderType = 4;
                }
                if (line.contains("bingbot")) {
                    spiderType = 3;
                }
                //来自俄罗斯的搜索引擎蜘蛛
                if (line.contains("YandexBot")) {
                    spiderType = 5;
                }
                if (line.contains("AhrefsBot")) {
                    spiderType = 6;
                }
                if (line.contains("Alibaba")) {
                    continue;
                }
                line = line.replaceAll("-", "");
                String[] dataList = line.split("\\s");
                ip = dataList[0];
                localTime = dataList[3];
                method = dataList[5];
                url = dataList[6];
                http = dataList[7];
                status = dataList[8];
                bytes = dataList[9];
                referer = dataList[10];
                xforward = dataList[dataList.length - 2];
                requestTime = dataList[dataList.length - 1];
                if (url.contains("layui") || url.contains(".css") || url.contains(".js")
                        || url.contains("wplogin.php") || url.contains("favicon.ico") || url.contains("phpmyadmin")
                        || url.contains("/sign/status") || url.contains("/u/msgsCount") || url.contains("message/read")
                        || url.contains("/visit/records") || url.contains(".png") || url.contains("collection/find")) {
                    continue;
                }
                if (status.contains("404")) {
                    continue;
                }
                userAgent = new StringBuilder();
                for (int k = 11; k < dataList.length - 2; k++) {
                    userAgent.append(dataList[k]);
                }
                method = method.replace("\"", "");
                requestTime = requestTime.replaceAll("\"", "");
                http = http.replaceAll("\"", "");
                referer = referer.replaceAll("\"", "");
                if (referer.length() > 250) {
                    logger.info("refer:{} 长度太长 无法存入数据库,准备进行截取", referer);
                    referer = StringUtils.substring(referer, 0, 250);
                }
                if (url.length() > 250) {
                    logger.info("url:{} 长度太长 无法存入数据库,准备进行截取", url);
                    url = StringUtils.substring(url, 0, 250);
                }
                if (method.length() > 250) {
                    logger.info("method:{} 长度太长 无法存入数据库,准备进行截取", method);
                    method = StringUtils.substring(method, 0, 250);
                }
                Double requestTimeDouble = 0d;
                try {
                    requestTimeDouble = Double.valueOf(requestTime);
                } catch (NumberFormatException e) {
                }
                if (userAgent.length() > 254) {
                    logger.info("userAgent:{} 长度太长 无法存入数据库,准备进行截取", userAgent.toString());
                    userAgent.delete(254, userAgent.length());
                }
                NginxLog log = new NginxLog(ip, localTime, method, url, http, status, bytes, referer, xforward,
                        requestTimeDouble, userAgent.toString().replaceAll("\"", ""), spiderType);
                log.setCreateTime(now);
                logService.insert(log);
            }
            NginxLogExample nginxLogExample = new NginxLogExample();
            nginxLogExample.createCriteria().andCreateTimeLessThan(DateUtil.offsetMonth(new Date(), -12));//删除90天之前的记录
            logService.deleteByExample(nginxLogExample);
            //更新7天的最热想法
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            PageHelper.startPage(1, 5, false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("comment_count desc ");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andCreateTimeGreaterThan(DateUtil.offsetDay(now, -7));
            List<SimThoughtDTO> simThoughtDTOS = Lists.newArrayList();
            List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
            if (CollectionUtil.isNotEmpty(thoughts)) {
                thoughts.forEach(t -> {
                    SimThoughtDTO simThoughtDTO = new SimThoughtDTO();
                    simThoughtDTO.setId(t.getId());
                    simThoughtDTO.setContent(t.getThoughtContent());
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(t.getPicList())) {
                        simThoughtDTO.setPic(t.getPicList().split(",")[0]);
                    } else {
                        simThoughtDTO.setPic("");
                    }
                    simThoughtDTOS.add(simThoughtDTO);
                });
            }
            commands.set(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST, JSON.toJSONString(simThoughtDTOS));
            commands.expire(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST, 2 * 24 * 60 * 60);
        } catch (Exception e) {
            logger.error("nginx日志分析报错", e);
        } finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("nginx日志分析完毕,耗时{}秒", seconds);
    }

    public void articleGernerate() {
        try {
            File file = new File("/home/chenweidong/webmagic/qdfuns.com");
            if(!file.exists()){
                throw new TaskException("文件夹不存在", TaskException.Code.CONFIG_ERROR);
            }
            File[] files = file.listFiles();
            Date now = new Date();
            int index = 1;
            Pattern pattern = Pattern.compile("<img.*>");
            Pattern pattern2 = Pattern.compile("<svg.*</svg>");
            for (File fileSingle : files) {
                String all = FileUtil.readString(fileSingle, "UTF-8");
                JSONObject jsonObject = new JSONObject(all);
                if (jsonObject.isEmpty()) {
                    continue;
                }
                String title = jsonObject.getStr("title");
                JSONArray authors = jsonObject.getJSONArray("author");
                if (authors.isEmpty()) {
                    continue;
                }
                String category = authors.get(2).toString();
                title = HtmlUtils.htmlEscape(title + "-" + category);
                JSONObject contentJson = jsonObject.getJSONObject("content");
                String content = contentJson.getStr("firstSourceText");
                content = ReUtil.delAll(pattern, content);
                content = ReUtil.delAll(pattern2, content);
                Article article = new Article();
                article.setDelFlag(YesNoEnum.NO.getValue());
                article.setArticleTitle(title);
                article.setBrowseCount(0);
                article.setAnonymousSwitch(0);
                article.setCommentCount(0);
                article.setIsRecommend(0);
                article.setLabel(1);
//                article.setLabel(5);
                article.setUserId(22);
//                article.setUserId(8);
                article.setLikeCount(0);
                int index1 = content.indexOf("id=\"comment\"");
                if (index1 != -1) {
                    content = content.substring(0, index1 - 3) + "</dd>";
                }
                content = ReUtil.delAll(pattern, content);

//            article.setArticleContent(HtmlUtils.htmlUnescape(content));
                article.setArticleContent(content);
                article.setCreateTime(now);
                article.setContentType(2);
                try {
                    articleService.insert(article);
                } catch (Exception e) {
                    logger.error("生成文章报错，文章详情：" + article.toString(), e);
                    fileSingle.delete();
                    continue;
                }
                fileSingle.delete();
                if (index > 10) {
                    break;
                }
                index++;
            }
        } catch (Exception e) {
            logger.error("生成文章报错", e);
        }
    }

    public void superBeauty() throws TaskException {
        String picRootPath = "/home/chenweidong/meinv";
        File rootFile = new File(picRootPath);
        if(!rootFile.exists()){
            throw new TaskException("文件夹不存在,本机ip："+ SystemUtil.getHostInfo().getAddress(),TaskException.Code.UNKNOWN);
        }
        logger.info("rootFile文件名：{},能否被读：{}", rootFile.getName(), rootFile.canRead());
        File[] categoryFiles = rootFile.listFiles();
        long currentTimeMillis = System.currentTimeMillis();
        String picLog = "";
        if (categoryFiles != null && categoryFiles.length > 0) {
            Date now = new Date();
            for (File categoryFile : categoryFiles) {
                boolean insertPic = false;
                logger.info("文件名：{},能否被读：{}", categoryFile.getName(), categoryFile.canRead());
                File[] files = categoryFile.listFiles();
                if (files == null || files.length == 0) {
                    categoryFile.delete();
                    continue;
                }
                for (File file : files) {
                    logger.info("3文件名：{},能否被读：{}", file.getName(), file.canRead());
                    File[] imgs = file.listFiles();
                    if (imgs != null && imgs.length > 0) {
                        String imgUrl = "";
                        String title = "";
                        StringJoiner picUrls = new StringJoiner(",");
                        StringJoiner picDescs = new StringJoiner(",");
                        for (File img : imgs) {
                            String imgName = img.getName();
                            if (imgName.contains("(")) {
                                imgName = imgName.substring(0, imgName.indexOf("("));
                            }
                            String picUrl = null;
                            try {
                                picUrl = FileSystemClient.getPublicClient().upload(CommonConstant.FILE_NAME_PREFIX + currentTimeMillis + ".jpg", img);
                            } catch (Exception e) {
                                logger.error("上传图片出错，图片名：" + imgName, e);
                                boolean delete = img.delete();
                                continue;
                            }
                            picUrl += CommonConstant.QINIU_PIC_STYLE_NAME;
                            picUrls.add(picUrl);
                            picDescs.add(imgName);
                            imgUrl = picUrl;
                            title = imgName;
                            boolean delete = img.delete();
                            logger.info("img文件名：{}，是否删除成功：{}", imgName, delete);
                            currentTimeMillis++;
                        }
                        if (StringUtils.isEmpty(title)) {
                            continue;
                        }
                        title = StrUtil.str(title, Charset.forName("utf-8"));
                        SuperBeauty superBeauty = new SuperBeauty();
                        superBeauty.setImgUrl(imgUrl);
                        superBeauty.setTitle(title);
                        superBeauty.setLikeCount(0);
                        superBeauty.setCreateTime(now);
                        superBeauty.setUploadUserId(22);
                        superBeauty.setCategory(GirlCategoryEnum.NV_SHENG.getCode());
                        superBeauty.setDelFlag(YesNoEnum.NO.getValue());
                        superBeauty.setPicList(picUrls.toString());
                        superBeauty.setPicDescList(picDescs.toString());
                        superBeautyService.insert(superBeauty);
                        picLog = superBeauty.toString();
                        insertPic = true;
                        FileUtil.del(file);
                    }
                    if (insertPic) {
                        break;
                    }
                }
            }
        }
        logger.info(picLog);
    }

    //新用户记录
    public void newUserRcord() throws TaskException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String picLog = "";
        try {
            FqUserExample userExample = new FqUserExample();
            userExample.setOrderByClause("id desc");
            userExample.createCriteria().andStatusEqualTo(UserStatusEnum.NORMAL.getValue());
//        userExample.createCriteria().andCreateTimeBetween(DateUtil.beginOfDay(DateUtil.yesterday()),DateUtil.endOfDay(DateUtil.yesterday()));
            PageHelper.startPage(0, 10, false);
            List<FqUser> newUsers = fqUserService.selectByExample(userExample);//昨天的新用户
            List<FqUserSim> sims = Lists.newArrayListWithExpectedSize(newUsers.size());
            for (FqUser fqUser : newUsers) {
                FqUserSim sim = new FqUserSim(fqUser);
                sims.add(sim);
            }
            CommonConstant.NEW_SIMPLE_USERS = sims;

            //计算用户数量是
            userExample.clear();
            CommonConstant.FQ_USER_TOTAL_COUNT = fqUserService.countByExample(userExample);

            List<FqFriendLink> fqFriendLinks = fqFriendLinkService.selectByExample(new FqFriendLinkExample());
            CommonConstant.FRIEND_LINK_LIST = fqFriendLinks;

            FqBackgroundImgExample backgroundImgExample = new FqBackgroundImgExample();
            backgroundImgExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(backgroundImgExample);
            CommonConstant.bgImgUrl = fqBackgroundImg == null ? "https://img.t.sinajs.cn/t6/skin/skinvip805/images/body_bg.jpg?id=1410943047113" : fqBackgroundImg.getImgUrl();
        }
        catch (Exception e) {
            logger.error("新用户以及友链更新、插入图片报错", e);
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("新用户以及友链更新完毕,完成插入图片，耗时{}秒,图片详情：{}", seconds, picLog);
    }

}
