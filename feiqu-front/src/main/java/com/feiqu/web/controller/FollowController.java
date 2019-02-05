package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.UserFollow;
import com.feiqu.system.model.UserFollowExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FollowUserResponse;
import com.feiqu.system.service.FqUserService;
import com.feiqu.system.service.UserFollowService;
import com.feiqu.framwork.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/5.
 */
@Controller
@RequestMapping("follow")
public class FollowController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FollowController.class);
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqUserService userService;

    @PostMapping("/{followedUserId}/followers")
    @ResponseBody
    public Object follow(@PathVariable Integer followedUserId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            result.setResult(ResultEnum.USER_NOT_LOGIN);
            return result;
        }
        FqUser oUser = userService.selectByPrimaryKey(followedUserId);
        if(oUser == null){
            result.setResult(ResultEnum.FOLLOWED_USER_NOT_EXIST);
            return result;
        }
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andFollowerUserIdEqualTo(user.getId()).andFollowedUserIdEqualTo(followedUserId)
                .andDelFlagEqualTo(YesNoEnum.YES.getValue());
        UserFollow deluserFollow = userFollowService.selectFirstByExample(example);
        if(deluserFollow == null){
            deluserFollow = new UserFollow(user.getId(),followedUserId,new Date(), YesNoEnum.NO.getValue());
            userFollowService.insert(deluserFollow);
        }else {
            deluserFollow.setDelFlag(YesNoEnum.NO.getValue());
            userFollowService.updateByPrimaryKeySelective(deluserFollow);
        }
        logger.info("{}关注{}成功",user.getId(),oUser.getId());
        return result;
    }

    /**
     * 取消关注
     * @return
     */
    @DeleteMapping("/{followedUserId}/followers")
    @ResponseBody
    public Object unfollow(@PathVariable Integer followedUserId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            result.setResult(ResultEnum.USER_NOT_LOGIN);
            return result;
        }
        FqUser oUser = userService.selectByPrimaryKey(followedUserId);
        if(oUser == null){
            result.setResult(ResultEnum.FOLLOWED_USER_NOT_EXIST);
            return result;
        }
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andFollowerUserIdEqualTo(user.getId()).andFollowedUserIdEqualTo(followedUserId)
                .andDelFlagEqualTo(YesNoEnum.NO.getValue());
        UserFollow userFollow = userFollowService.selectFirstByExample(example);
        if(userFollow == null){
            result.setResult(ResultEnum.FOLLOW_NOT_EXIST);
            return result;
        }
        userFollow.setDelFlag(YesNoEnum.YES.getValue());
        userFollowService.updateByPrimaryKeySelective(userFollow);
        logger.info("{}取消关注{}成功",user.getId(),oUser.getId());
        return result;
    }

    /**
     * 看看我关注了多少
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/{userId}/followees")
    @ResponseBody
    public Object seefollowers(@PathVariable Integer userId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            result.setResult(ResultEnum.USER_NOT_LOGIN);
            return result;
        }
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andFollowerUserIdEqualTo(userId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        if(userId == user.getId()){
            List<FollowUserResponse> followees = userFollowService.selectFollowees(example);
            result.setData(followees);
        }else {
            FqUser oUser = userService.selectByPrimaryKey(userId);
            if(oUser == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }else {
                List<FollowUserResponse> followees = userFollowService.selectFollowees(example);
                result.setData(followees);
            }
        }
        return result;
    }

    /**
     * 我的关注
     * @param request
     * @param response
     * @return
     */
    @GetMapping("following")
    public String following(HttpServletRequest request, HttpServletResponse response, Model model){
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            return "/login.html";
        }
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andFollowerUserIdEqualTo(user.getId());
        List<FollowUserResponse> follows = userFollowService.selectFollowees(example);
        model.addAttribute("follows",follows);
        return "/follow/following.html";
    }
    /*
    我的粉丝
     */
    @GetMapping("fans")
    public String fans(HttpServletRequest request, HttpServletResponse response, Model model){
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            return "/login.html";
        }
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andFollowedUserIdEqualTo(user.getId());
        List<FollowUserResponse> fans = userFollowService.selectFans(example);
        model.addAttribute("fans",fans);
        return "/follow/fans.html";
    }
}
