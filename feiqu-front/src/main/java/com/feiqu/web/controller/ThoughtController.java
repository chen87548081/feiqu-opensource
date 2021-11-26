package com.feiqu.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeesuite.cache.command.RedisString;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */
@Controller
@RequestMapping("/thought")
public class ThoughtController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ThoughtController.class);

    @Autowired
    private ThoughtService thoughtService;
    @Autowired
    private GeneralCommentService commentService;

    @Autowired
    private GeneralLikeService likeService;
    @Autowired
    private CMessageService messageService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqUserService fqUserService;

    @Autowired
    private FqCollectService fqCollectService;

    /*
    手动置顶
     */
    @ResponseBody
    @PostMapping(value = "/handTop/{id}")
    public Object handTop(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(id);
            if(thought == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
            FqUser fqUser = fqUserService.selectByPrimaryKey(thoughtWithUser.getUserId());
            thoughtWithUser.setIcon(fqUser.getIcon());
            thoughtWithUser.setNickname(fqUser.getNickname());
            thoughtWithUser.setUsername(fqUser.getUsername());
            RedisString redisString = new RedisString(CommonConstant.THOUGHT_TOP_LIST);
            if(StringUtils.isEmpty(redisString.get())){
                redisString.set(JSON.toJSONString(thoughtWithUser),24*60*60);
            }else {
                result.setResult(ResultEnum.THOUGHT_TOP_EXIST);
                result.setMessage("置顶的想法已经存在,需要等待"+redisString.getTtl()/60+"分钟");
                return result;
            }
            CommonUtils.addOrDelUserQudouNum(user,-20);
        } catch (Exception e) {
            logger.error("想法置顶失败",e);
            result.setResult(ResultEnum.FAIL);

        }
        return result;
    }

    /*
   手动置顶
    */
    @ResponseBody
    @PostMapping(value = "/handCancelTop/{id}")
    public Object handCancelTop(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(id);
            if(thought == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }

            RedisString redisString = new RedisString(CommonConstant.THOUGHT_TOP_LIST);
            if(StringUtils.isEmpty(redisString.get())){
            }else {
                result.setResult(ResultEnum.THOUGHT_TOP_EXIST);
                result.setMessage("置顶的想法已经存在,需要等待"+redisString.getTtl()/60+"分钟");
                return result;
            }
        } catch (Exception e) {
            logger.error("想法置顶失败",e);
            result.setResult(ResultEnum.FAIL);

        }
        return result;
    }


    @ResponseBody
    @PostMapping(value = "post")
    public Object postThoughts(Thought thought, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        logger.info("postThoughts:发表想法详情：{}",thought.toString());
        try {
            String ip = WebUtil.getIP(request);
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(StringUtils.isBlank(thought.getThoughtContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(thought.getThoughtContent().length() > 400){
                result.setResult(ResultEnum.THOUGHT_TOO_LONG);
                return result;
            }
            String key = "postThought_"+ip+"_"+user.getId();
            RedisString redisString = new RedisString(key);
            String value = redisString.get();
            if(StringUtils.isEmpty(value)){
                redisString.set("1", 60);
            }else {
                long time = redisString.getTtl();
                result.setResult(ResultEnum.POST_THOUGHT_FREQUENCY_OVER_LIMIT);
                result.setMessage("发表想法频率超过限制，请过"+time+"秒再试!");
                return result;
            }
            //todo 校验图片列表
            /*if(StringUtils.isNotEmpty(thought.getPicList())){
            }*/
            String region = CommonUtils.getRegionByIp(ip);
            thought.setArea(region);
            thought.setThoughtContent(EmojiUtils.toAliases(thought.getThoughtContent()));
            thought.setUserId(user.getId());
            thought.setDelFlag(YesNoEnum.NO.getValue());
            thought.setCreateTime(new Date());
            thoughtService.insert(thought);
            thought.setThoughtContent(EmojiUtils.toUnicode(thought.getThoughtContent()));
            result.setData(thought);
            CommonUtils.addActiveNum(user.getId(),ActiveNumEnum.POST_THOUGHT.getValue());
        } catch (Exception e) {
            logger.error("发表想法失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer thoughtId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if(thought == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
            .andTopicIdEqualTo(thoughtId).andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(thoughtId,TopicTypeEnum.THOUGHT_TYPE.getValue(),1,user.getId(),new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);
            thought.setLikeCount(thought.getLikeCount() == null? 1: thought.getLikeCount()+1);
            thoughtService.updateByPrimaryKey(thought);
            if(!thought.getUserId().equals(user.getId())){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(new Date());
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(thought.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                        "赞了你的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId +"\" target=\"_blank\">想法</a> "+ DateUtil.formatDateTime(new Date()));
                messageService.insert(message);
            }
            result.setData(thought.getLikeCount());
            CommonUtils.addActiveNum(user.getId(),ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            logger.error("thought like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
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
            Integer thoughtId = comment.getTopicId();
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if(thought == null || YesNoEnum.YES.getValue().equals(thought.getDelFlag())){
                result.setResult(ResultEnum.FAIL);
                result.setMessage("此想法不存在，或已经删除！");
                return result;
            }
            String commentContent = comment.getContent();
            if(StringUtils.isBlank(commentContent)){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空！");
                return result;
            }
            if(commentContent.length() > 255){
                result.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                result.setMessage("评论内容长度不能超过255！");
                return result;
            }

            String key = "thought_comment_"+thoughtId+"_"+user.getId()+"_"+ip;
            RedisString redisString = new RedisString(key);
            String value = redisString.get();
            if(StringUtils.isEmpty(value)){
                redisString.set("1", 60);
            }else {
                result.setResult(ResultEnum.THOUGHT_COMMENT_FREQUENCY_OVER_LIMIT);
                return result;
            }
            Date now = new Date();
            commentContent = EmojiUtils.toAliases(commentContent);
            String shortCommentContent = "";
            String shortThoughtContent = "";
            if(commentContent.length()>20){
                shortCommentContent = commentContent.substring(0,20)+"...";
            }else {
                shortCommentContent = commentContent;
            }
            if(thought.getThoughtContent().length() > 20){
                shortThoughtContent = thought.getThoughtContent().substring(0,20)+"...";
            }else {
                shortThoughtContent = thought.getThoughtContent();
            }
            int aiteSize = 0;
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if(CollectionUtil.isNotEmpty(aiteNames)){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null){
                        aiteSize++;
                        if(!aiteUser.getId().equals(user.getId())){
                            shortCommentContent = shortCommentContent.replaceAll("@"+aiteNickname,"");
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId +"\" target=\"_blank\">此想法（"+shortThoughtContent+"）</a>中回复了你 :"+shortCommentContent+"-"+ DateUtil.formatDateTime(now));
                            messageService.insert(message);
                        }
                    }
                }
            }
            thought.setCommentCount(thought.getCommentCount() == null ? 1:thought.getCommentCount()+1);
            thought.setLastReplyUserName(user.getNickname());
            thought.setLastReplyTime(DateUtil.formatDateTime(now));
            thoughtService.updateByPrimaryKey(thought);
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.THOUGHT_TYPE.getValue());
            comment.setPostUserId(user.getId());
            comment.setContent(commentContent);
            commentService.insert(comment);

            if(!user.getId().equals(thought.getUserId()) && aiteSize == 0){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(thought.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"/peopleIndex\" target=\"_blank\">"+user.getNickname()+" </a>" +
                        "回复了你的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId + "\" target=\"_blank\">想法（"+shortThoughtContent+"）</a> :"+shortCommentContent+"-"+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
            result.setData(thought.getCommentCount());
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
        } catch (Exception e) {
            logger.error("thought 回复异常",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @GetMapping(value = "/{thoughtId}")
    public String commentList(Model model, @PathVariable Integer thoughtId){
        try {
            FqUserCache fqUserCache = getCurrentUser();
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            FqUser thoughtUser = null;
            if(thought != null){
                thoughtUser = fqUserService.selectByPrimaryKey(thought.getUserId());
                ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
                model.addAttribute("thought",thoughtWithUser);
                model.addAttribute("oUser",thoughtUser);
            }else {
                return "/404";
            }
            //如果想法被删除
            if(YesNoEnum.YES.getValue().equals(thought.getDelFlag())){
                return "/topic-deleted";
            }
            boolean isCollected = false;
            if(fqUserCache != null){
                FqCollectExample fqCollectExample = new FqCollectExample();
                fqCollectExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                        .andUserIdEqualTo(fqUserCache.getId()).andTopicIdEqualTo(thoughtId)
                        .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue());
                FqCollect fqCollect = fqCollectService.selectFirstByExample(fqCollectExample);
                if(fqCollect != null){
                    isCollected = true;
                }
            }
            model.addAttribute("isCollected",isCollected);
            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.setOrderByClause("create_time asc");
            commentExample.createCriteria().andTopicIdEqualTo(thoughtId)
                    .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            model.addAttribute("comments",comments);
            model.addAttribute("count",comments.size());

            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("create_time desc");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                    .andUserIdEqualTo(thought.getUserId())
            .andIdNotEqualTo(thoughtId);
            PageHelper.startPage(1,10,false);
            List<Thought> theirThoughts = thoughtService.selectByExample(thoughtExample);
            model.addAttribute("theirThoughts",theirThoughts);
            int month = DateUtil.thisMonth()+1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,thoughtUser.getId().toString());
            model.addAttribute("activeNum",score == null?0:score.intValue());
        } catch (Exception e) {
            logger.error("thought 详情失败！",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        return "/thought/detail";
    }

    /*
    我的想法
     */
    @GetMapping(value = "/my")
    public String myThoughts(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(defaultValue = "1") Integer page,
                             Model model){
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                return "/login";
            }
            PageHelper.startPage(page,20);
            ThoughtExample example = new ThoughtExample();
            example.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<ThoughtWithUser> thoughts = thoughtService.getThoughtWithUser(example);

            List<Integer> list = fqCollectService.selectTopicIdsByTypeAndUid(TopicTypeEnum.THOUGHT_TYPE.getValue(),user.getId());
            if(list != null && list.size() > 0){
                try {
                    JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
                    String key = CacheManager.getCollectKey(TopicTypeEnum.THOUGHT_TYPE.name(),user.getId());
                    long redisCount = commands.scard(key);
                    if(redisCount == 0){
                        for(Integer tid : list){
                            commands.sadd(key, tid.toString());
                        }
                        commands.expire(key,24*60*60);
                    }
                    for(ThoughtWithUser thoughtWithUser : thoughts){
                        thoughtWithUser.setCollected(commands.sismember(key,thoughtWithUser.getId().toString()));
                    }
                } finally{
                    JedisProviderFactory.getJedisProvider(null).release();
                }
            }

            PageInfo pageInfo = new PageInfo(thoughts);
            model.addAttribute("thoughtList",thoughts);
            model.addAttribute("count",pageInfo.getTotal());
            model.addAttribute("p",page);
            model.addAttribute("pageSize",20);
        } catch (Exception e) {
            logger.error("thought 获取我的想法失败！",e);
        }
        return "/thought/thoughts";
    }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(Integer thoughtId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if(thought != null){
                if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())){
                    thought.setDelFlag(YesNoEnum.YES.getValue());
                    thoughtService.updateByPrimaryKeySelective(thought);
                }else {
                    if(user.getId().equals(thought.getUserId())){
                        thought.setDelFlag(YesNoEnum.YES.getValue());
                        thoughtService.updateByPrimaryKeySelective(thought);
                    }else {
                        result.setResult(ResultEnum.DELETE_NOT_MY);
                    }
                }
            }else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("thought delete error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("/collect/{type}")
    @ResponseBody
    public Object collect(@PathVariable String type, Integer tid, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thoughtDB = thoughtService.selectByPrimaryKey(tid);
            if(thoughtDB == null){
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(tid)
                    .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if("add".equals(type)){
                if(fqCollect == null){
                    fqCollect = new FqCollect();
                    fqCollect.setTopicId(tid);
                    fqCollect.setTopicType(TopicTypeEnum.THOUGHT_TYPE.getValue());
                    fqCollect.setCreateTime(new Date());
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollect.setUserId(fqUser.getId());
                    fqCollectService.insert(fqCollect);
                    CacheManager.addCollect(TopicTypeEnum.THOUGHT_TYPE.name(),fqUser.getId(),tid);
                }else {
                    if(!YesNoEnum.NO.getValue().equals(fqCollect.getDelFlag())){
                        fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                        fqCollectService.updateByPrimaryKey(fqCollect);
                    }
                    CacheManager.addCollect(TopicTypeEnum.THOUGHT_TYPE.name(),fqUser.getId(),tid);
                }
            }else if("remove".equals(type)){
                if(fqCollect == null){
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                }else {
                    fqCollect.setDelFlag(YesNoEnum.YES.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                    CacheManager.removeCollect(TopicTypeEnum.THOUGHT_TYPE.name(),fqUser.getId(),tid);
                }
            }else {
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            result.setData(fqCollect.getId());
        } catch (Exception e) {
            logger.error("thought collect error",e);
        }
        return result;
    }
}
