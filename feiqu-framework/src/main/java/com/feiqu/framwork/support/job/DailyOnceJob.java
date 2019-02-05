package com.feiqu.framwork.support.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.enums.GirlCategoryEnum;
import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.response.SimThoughtDTO;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import com.jeesuite.filesystem.FileSystemClient;
import com.jeesuite.filesystem.provider.FSOperErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Component
public class DailyOnceJob {

    private final static Logger logger = LoggerFactory.getLogger(DailyOnceJob.class);
    @Resource
    private NginxLogService logService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private FqFriendLinkService fqFriendLinkService;
    @Resource
    private FqBackgroundImgService fqBackgroundImgService;
    @Resource
    private FqShortVideoService fqShortVideoService;
    @Resource
    private SuperBeautyService superBeautyService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private ArticleService articleService;
    @Resource
    private FqUserActiveNumService fqUserActiveNumService;

    //每天活跃度计算 昨天的
    @Scheduled(cron = "30 0 0 * * ? ")
    public void activeCount(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Date now = new Date();
            int day = DateUtil.dayOfMonth(now);
            DateTime tbegin = DateUtil.beginOfDay(now);
            DateTime ybegin = DateUtil.beginOfDay(DateUtil.yesterday());
            boolean isfirstDay = day == 1;
            int month = DateUtil.thisMonth()+1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> userIds =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,-1);
            if(CollectionUtils.isNotEmpty(userIds)){
                List<Integer> userIdList = Lists.newArrayList();
                for(String userId : userIds){
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample fqUserExample = new FqUserExample();
                fqUserExample.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                Map<Integer,FqUser> userMap = Maps.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(),fqUser);
                });
                FqUserActiveNumExample example = new FqUserActiveNumExample();
                for(int i = 0;i<userIdList.size();i++){
                    Double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,userIdList.get(i).toString());
                    if(isfirstDay){
                        FqUserActiveNum fqUserActiveNum = new FqUserActiveNum();
                        fqUserActiveNum.setGmtCreate(now);
                        fqUserActiveNum.setActiveNum(score == null ? 0 : score.intValue());
                        fqUserActiveNum.setUserId(userIdList.get(i));
                        fqUserActiveNum.setMark(userMap.get(userIdList.get(i)).getNickname()+":当天活跃度："+fqUserActiveNum.getActiveNum());
                        fqUserActiveNumService.insert(fqUserActiveNum);
                    }else {
                        example.clear();
                        example.createCriteria().andUserIdEqualTo(userIdList.get(i)).andGmtCreateBetween(ybegin.toJdkDate(),tbegin.toJdkDate());
                        FqUserActiveNum yesactive = fqUserActiveNumService.selectFirstByExample(example);
                        if(yesactive == null){
                            yesactive = new FqUserActiveNum();
                            yesactive.setGmtCreate(now);
                            yesactive.setActiveNum(score == null ? 0 : score.intValue());
                            yesactive.setUserId(userIdList.get(i));
                            yesactive.setMark(userMap.get(userIdList.get(i)).getNickname()+":当天活跃度："+yesactive.getActiveNum());
                            fqUserActiveNumService.insert(yesactive);
                        }else {
                            //如果昨天的和今天的数据不一样 进行插入
                            if(yesactive.getActiveNum() != score.intValue()){
                                yesactive = new FqUserActiveNum();
                                yesactive.setGmtCreate(now);
                                yesactive.setActiveNum(score.intValue() - yesactive.getActiveNum());
                                yesactive.setUserId(userIdList.get(i));
                                yesactive.setMark(userMap.get(userIdList.get(i)).getNickname()+":当天活跃度："+yesactive.getActiveNum());
                                fqUserActiveNumService.insert(yesactive);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("活跃度计算出错！",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("活跃度计算完毕,耗时{}秒",seconds);
    }

    //nginx日志
    @Scheduled(cron = "0 0 1 * * ? ")
    public void work(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Date now = new Date();
            String format = DateUtil.formatDate(now);
            CommonConstant.FQ_IP_DATA_THIS_DAY_FORMAT = "fq_ip_data_"+format;
            CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST = "fq_7_days_hot_thoughts_"+format;
            File file = FileUtil.file("/usr/local/nginx/logs/" + DateUtil.offsetDay(now, -1).toString("yyyyMM"),
                    "access_" + DateUtil.offsetDay(now, -1).toString("yyyyMMdd") + ".log");
//        File file = new File("d://logs//host.access.log");
            if(file.exists()){
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
                    if(line.contains("Baiduspider")){
                        spiderType=1;
                    }
                    if(line.contains("Googlebot")){
                        spiderType=2;
                    }
                    if(line.contains("Sogouwebspider") || line.contains("www.sogou.com")){
                        spiderType=4;
                    }
                    if(line.contains("bingbot")){
                        spiderType=3;
                    }
                    //来自俄罗斯的搜索引擎蜘蛛
                    if(line.contains("YandexBot")){
                        spiderType=5;
                    }
                    if(line.contains("AhrefsBot")){
                        spiderType=6;
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
                            || url.contains("wplogin.php") || url.contains("favicon.ico")||url.contains("phpmyadmin")
                            || url.contains("/sign/status") || url.contains("/u/msgsCount") || url.contains("message/read")
                            || url.contains("/visit/records")|| url.contains(".png")|| url.contains("collection/find")) {
                        continue;
                    }
                    if(status.contains("404")){
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
                    if(referer.length() > 250){
                        logger.info("refer:{} 长度太长 无法存入数据库,准备进行截取",referer);
                        referer = StringUtils.substring(referer,0,250);
                    }
                    if(url.length() > 250){
                        logger.info("url:{} 长度太长 无法存入数据库,准备进行截取",url);
                        url = StringUtils.substring(url,0,250);
                    }
                    if(method.length() > 250){
                        logger.info("method:{} 长度太长 无法存入数据库,准备进行截取",method);
                        method = StringUtils.substring(method,0,250);
                    }
                    Double requestTimeDouble = 0d;
                    try {
                        requestTimeDouble = Double.valueOf(requestTime);
                    } catch (NumberFormatException e) {
                    }
                    if(userAgent.length() > 254){
                        logger.info("userAgent:{} 长度太长 无法存入数据库,准备进行截取",userAgent.toString());
                        userAgent.delete(254,userAgent.length());
                    }
                    NginxLog log = new NginxLog(ip, localTime, method, url, http, status, bytes, referer, xforward,
                            requestTimeDouble, userAgent.toString().replaceAll("\"", ""),spiderType);
                    log.setCreateTime(now);
                    logService.insert(log);
                }
                NginxLogExample nginxLogExample = new NginxLogExample();
                nginxLogExample.createCriteria().andCreateTimeLessThan(DateUtil.offsetMonth(new Date(),-12));//删除90天之前的记录
                logService.deleteByExample(nginxLogExample);
            }
            //更新7天的最热想法
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            PageHelper.startPage(1, 5 , false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("comment_count desc ");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andCreateTimeGreaterThan(DateUtil.offsetDay(now,-7));
            List<SimThoughtDTO> simThoughtDTOS = Lists.newArrayList();
            List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
            if(CollectionUtil.isNotEmpty(thoughts)){
                thoughts.forEach(t->{
                    SimThoughtDTO simThoughtDTO = new SimThoughtDTO();
                    simThoughtDTO.setId(t.getId());
                    simThoughtDTO.setContent(t.getThoughtContent());
                    if(org.apache.commons.lang.StringUtils.isNotEmpty(t.getPicList())){
                        simThoughtDTO.setPic(t.getPicList().split(",")[0]);
                    }else {
                        simThoughtDTO.setPic("");
                    }
                    simThoughtDTOS.add(simThoughtDTO);
                });
            }
            commands.set(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST, JSON.toJSONString(simThoughtDTOS));
            commands.expire(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST,2*24*60*60);
        }catch (Exception e){
            logger.error("nginx日志分析报错",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("nginx日志分析完毕,耗时{}秒",seconds);
    }

    //每天更新文章
    @Scheduled(cron = "0 0 9 * * ? ")
    public void articleGernerate(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            File file = new File("/home/chenweidong/webmagic/qdfuns.com");
            File[] files =  file.listFiles();
            Date now = new Date();
            int index = 1;
            Pattern pattern = Pattern.compile("<img.*>");
            Pattern pattern2 = Pattern.compile("<svg.*</svg>");
            for(File fileSingle : files){
                String all = FileUtil.readString(fileSingle, "UTF-8");
                JSONObject jsonObject = new JSONObject(all);
                if (jsonObject.isEmpty()) {
                    continue;
                }
                String title = jsonObject.getStr("title");
                JSONArray authors = jsonObject.getJSONArray("author");
                if(authors.isEmpty()){
                    continue;
                }
                String category = authors.get(2).toString();
                title = title +"-"+category;
//                JSONArray commentCounts = jsonObject.getJSONArray("commentCount");
//                String seeCount = commentCounts.get(0).toString();
//                seeCount = seeCount.replace("阅读","");
//                Integer seeCounti = Integer.valueOf(seeCount);
                JSONObject contentJson = jsonObject.getJSONObject("content");
                String content = contentJson.getStr("firstSourceText");
                content = ReUtil.delAll(pattern,content);
                content = ReUtil.delAll(pattern2,content);
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
            if(index1 != -1){
                content = content.substring(0,index1-3)+"</dd>";
            }
            content = ReUtil.delAll(pattern,content);

//            article.setArticleContent(HtmlUtils.htmlUnescape(content));
                article.setArticleContent(content);
                article.setCreateTime(now);
                article.setContentType(2);
                try {
                    articleService.insert(article);
                } catch (Exception e) {
                    logger.error("生成文章报错，文章详情："+article.toString(),e);
                    fileSingle.delete();
                    continue;
                }
                fileSingle.delete();
                if(index > 10){
                    break;
                }
                index++;
            }
        }catch (Exception e){
            logger.error("生成文章报错",e);
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("生成文章完毕,耗时{}秒",seconds);
    }

    //短视频删除 暂时不删除
/* @Scheduled(cron = "1 0 0 * * ? ")
    public void delShortVideos(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        FqShortVideoExample fqShortVideoExample = new FqShortVideoExample();
        fqShortVideoExample.createCriteria().andCreateTimeLessThan(DateUtil.offsetDay(new Date(),-31));//删除31天之前的记录
        List<FqShortVideo> shortVideos = fqShortVideoService.selectByExample(fqShortVideoExample);
        if(CollectionUtil.isNotEmpty(shortVideos)){
            shortVideos.forEach(shortVideo->{
                shortVideo.setDelFlag(YesNoEnum.YES.getValue());
                fqShortVideoService.updateByPrimaryKey(shortVideo);
            });
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("31天前的短视频删除完毕,耗时{}秒",seconds);
    }*/


    //更新昨天的新用户 以及更新下友情链接 以及更新背景图片 插入图片 （如果修改了的话 数据库）
    @Scheduled(cron = "0 1 0 * * ? ")
//    @Scheduled(cron = "0 51 15 * * ? ")
    public void newUserRcord(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        String picLog = "";
        try {
            FqUserExample userExample = new FqUserExample();
            userExample.setOrderByClause("id desc");
            userExample.createCriteria().andStatusEqualTo(UserStatusEnum.NORMAL.getValue());
//        userExample.createCriteria().andCreateTimeBetween(DateUtil.beginOfDay(DateUtil.yesterday()),DateUtil.endOfDay(DateUtil.yesterday()));
            PageHelper.startPage(0,10,false);
            List<FqUser> newUsers = fqUserService.selectByExample(userExample);//昨天的新用户
            List<FqUserSim> sims = Lists.newArrayListWithExpectedSize(newUsers.size());
            for(FqUser fqUser : newUsers){
                FqUserSim sim = new FqUserSim(fqUser);
                sims.add(sim);
            }
            CommonConstant.NEW_SIMPLE_USERS= sims;

            //计算用户数量是
            userExample.clear();
            CommonConstant.FQ_USER_TOTAL_COUNT = fqUserService.countByExample(userExample);

            List<FqFriendLink> fqFriendLinks = fqFriendLinkService.selectByExample(new FqFriendLinkExample());
            CommonConstant.FRIEND_LINK_LIST = fqFriendLinks;

            FqBackgroundImgExample backgroundImgExample = new FqBackgroundImgExample();
            backgroundImgExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(backgroundImgExample);
            CommonConstant.bgImgUrl = fqBackgroundImg == null?"https://img.t.sinajs.cn/t6/skin/skinvip805/images/body_bg.jpg?id=1410943047113":fqBackgroundImg.getImgUrl();


            String picRootPath = "/home/chenweidong/meinv";
            File rootFile = new File(picRootPath);
            logger.info("rootFile文件名：{},能否被读：{}",rootFile.getName(),rootFile.canRead());
            File[] categoryFiles = rootFile.listFiles();
            long currentTimeMillis = System.currentTimeMillis();
            if(categoryFiles != null && categoryFiles.length > 0){
                Date now = new Date();
                for(File categoryFile : categoryFiles){
                    boolean insertPic = false;
                    logger.info("文件名：{},能否被读：{}",categoryFile.getName(),categoryFile.canRead());
                    File[] files = categoryFile.listFiles();
                    if(files == null || files.length == 0){
                        categoryFile.delete();
                        continue;
                    }
                    for(File file : files){
                        logger.info("3文件名：{},能否被读：{}",file.getName(),file.canRead());
                        File[] imgs = file.listFiles();
                        if(imgs != null && imgs.length > 0){
                            String imgUrl = "";
                            String title = "";
                            StringJoiner picUrls = new StringJoiner(",");
                            StringJoiner picDescs = new StringJoiner(",");
                            for(File img : imgs){
                                String imgName = img.getName();
                                if(imgName.contains("(")){
                                    imgName = imgName.substring(0,imgName.indexOf("("));
                                }
                                String picUrl = null;
                                try {
                                    picUrl = FileSystemClient.getPublicClient().upload(CommonConstant.FILE_NAME_PREFIX+currentTimeMillis+".jpg", img);
                                } catch (FSOperErrorException e) {
                                    logger.error("上传图片出错，图片名："+imgName,e);
                                    boolean delete = img.delete();
                                    continue;
                                }
                                picUrl+= CommonConstant.QINIU_PIC_STYLE_NAME;
                                picUrls.add(picUrl);
                                picDescs.add(imgName);
                                imgUrl = picUrl;
                                title = imgName;
                                boolean delete = img.delete();
                                logger.info("img文件名：{}，是否删除成功：{}",imgName,delete);
                                currentTimeMillis++;
                            }
                            if(StringUtils.isEmpty(title)){
                                continue;
                            }
                            title = StrUtil.str(title,Charset.forName("utf-8"));
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
                            file.delete();
                        }
                        if(insertPic){
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("新用户以及友链更新、插入图片报错",e);
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("新用户以及友链更新完毕,完成插入图片，耗时{}秒,图片详情：{}",seconds,picLog);
    }
}
