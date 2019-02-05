package com.feiqu.web.controller.apidoc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.ApiDocProject;
import com.feiqu.system.model.ApiDocProjectExample;
import com.feiqu.system.model.ApiDocProjectUser;
import com.feiqu.system.model.ApiDocProjectUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.service.ApiDocProjectService;
import com.feiqu.system.service.ApiDocProjectUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* ApiDocProjectUsercontroller
* Created by cwd on 2019/1/17.
*/
@Controller
@RequestMapping("/apiDocProjectUser")
public class ApiDocProjectUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ApiDocProjectUserController.class);

    @Resource
    private ApiDocProjectUserService apiDocProjectUserService;
    @Resource
    private ApiDocProjectService apiDocProjectService;

    /**
    * 跳转到ApiDocProjectUser首页
    */
    @RequestMapping("")
    public String index(Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        ApiDocProjectExample example = new ApiDocProjectExample();
        example.createCriteria().andUserIdEqualTo(fqUserCache.getId());
        List<ApiDocProject> apiDocProjects = apiDocProjectService.selectByExample(example);
        List<KeyValue> keyValues = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(apiDocProjects)){
            apiDocProjects.forEach(apiDocProject -> {
                KeyValue keyValue = new KeyValue(apiDocProject.getId().toString(),apiDocProject.getProjectName());
                keyValues.add(keyValue);
            });
        }
        model.addAttribute("projects", keyValues);
        return "/apiDocProjectUser/index.html";
    }

    @GetMapping("receiveInvite")
    public String receiveInvite(){
        return "/apiDocProjectUser/receiveInvite.html";
    }

    @GetMapping("postInvite")
    public String postInvite(){
        return "/apiDocProjectUser/postInvite.html";
    }

    /**
    * 添加ApiDocProjectUser页面
    */
    @RequestMapping("/apiDocProjectUser_add")
    public String apiDocProjectUser_add() {
        return "/apiDocProjectUser/add.html";
    }

    /**
    * ajax删除ApiDocProjectUser
    */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            apiDocProjectUserService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
    * 更新ApiDocProjectUser页面
    */
    @RequestMapping("/edit/{apiDocProjectUserId}")
    public Object apiDocProjectUserEdit(@PathVariable Long apiDocProjectUserId, Model model) {
        ApiDocProjectUser apiDocProjectUser = apiDocProjectUserService.selectByPrimaryKey(apiDocProjectUserId);
        model.addAttribute("apiDocProjectUser", apiDocProjectUser);
        return "/apiDocProjectUser/edit.html";
    }

    /**
    * ajax更新ApiDocProjectUser
    */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ApiDocProjectUser apiDocProjectUser) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (apiDocProjectUser.getId() == null) {
                ApiDocProjectUserExample example = new ApiDocProjectUserExample();
                example.createCriteria().andStatusEqualTo(0).andSponsorEqualTo(fqUserCache.getId()).andUserIdEqualTo(apiDocProjectUser.getUserId());
                int count = apiDocProjectUserService.countByExample(example);
                if(count > 0){
                    result.setResult(ResultEnum.FAIL);
                    result.setMessage("请不要对一个人重复发送邀请！");
                    return result;
                }
                apiDocProjectUser.setSponsor(fqUserCache.getId());
                apiDocProjectUser.setCreateTime(new Date());
                apiDocProjectUser.setStatus(0);
                apiDocProjectUserService.insert(apiDocProjectUser);
            } else {
                apiDocProjectUserService.updateByPrimaryKeySelective(apiDocProjectUser);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
    * 查询ApiDocProjectUser首页
    */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index, @RequestParam(defaultValue = "10") Integer size, Long projectId) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            ApiDocProjectUserExample example = new ApiDocProjectUserExample();
            example.setOrderByClause("create_time desc");
            example.createCriteria().andProjectIdEqualTo(projectId).andStatusEqualTo(1);
            List<ApiDocProjectUser> list = apiDocProjectUserService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     *
     * @param index
     * @param size
     * @param userId
     * @param type 1 收到的邀请 2 我发的邀请
     * @return
     */
    @GetMapping("inviteQuery")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index, @RequestParam(defaultValue = "10") Integer size, Integer userId, Integer type) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            ApiDocProjectUserExample example = new ApiDocProjectUserExample();
            example.setOrderByClause("create_time desc");
            if(type == 1){
                example.createCriteria().andUserIdEqualTo(userId);
            }else {
                example.createCriteria().andSponsorEqualTo(userId);
            }
            List<ApiDocProjectUser> list = apiDocProjectUserService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    @PostMapping("dealInvite")
    @ResponseBody
    public Object list(Long id,Integer status) {
        BaseResult result = new BaseResult();
        try {
            ApiDocProjectUser apiDocProjectUser = apiDocProjectUserService.selectByPrimaryKey(id);
            Assert.notNull(apiDocProjectUser,"邀请不存在");
            //此条邀请已经被处理过了
            if(apiDocProjectUser.getStatus() != 0){
                result.setCode("1");
                result.setMessage("该条邀请已经被处理了！");
                return result;
            }
            if(apiDocProjectUser.getStatus().equals(status)){
               return result;
            }
            ApiDocProjectUser toUpdate = new ApiDocProjectUser();
            toUpdate.setId(id);
            toUpdate.setStatus(status);
            apiDocProjectUserService.updateByPrimaryKeySelective(toUpdate);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}