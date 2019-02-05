package com.feiqu.web.controller;

import cn.hutool.core.lang.Assert;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.model.FqFriendLinkExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqFriendLinkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqFriendLinkcontroller
 * Created by cwd on 2019/1/9.
 */
@Controller
@RequestMapping("/fqFriendLink")
public class FqFriendLinkController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqFriendLinkController.class);

    @Resource
    private FqFriendLinkService fqFriendLinkService;

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
        return "/friendLink/manage.html";
    }
    @GetMapping("/manage/list")
    @ResponseBody
    public Object manageList(Integer page,Integer limit){
        BaseResult result = new BaseResult();
        try {
            if(limit >20 ){
                limit = 20;
            }
            PageHelper.startPage(page,limit);
            FqFriendLinkExample example = new FqFriendLinkExample();
            example.setOrderByClause("create_time desc");
            List<FqFriendLink> fqFriendLinks = fqFriendLinkService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(fqFriendLinks);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("友链分页出错",e);
            result.setCode("1");
            result.setMessage("友链分页出错");
        }
        return result;
    }

    /**
     * ajax删除
     */
    @ResponseBody
    @PostMapping("/manage/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("删除友链：用户id：{}，友链id：{}",fqUserCache.getId(),id);
            FqFriendLink fqFriendLink = fqFriendLinkService.selectByPrimaryKey(id);
            Assert.notNull(fqFriendLink,"网址不能为空");
            fqFriendLinkService.deleteByPrimaryKey(id);
            CommonConstant.FRIEND_LINK_LIST.remove(fqFriendLink);
        } catch (Exception e) {
            logger.error("删除友链报错",e);
            result.setCode("1");
            result.setMessage(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * ajax更新FqWebsiteDir
     */
    @ResponseBody
    @PostMapping("/manage/save")
    public Object edit(FqFriendLink fqFriendLink) {
        BaseResult result = new BaseResult();
        if(fqFriendLink.getId() == null){
            fqFriendLink.setCreateTime(new Date());
            fqFriendLinkService.insert(fqFriendLink);
            CommonConstant.FRIEND_LINK_LIST.add(fqFriendLink);
        }else {
            fqFriendLinkService.updateByPrimaryKeySelective(fqFriendLink);
            CommonConstant.FRIEND_LINK_LIST = fqFriendLinkService.selectByExample(new FqFriendLinkExample());
        }
        return result;
    }
}