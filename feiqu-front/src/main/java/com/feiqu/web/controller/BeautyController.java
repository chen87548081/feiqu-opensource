package com.feiqu.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
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
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.pojo.simple.BeautySim;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.jeesuite.cache.command.RedisString;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author cwd
 * @create 2017-10-9:30
 **/
@Controller
@RequestMapping("beauty")
public class BeautyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BeautyController.class);

    @Autowired
    private SuperBeautyService superBeautyService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private CMessageService messageService;
    @Autowired
    private GeneralLikeService likeService;
    @Autowired
    private GeneralCommentService commentService;
    @Autowired
    private FqUserService fqUserService;

    @GetMapping("/manage")
    public String manage(Model model){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(fqUserCache.getRole() != 1){
            model.addAttribute("errorMsg","用户权限不足！");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/beauty/manage";
    }
    @GetMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page,Integer limit){
        BaseResult result = new BaseResult();
        try {
            if(limit >20 ){
                limit = 20;
            }
            PageHelper.startPage(page,limit);
            SuperBeautyExample example = new SuperBeautyExample();
            example.setOrderByClause("create_time desc");
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<SuperBeauty> beautyList = superBeautyService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(beautyList);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("图片分页出错",e);
            result.setCode("1");
            result.setMessage("图片分页出错");
        }
        return result;
    }

    @PostMapping("/manage/addOrDelBanner")
    @ResponseBody
    public Object addOrDelBanner(BeautySim beautySim,String type){
        BaseResult result = new BaseResult();
        try {
            logger.info("banner新增或者删除图片：{}，类型：{}",JSON.toJSON(beautySim),type);
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            if("add".equals(type)){
                boolean exist = CommonConstant.BEAUTY_BANNERS.stream().anyMatch(e->e.getImgUrl().equals(beautySim.getImgUrl()));
                if(exist){
                    result.setCode("1");
                    result.setMessage("Banner已经存在此照片！");
                    return result;
                }else {
                    beautySim.setHref(beautySim.getHref());
                    CommonConstant.BEAUTY_BANNERS.add(beautySim);
                    commands.set(CommonConstant.BEAUTY_BANNERS_REDIS, JSON.toJSONString(CommonConstant.BEAUTY_BANNERS));
                }
            }else {
                CommonConstant.BEAUTY_BANNERS = CommonConstant.BEAUTY_BANNERS.stream().filter(e->!beautySim.getImgUrl().equals(e.getImgUrl())).collect(Collectors.toList());
                commands.set(CommonConstant.BEAUTY_BANNERS_REDIS, JSON.toJSONString(CommonConstant.BEAUTY_BANNERS));
            }
        } catch (Exception e) {
            logger.error("加至banner出错",e);
            result.setCode("1");
            result.setMessage("加至banner出错");
        } finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("删除图片：用户id：{}，图片id：{}",fqUserCache.getId(),id);
            SuperBeauty superBeauty = superBeautyService.selectByPrimaryKey(id);
            Assert.notNull(superBeauty,"图片不能为空！");
            SuperBeauty superBeautyToUpdate = new SuperBeauty();
            superBeautyToUpdate.setId(id);
            superBeautyToUpdate.setDelFlag(YesNoEnum.YES.getValue());
            superBeautyService.updateByPrimaryKeySelective(superBeautyToUpdate);
        } catch (Exception e) {
            logger.error("删除图片出错",e);
            result.setCode("1");
            result.setMessage("删除图片出错");
        }
        return result;
    }

    @RequestMapping(value = "index")
    public String superBeauty(Model model, @RequestParam(defaultValue = "0") Integer pageIndex,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              String order){
        return category("",model,pageIndex,pageSize,order);
    }

    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model, @RequestParam(defaultValue = "0") Integer pageIndex,
                           @RequestParam(defaultValue = "10") Integer pageSize, String order){
        try {
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                return USER_LOGIN_REDIRECT_URL;
            }
            //暂时只支持男生看 因为我老婆看了感觉不正经 所以关闭女性观看的权限
            if(fqUser.getSex() == null || SexEnum.GIRL.getValue().equals(fqUser.getSex())){
                model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE,"暂时只支持男生看");
                return GENERAL_CUSTOM_ERROR_URL;
            }
            if(pageSize > 20){
                pageSize = 20;
            }
            PageHelper.startPage(pageIndex, pageSize);
            SuperBeautyExample example = new SuperBeautyExample();
            if("zan".equals(order)){
                example.setOrderByClause("like_count desc ");
            }else {
                example.setOrderByClause("create_time desc");
            }
            if(StringUtils.isEmpty(category)){
                example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            }else {
                example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andCategoryEqualTo(category);
            }
            List<BeautyUserResponse> beautyUserResponses = superBeautyService.selectDetailByExample(example);
            PageInfo page = new PageInfo(beautyUserResponses);
            model.addAttribute("list",beautyUserResponses);
            model.addAttribute("girlCategories",CommonConstant.CATEGORIES);
            model.addAttribute("pageIndex",pageIndex);
            model.addAttribute("pageSize",pageSize);
            model.addAttribute("count",page.getTotal());
            model.addAttribute("category",category);
            model.addAttribute("order",order);

            PageHelper.startPage(1,5,false);
            example.clear();
            example.setOrderByClause("see_count desc");
            List<SuperBeauty> beautyList = superBeautyService.selectByExample(example);
            model.addAttribute("recommendList",beautyList);
        } catch (Exception e) {
            logger.error("beauty category error",e);
            model.addAttribute(CommonConstant.SYSTEM_ERROR_CODE,"出错了");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/superBeauty";
    }

    @GetMapping("{beautyId}")
    public Object detail(@PathVariable Integer beautyId, Model model){
        try {
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                return USER_LOGIN_REDIRECT_URL;
            }
            //暂时只支持男生看 因为我老婆看了感觉不正经 所以关闭女性观看的权限
            if(fqUser.getSex() == null || SexEnum.GIRL.getValue().equals(fqUser.getSex())){
                model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE,"暂时只支持男生看");
                return GENERAL_CUSTOM_ERROR_URL;
            }
            SuperBeautyExample example = new SuperBeautyExample();
            example.createCriteria().andIdEqualTo(beautyId);
            List<BeautyUserResponse> beautyUserResponses = superBeautyService.selectDetailById(beautyId);
            if(CollectionUtil.isNotEmpty(beautyUserResponses)){
                SuperBeauty superBeauty = beautyUserResponses.get(0);
                SuperBeauty superBeautyToupdate = new SuperBeauty();
                superBeautyToupdate.setId(superBeauty.getId());
                superBeautyToupdate.setSeeCount(superBeauty.getSeeCount() == null?1:superBeauty.getSeeCount() + 1);
                superBeautyService.updateByPrimaryKeySelective(superBeautyToupdate);
                model.addAttribute("beauty",beautyUserResponses.get(0));
                String picList = superBeauty.getPicList();
                String picDescList = superBeauty.getPicDescList();
                List<KeyValue> list = Lists.newArrayList();
                if(picList != null){
                    String[] picListStr = picList.split(",");
                    String[] picDescStr = picDescList.split(",");

                    for(int i = 0;i<picListStr.length;i++){
                        list.add(new KeyValue(picDescStr[i],picListStr[i]));
                    }
                }
                model.addAttribute("picList",list);
                GeneralCommentExample commentExample = new GeneralCommentExample();
                commentExample.createCriteria().andTopicIdEqualTo(beautyId).andTopicTypeEqualTo(TopicTypeEnum.PIC_TYPE.getValue());
                List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
                model.addAttribute("comments",comments);
                model.addAttribute("commentsSize",comments.size());
                /*if(!fqUser.getId().equals(superBeauty.getUploadUserId())){
                    CommonUtils.addOrDelUserQudouNum(fqUser,-10);
                }*/
            }else {
                return GENERAL_NOT_FOUNF_404_URL;
            }

        } catch (Exception e) {
            logger.error("beauty detail error",e);
            model.addAttribute(CommonConstant.SYSTEM_ERROR_CODE,"出错了");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/beauty/detail";
    }

    @ResponseBody
    @PostMapping(value = "uploadImg")
    public Object UploadImg(SuperBeauty beauty, HttpServletRequest request, HttpServletResponse response){
        BaseResult baseResult = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
                return baseResult;
            }
            String picUrl = beauty.getImgUrl();
            if(StringUtils.isBlank(picUrl)|| StringUtils.isBlank(beauty.getTitle())|| StringUtils.isBlank(beauty.getCategory())){
                baseResult.setResult(ResultEnum.PARAM_NULL);
                return baseResult;
            }
            if(beauty.getTitle().length() > 50){
                baseResult.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                return baseResult;
            }
            String picExt = FileUtil.extName(picUrl);
            List<String> picExtList = Arrays.asList("jpg","png","gif","bmp","jpeg");
            if(picExtList.contains(picExt)){
                if(picUrl.contains(CommonConstant.FILE_NAME_PREFIX)){
                    picUrl+=CommonConstant.QINIU_PIC_STYLE_NAME;
                }
            }
            SuperBeautyExample example = new SuperBeautyExample();
            example.createCriteria().andImgUrlEqualTo(picUrl).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            SuperBeauty beautyDB = superBeautyService.selectFirstByExample(example);
            if(beautyDB != null){
                baseResult.setResult(ResultEnum.PIC_URL_ALREADY_EXIST);
                return baseResult;
            }
            beauty.setCreateTime(new Date());
            beauty.setUploadUserId(fqUser.getId());
            beauty.setDelFlag(YesNoEnum.NO.getValue());
            beauty.setPicList(picUrl);
            beauty.setPicDescList(beauty.getTitle());
            superBeautyService.insert(beauty);
            baseResult.setResult(ResultEnum.SUCCESS);
            baseResult.setData(beauty);
        } catch (Exception e) {
            logger.error("uploadImg error",e);
            baseResult.setResult(ResultEnum.FAIL);
        }
        return baseResult;
    }

    @ResponseBody
    @PostMapping(value = "joinImg")
    public Object joinImg(SuperBeauty beauty, HttpServletRequest request, HttpServletResponse response){
        BaseResult baseResult = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
                return baseResult;
            }
            String picUrl = beauty.getImgUrl();
            String title = beauty.getTitle();
            if(StringUtils.isBlank(picUrl) || StringUtils.isBlank(title) || beauty.getId() == null){
                baseResult.setResult(ResultEnum.PARAM_NULL);
                return baseResult;
            }
            SuperBeauty superBeautyDB = superBeautyService.selectByPrimaryKey(beauty.getId());
            if(superBeautyDB == null){
                baseResult.setResult(ResultEnum.PARAM_NULL);
                return baseResult;
            }
            superBeautyDB.setPicList(StringUtils.isEmpty(superBeautyDB.getPicList())?picUrl:superBeautyDB.getPicList()+","+picUrl);
            superBeautyDB.setPicDescList(StringUtils.isEmpty(superBeautyDB.getPicDescList())?title:superBeautyDB.getPicDescList()+","+title);
            superBeautyService.updateByPrimaryKey(superBeautyDB);
            baseResult.setResult(ResultEnum.SUCCESS);
            baseResult.setData(superBeautyDB);
        } catch (Exception e) {
            logger.error("joinImg error",e);
            baseResult.setResult(ResultEnum.FAIL);
        }
        return baseResult;
    }

    @ResponseBody
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public Object thoughtComment(GeneralComment comment, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            String ip = WebUtil.getIP(request);
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Integer beautyId = comment.getTopicId();
            SuperBeauty superBeauty = superBeautyService.selectByPrimaryKey(beautyId);
            if(superBeauty == null || YesNoEnum.YES.getValue().equals(superBeauty.getDelFlag())){
                result.setResult(ResultEnum.FAIL);
                result.setMessage("此图片不存在，或已经删除！");
                return result;
            }
            String commentContent = comment.getContent();
            if(StringUtils.isBlank(commentContent)){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空！");
                return result;
            }

            String key = "beauty_comment_"+beautyId+"_"+user.getId()+"_"+ip;
            RedisString redisString = new RedisString(key);
            String value = redisString.get();
            if(StringUtils.isEmpty(value)){
                redisString.set("1", 60);
            }else {
                result.setResult(ResultEnum.THOUGHT_COMMENT_FREQUENCY_OVER_LIMIT);
                result.setMessage("频率超过限制");
                return result;
            }
            Date now = new Date();
            commentContent = EmojiUtils.toAliases(commentContent);
            int aiteSize = 0;
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if(aiteNames.size() > 0){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null){
                        aiteSize++;
                        if(!aiteUser.getId().equals(user.getId())){
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/beauty/" + beautyId +"\" target=\"_blank\">此图片</a>中回复了你 -"+ DateUtil.formatDateTime(now));
                            messageService.insert(message);
                        }
                    }
                }
            }
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.PIC_TYPE.getValue());
            comment.setPostUserId(user.getId());
            comment.setContent(commentContent);
            commentService.insert(comment);

            if(!user.getId().equals(superBeauty.getUploadUserId()) && aiteSize == 0){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(superBeauty.getUploadUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a>" +
                        "回复了你上传的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/beauty/" + beautyId + "\" target=\"_blank\">图片</a> "+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
        } catch (Exception e) {
            logger.error("图片回复异常",e);
        }
        return result;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer beautyId, HttpServletRequest request, HttpServletResponse response, Boolean unpraise){
        BaseResult result = new BaseResult();
        try {
            unpraise = unpraise==null?false:unpraise;
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andPostUserIdEqualTo(user.getId())
                    .andLikeValueEqualTo(YesNoEnum.YES.getValue()).andTopicIdEqualTo(beautyId)
                    .andTopicTypeEqualTo(TopicTypeEnum.PIC_TYPE.getValue());
            GeneralLike generalLike = likeService.selectFirstByExample(likeExample);
            SuperBeauty superBeauty = superBeautyService.selectByPrimaryKey(beautyId);
            if(superBeauty != null){
                if(unpraise){
                    superBeauty.setLikeCount(superBeauty.getLikeCount() -1 );
                }else {
                    if(generalLike == null){
                        generalLike = new GeneralLike(beautyId,TopicTypeEnum.PIC_TYPE.getValue(),YesNoEnum.YES.getValue(),
                                user.getId(),new Date(),YesNoEnum.NO.getValue());
                        likeService.insert(generalLike);
                        superBeauty.setLikeCount(superBeauty.getLikeCount()==null? 1: superBeauty.getLikeCount() +1 );
                    }else {
                        if(generalLike.getLikeValue() == 1){
                            result.setResult(ResultEnum.USER_ALREADY_LIKE);
                            return result;
                        }else {
                            generalLike.setLikeValue(1);
                            likeService.updateByPrimaryKey(generalLike);
                        }
                    }
                }
                superBeautyService.updateByPrimaryKey(superBeauty);
                result.setData(superBeauty.getLikeCount());
            }else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("beauty like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /*@GetMapping("/collect/{category}")
    @ResponseBody
    public Object collect(Integer page,@PathVariable String category){
        BaseResult ajaxresult = new BaseResult();
        List<String> fileNames = FileUtil.listFileNames("D:\\cheninfo\\mmp");
        Map<String,List<String>> map = Maps.newHashMap();
        File parent = new File("D:\\cheninfo\\mmp");
        Date now =new Date();
        int index = 1;
        long currentTimeMillis = System.currentTimeMillis();
        for(String fileName : fileNames){
            File file = FileUtil.file(parent,fileName);
            System.out.println(file.getAbsolutePath());
            fileName =  fileName.replaceAll("\\d{3,4}","");
            fileName =  fileName.replaceAll(".jpg","");
            fileName =  fileName.replaceAll("尤果网","");
            fileName =  fileName.replaceAll("尤果","");
            fileName =  fileName.replaceAll("秀人网","");

            for(KeyValue keyValue : CommonConstant.CATEGORIES){
                if(fileName.contains(keyValue.getValue())){
                    SuperBeauty superBeauty = new SuperBeauty();
                    superBeauty.setDelFlag(YesNoEnum.NO.getValue());
                    superBeauty.setCategory(keyValue.getKey());
                    superBeauty.setUploadUserId(22);
                    superBeauty.setCreateTime(now);
                    superBeauty.setLikeCount(0);
                    superBeauty.setTitle(fileName);
                    String picUrl = provider.upload(CommonConstant.FILE_NAME_PREFIX+currentTimeMillis+".jpg", file);;
                    picUrl+=CommonConstant.QINIU_PIC_STYLE_NAME;
                    superBeauty.setImgUrl(picUrl);
                    superBeauty.setPicList(picUrl);
                    superBeauty.setPicDescList(fileName);
                    superBeautyService.insert(superBeauty);
                    break;
                }
            }
            System.out.println(fileName);
            index++;
            currentTimeMillis++;
            if(index > 1000){
                break;
            }
        }*/

/*
        File dir = new File( "d:\\pictures\\" );
        if(!dir.exists()){
            dir.mkdir();
        }
       String title_regex2 = "[\u4e00-\u9fa5\\w\\-]*[\u4e00-\u9fa5][\u4e00-\u9fa5\\w\\-]*";
       Pattern title_pattern2 = Pattern.compile( title_regex2 );
        String[] indexname = {"xgmn", "swmn", "hgmn", "wgmv", "bjnmn", "nymn",
                "qcmn", "ctmn", "mnmx", "jpmn",};
        *//*int[] pages = {169, 53, 23, 51, 33, 59, 80, 28, 90, 30};
        int no;
        String url_str = "http://www.mmonly.cc/mmtp/";
        for( int i = 0; i < indexname.length; i++ ){
            String index = indexname[i];
            String url = url_str + index + "/";
            no = 10 + i;
//            File dir_file = new File( dir, index );
            url+="list_" + no + "_" + 1 + ".html";
            String result = HttpUtil.get("http://www.mmonly.cc/mmtp/list_9_"+page+".html","GBK");
        }*//*
//        list_10_2

//        String result = HttpUtil.get("http://www.mmonly.cc/mmtp/list_9_"+page+".html","GBK");
        //http://www.xingyun.cn/yanlist/

        String htmlUrl = "http://www.mmonly.cc/mmtp/"+category+"/list_13_"+page+".html";
        logger.info(htmlUrl);

        String result = HttpUtil.get(htmlUrl,"GBK");
        String title_regex = "alt=\"[\\u4E00-\\u9FA5\\w\\s\\-]+\"\\ssrc=\"";
        File file = new File("d://pictures//"+category+"-"+page+".txt");
        try {
            FileUtil.writeBytes(result.getBytes("GBK"),file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<String> list = FileUtil.readLines(file,"GBK");
        Pattern imgpattern = Pattern.compile("http://t1\\.mmonly\\.cc/uploads/.+\\.jpg");
        Pattern titlepattern = Pattern.compile(title_regex);

        List<String> titles = Lists.newLinkedList();
        List<String> imgUrlList = Lists.newLinkedList();

        for(int i = 0;i<list.size();i++){
            if(i < 52){
                continue;
            }
            String line = list.get(i);
            Matcher matcher = titlepattern.matcher(line );
            if( matcher.find() ){
                Matcher title_matcher2 = title_pattern2
                        .matcher( matcher.group() );
                if( title_matcher2.find() )
                {
                    titles.add(title_matcher2.group());
                }
            }
            matcher = imgpattern.matcher(line);
            if( matcher.find() ){
                imgUrlList.add(StringUtils.substring(matcher.group(),0,matcher.group().indexOf("jpg")+3));
            }
        }
        for(int i =0;i<titles.size();i++){
            System.out.println(titles.get(i)+"||" +imgUrlList.get(i));
            SuperBeauty beauty = new SuperBeauty();
            beauty.setCreateTime(new Date());
            beauty.setUploadUserId(22);
            beauty.setImgUrl(imgUrlList.get(i));
            beauty.setTitle(titles.get(i));
            beauty.setCategory(category+"-相册"+(page*10+i));
            superBeautyService.insert(beauty);
        }*/

     /*   return ajaxresult;
    }*/

    @GetMapping("/downloadimg/{category}")
    @ResponseBody
    public Object download(@PathVariable String category){
        BaseResult result = new BaseResult();
        SuperBeautyExample superBeautyExample = new SuperBeautyExample();
        superBeautyExample.createCriteria().andCreateTimeGreaterThan(DateUtil.beginOfDay(new Date()));
        List<SuperBeauty> list = superBeautyService.selectByExample(superBeautyExample);
        int index = 0;
        for(SuperBeauty superBeauty : list){
            index++;

            String usergent = CommonConstant.userAgentArray[new Random().nextInt(CommonConstant.userAgentArray.length)];
            /*InputStream inputStream = HttpRequest.get(superBeauty.getImgUrl()).header(Header.USER_AGENT,usergent)
            .execute().bodyStream();
            File file = new File("d://pictures//test//"+ superBeauty.getTitle() +".jpg");
            FileUtil.writeFromStream(inputStream,file);*/

            URL url = null;
            try {
                url = new URL(superBeauty.getImgUrl());

            // 打开连接
            URLConnection con = url.openConnection();
                //设置请求超时为5s
                con.setConnectTimeout(5*1000);
                con.addRequestProperty("User-Agent", usergent);
                con.addRequestProperty("referer", "http://www.mmonly.cc/mmtp/bjnmn/211982.html");
                // 输入流
                InputStream is = con.getInputStream();

                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                File sf=new File("d://pictures//"+category);
                if(!sf.exists()){
                    sf.mkdirs();
                }
                OutputStream os = new FileOutputStream(sf.getPath()+"\\"+superBeauty.getTitle() +".jpg");
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            HttpClientUtil.downloadFile(superBeauty.getImgUrl(),"d://pictures//",index++ +".jpg",true);



            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            HttpUtil.downloadFile(superBeauty.getImgUrl(),new File("d://pictures//"+ superBeauty.getTitle() +".jpg"));
        }
        return result;
    }

    /*@GetMapping("/dealLocalImg")
    @ResponseBody
    public Object dealLocalImg(@RequestParam(required = false) String path){
        BaseResult result = new BaseResult();
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean insertPic = false;
        String picRootPath = StringUtils.isEmpty(path)?"/home/chenweidong/meinv":path.replaceAll("-",File.separator);
        File rootFile = new File(picRootPath);
        String fileEncoding = System.getProperty("file.encoding");
        logger.info("rootFile文件名：{},能否被读：{},fileEncoding:{}",rootFile.getName(),rootFile.canRead(),fileEncoding);
        File[] categoryFiles = rootFile.listFiles();
        long currentTimeMillis = System.currentTimeMillis();
        if(categoryFiles != null && categoryFiles.length > 0){
            Date now = new Date();
            for(File categoryFile : categoryFiles){
                logger.info("文件名：{},能否被读：{}",categoryFile.getName(),categoryFile.canRead());
                File[] files = categoryFile.listFiles();
                if(files == null || files.length == 0){
                    categoryFile.delete();
                    continue;
                }
                for(File file : files){
                    logger.info("imgs文件名：{},能否被读：{}",file.getName(),file.canRead());
                    File[] imgs = file.listFiles();
                    if(imgs != null && imgs.length > 0){
                        String imgUrl = "";
                        String title = "";
                        StringJoiner picUrls = new StringJoiner(",");
                        StringJoiner picDescs = new StringJoiner(",");
                        for(File img : imgs){
                            logger.info("img文件名：{}",img.getName());
                            String imgName = img.getName();
                            if(imgName.contains("(")){
                                imgName = imgName.substring(0,imgName.indexOf("("));
                            }
                            String picUrl = provider.upload(CommonConstant.FILE_NAME_PREFIX+currentTimeMillis+".jpg", img);
                            picUrl+= CommonConstant.QINIU_PIC_STYLE_NAME;
                            picUrls.add(picUrl);
                            picDescs.add(imgName);
                            imgUrl = picUrl;
                            title = imgName;
                            boolean delete = img.delete();
                            logger.info("img文件名：{},是否删除成功：{}",img.getName(),delete);
                            currentTimeMillis++;
                        }
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
                        insertPic = true;
                        file.delete();
                    }
                    if(insertPic){
                        break;
                    }
                }
                if(insertPic){
                    break;
                }
            }
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("耗时{}秒,",seconds);
        return result;
    }*/
}
