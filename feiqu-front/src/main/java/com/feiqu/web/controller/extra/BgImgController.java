package com.feiqu.web.controller.extra;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqBackgroundImgService;
import com.feiqu.system.service.FqUserService;
import com.jeesuite.cache.command.RedisString;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */
@Controller
@RequestMapping("bgImg")
public class BgImgController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(BgImgController.class);
    @Autowired
    private FqBackgroundImgService fqBackgroundImgService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private CMessageService messageService;

    @GetMapping("update")
    @ResponseBody
    public Object update(FqBackgroundImg backgroundImg, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache fqUser = webUtil.currentUser(request,response);
        if(fqUser == null){
            result.setResult(ResultEnum.FAIL);
            return result;
        }
        if(!fqUser.getRole().equals(UserRoleEnum.ADMIN_USER_ROLE.getValue())){
            result.setResult(ResultEnum.FAIL);
            return result;
        }
        FqBackgroundImgExample example = new FqBackgroundImgExample();
        FqBackgroundImg fqBackgroundImgDB = fqBackgroundImgService.selectFirstByExample(example);
        fqBackgroundImgDB.setUpdateTime(new Date());
        if(StringUtils.isEmpty(fqBackgroundImgDB.getHistoryUrls())){
            fqBackgroundImgDB.setHistoryUrls(backgroundImg.getImgUrl());
        }else {
            if(!fqBackgroundImgDB.getHistoryUrls().contains(backgroundImg.getImgUrl())){
                fqBackgroundImgDB.setHistoryUrls(fqBackgroundImgDB.getHistoryUrls() +","+backgroundImg.getImgUrl());
            }
        }
        fqBackgroundImgDB.setImgUrl(backgroundImg.getImgUrl());
        fqBackgroundImgService.updateByPrimaryKey(fqBackgroundImgDB);
        return result;
    }

    @GetMapping("change")
    public String change(HttpServletRequest request, HttpServletResponse response, Model model){
        FqUserCache fqUser = webUtil.currentUser(request,response);
        if(fqUser == null){
            return USER_LOGIN_REDIRECT_URL;
        }

        FqBackgroundImgExample example = new FqBackgroundImgExample();
        example.createCriteria().andUserIdEqualTo(-1);
        List<FqBackgroundImg> imgList = fqBackgroundImgService.selectByExample(example);
        model.addAttribute("imgList",imgList);
        return "/backImg/change.html";
    }

     @PostMapping("update")
     @ResponseBody
     public Object update(HttpServletRequest request, HttpServletResponse response, String picUrl){
        BaseResult result = new BaseResult();
        try {
             FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(StringUtils.isEmpty(picUrl)){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            FqBackgroundImgExample example = new FqBackgroundImgExample();
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andUserIdEqualTo(fqUser.getId());
             FqBackgroundImg fqBackgroundImgDB = fqBackgroundImgService.selectFirstByExample(example);
             Date now = new Date();
            if(fqBackgroundImgDB == null){
                FqBackgroundImg backgroundImg = new FqBackgroundImg();
                backgroundImg.setImgUrl(picUrl);
                backgroundImg.setDelFlag(YesNoEnum.NO.getValue());
                backgroundImg.setUserId(fqUser.getId());
                backgroundImg.setCreateTime(now);
                backgroundImg.setUpdateTime(now);
                backgroundImg.setHistoryUrls("");
                fqBackgroundImgService.insert(backgroundImg);
            }else {
                if(picUrl.equals(fqBackgroundImgDB.getImgUrl())){
                    result.setResult(ResultEnum.PIC_URL_SAME);
                    return result;
                }
                fqBackgroundImgDB.setUpdateTime(new Date());
                if(StringUtils.isEmpty(fqBackgroundImgDB.getHistoryUrls())){
                    fqBackgroundImgDB.setHistoryUrls(picUrl);
                }else {
                    if(!fqBackgroundImgDB.getHistoryUrls().contains(picUrl)){
                        fqBackgroundImgDB.setHistoryUrls(fqBackgroundImgDB.getHistoryUrls() +","+picUrl);
                    }
                }
                fqBackgroundImgDB.setImgUrl(picUrl);
                fqBackgroundImgService.updateByPrimaryKey(fqBackgroundImgDB);
            }

            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
             String key = CacheManager.getUserBackImgKey(fqUser.getId());
             commands.set(key,picUrl);
             commands.expire(key,60*60*24);
             CommonUtils.addActiveNum(fqUser.getId(),ActiveNumEnum.UPDATE_BG_IMG.getValue());
         } catch (Exception e){
             logger.error("更新背景图片失败！",e);
             result.setResult(ResultEnum.FAIL);
         }finally{
             JedisProviderFactory.getJedisProvider(null).release();
         }
         result.setData(picUrl);
         return result;
    }

     @PostMapping("recommend")
     @ResponseBody
     public Object recommend(HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
             FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqBackgroundImgExample example = new FqBackgroundImgExample();
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqBackgroundImg fqBackgroundImgDB = fqBackgroundImgService.selectFirstByExample(example);
            if(fqBackgroundImgDB == null){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("你还未上传过背景图片");
                return result;
            }else {
                String imgUrl = fqBackgroundImgDB.getImgUrl();
                example.clear();
                example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andImgUrlEqualTo(imgUrl).andUserIdEqualTo(-1);
                FqBackgroundImg temp = fqBackgroundImgService.selectFirstByExample(example);
                if(temp != null){
                    result.setResult(ResultEnum.PARAM_NULL);
                    result.setMessage("该图片已经在官方列表当中");
                    return result;
                }
                String key = "recommendBgImg_"+fqUser.getId();
                RedisString redisString = new RedisString(key);
                String value = redisString.get();
                if(org.apache.commons.lang.StringUtils.isEmpty(value)){
                    redisString.set("1", 60);
                }else {
                    long time = redisString.getTtl();
                    result.setResult(ResultEnum.POST_THOUGHT_FREQUENCY_OVER_LIMIT);
                    result.setMessage("推荐背景图片频率超过限制，请过"+time+"秒再试!");
                    return result;
                }
                Date now = new Date();
                FqUserExample fqUserExample = new FqUserExample();
                fqUserExample.createCriteria().andRoleEqualTo(UserRoleEnum.ADMIN_USER_ROLE.getValue());
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                if(CollectionUtils.isNotEmpty(fqUsers)){
                    fqUsers.forEach(user->{
                        CMessage message = new CMessage();
                        message.setPostUserId(-1);
                        message.setCreateTime(now);
                        message.setDelFlag(YesNoEnum.NO.getValue());
                        message.setReceivedUserId(user.getId());
                        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                        message.setContent("系统消息通知："+fqUser.getNickname()+"("+fqUser.getId()+")向您推荐了这幅背景图片：" +
                                "<img src=\""+fqBackgroundImgDB.getImgUrl()+"\"/><br>"+ DateUtil.formatDateTime(now));
                        messageService.insert(message);
                    });
                }
            }
         } catch (Exception e){
             logger.error("更新背景图片失败！",e);
             result.setResult(ResultEnum.FAIL);
         }
         return result;
    }


}
