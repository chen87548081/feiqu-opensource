package com.feiqu.web.controller.apidoc;

import cn.hutool.core.collection.CollectionUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.ApiDocProject;
import com.feiqu.system.model.ApiDocProjectExample;
import com.feiqu.system.model.ApiDocProjectUser;
import com.feiqu.system.model.ApiDocProjectUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
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
import java.util.stream.Collectors;


/**
 * ApiDocProjectcontroller
 * Created by cwd on 2019/1/13.
 */
@Controller
@RequestMapping("/apiDocProject")
public class ApiDocProjectController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ApiDocProjectController.class);

    @Resource
    private ApiDocProjectService apiDocProjectService;
    @Resource
    private ApiDocProjectUserService apiDocProjectUserService;

    /**
     * 跳转到ApiDocProject首页
     */
    @RequestMapping("")
    public String index() {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        return "/apiDocProject/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,Model model) {
        ApiDocProject apiDocProject = apiDocProjectService.selectByPrimaryKey(id);
        model.addAttribute("apiDocProject",apiDocProject);
        return "/apiDocProject/detail";
    }

    @GetMapping("/public")
    public String publicHtml() {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        return "/apiDocProject/public";
    }

    @GetMapping("/introduce")
    public String introduce() {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        return "/apiDocProject/introduce";
    }

    /**
     * 我加入的项目
     * @return
     */
    @GetMapping("/joined")
    public String joined() {
        return "/apiDocProject/joined";
    }

    /**
     * 添加ApiDocProject页面
     */
    @RequestMapping("/apiDocProject_add")
    public String apiDocProject_add() {
        return "/apiDocProject/add";
    }

    /**
     * ajax添加ApiDocProject
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ApiDocProject apiDocProject) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (apiDocProject.getId() == null) {
                ApiDocProjectExample example = new ApiDocProjectExample();
                example.createCriteria().andUserIdEqualTo(fqUserCache.getId());
                int projectCount = apiDocProjectService.countByExample(example);
                boolean match = isOverNum(fqUserCache.getLevel(),projectCount);
                if(!match){
                    result.setCode("1");
                    result.setMessage("您当前等级不足以创建新的项目！");
                    return result;
                }
                apiDocProject.setUserId(fqUserCache.getId());
                apiDocProject.setCreateTime(new Date());
                apiDocProject.setStatus((byte) 1);
                apiDocProject.setCover("");
                apiDocProject.setPassword("");
                apiDocProjectService.insert(apiDocProject);
            } else {
                apiDocProjectService.updateByPrimaryKeySelective(apiDocProject);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    private boolean isOverNum(Integer level, int alreadyProjectCount) {
        boolean isOver = false;
        switch (level){
            case 1:if(alreadyProjectCount <1) return true;break;
            case 2:if(alreadyProjectCount <3) return true;break;
            case 3:if(alreadyProjectCount <5) return true;break;
            default:break;
        }
        return isOver;
    }

    /**
     * ajax删除ApiDocProject
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long apiDocProjectId) {
        BaseResult result = new BaseResult();
        try {
            apiDocProjectService.deleteByPrimaryKey(apiDocProjectId);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新ApiDocProject页面
     */
    @RequestMapping("/edit/{apiDocProjectId}")
    public Object apiDocProjectEdit(@PathVariable Long apiDocProjectId, Model model) {
        ApiDocProject apiDocProject = apiDocProjectService.selectByPrimaryKey(apiDocProjectId);
        model.addAttribute("apiDocProject", apiDocProject);
        return "/apiDocProject/edit";
    }

    /**
     *
     * @param index
     * @param size
     * @param userId
     * @param type 1 创建 2 加入 3 公开的
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size,
                       Integer userId,
                       @RequestParam(defaultValue = "1") Integer type) {
        BaseResult result = new BaseResult();
        try {
            List<ApiDocProject> list = Lists.newArrayList();
            ApiDocProjectExample example = new ApiDocProjectExample();
            example.setOrderByClause("create_time desc");
            if(type == 1){
                example.createCriteria().andUserIdEqualTo(userId);
                PageHelper.startPage(index, size);
            }else if(type == 2) {
                ApiDocProjectUserExample apiDocProjectUserExample = new ApiDocProjectUserExample();
                apiDocProjectUserExample.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(1);
                List<ApiDocProjectUser> apiDocProjectUsers = apiDocProjectUserService.selectByExample(apiDocProjectUserExample);
                if(CollectionUtil.isNotEmpty(apiDocProjectUsers)){
                    List<Long> projectIds = apiDocProjectUsers.stream().map(ApiDocProjectUser::getProjectId).collect(Collectors.toList());
                    example.createCriteria().andIdIn(projectIds);
                    PageHelper.startPage(index, size);
                }
            }else {
                example.createCriteria().andTypeEqualTo((byte) 2);
            }
            list = apiDocProjectService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}