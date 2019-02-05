package com.feiqu.web.controller.extra2;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqShortVideo;
import com.feiqu.system.model.FqShortVideoExample;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.UserActiveNumResponse;
import com.feiqu.system.service.FqShortVideoService;
import com.feiqu.system.service.FqUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/10/6.
 */
@RequestMapping("/findFriend")
@Controller
public class FindLoveController extends BaseController{
    private final static Logger logger = LoggerFactory.getLogger(FindLoveController.class);
    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private FqShortVideoService fqShortVideoService;

    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                request.setAttribute("redirectSuccessUrl","/findFriend");
                return "/login.html";
            }
            FqShortVideoExample shortVideoExample = new FqShortVideoExample();
            shortVideoExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            shortVideoExample.setOrderByClause("create_time desc");
            List<FqShortVideo> fqShortVideos = fqShortVideoService.selectByExample(shortVideoExample);
            if(CollectionUtils.isEmpty(fqShortVideos)){
                fqShortVideos = Lists.newArrayList();
            }
            model.addAttribute("shortVideos",fqShortVideos);

            int month = DateUtil.thisMonth()+1;
            int lastMonth = month == 1? 12 : month-1;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> userIds =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,4);
            if(CollectionUtils.isNotEmpty(userIds)){
                List<Integer> userIdList = Lists.newArrayList();
                for(String userId : userIds){
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample example = new FqUserExample();
                example.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(example);
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
                model.addAttribute("thisMonthActiveUsers",responses);
            }
            Set<String> userIds2 =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+lastMonth,0,4);
            if(CollectionUtils.isNotEmpty(userIds2)){
                List<Integer> userIdList = Lists.newArrayList();
                for(String userId : userIds2){
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample example = new FqUserExample();
                example.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(example);
                Map<Integer,FqUser> userMap = Maps.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(),fqUser);
                });
                List<UserActiveNumResponse> responses = Lists.newArrayList();
                for(int i = 0;i<userIdList.size();i++){
                    double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+lastMonth,userIdList.get(i).toString());
                    UserActiveNumResponse userActiveNumResponse = new UserActiveNumResponse(userMap.get(userIdList.get(i)),score,i+1);
                    responses.add(userActiveNumResponse);
                }
                model.addAttribute("lastMonthActiveUsers",responses);
            }
        } catch (Exception e) {
            logger.error("寻找报错",e);
            return "/error.html";
        } finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        return "/findLove/index.html";
    }

}
