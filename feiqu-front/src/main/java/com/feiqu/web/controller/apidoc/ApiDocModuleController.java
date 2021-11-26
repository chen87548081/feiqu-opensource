package com.feiqu.web.controller.apidoc;

import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.ApiDocModule;
import com.feiqu.system.model.ApiDocModuleExample;
import com.feiqu.system.model.ApiDocProject;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.ApiDocModuleService;
import com.feiqu.system.service.ApiDocProjectService;
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
 * ApiDocModulecontroller
 * Created by cwd on 2019/1/13.
 */
@Controller
@RequestMapping("/apiDocModule")
public class ApiDocModuleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ApiDocModuleController.class);

    @Resource
    private ApiDocModuleService apiDocModuleService;
    @Resource
    private ApiDocProjectService apiDocProjectService;

    /**
     * 跳转到ApiDocModule首页
     */
    @RequestMapping("")
    public String index(Model model,Long projectId) {
//        ApiDocProjectExample example = new ApiDocProjectExample();
        ApiDocProject apiDocProject = apiDocProjectService.selectByPrimaryKey(projectId);
        /*List<KeyValue> keyValues = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(apiDocProjects)){
            apiDocProjects.forEach(apiDocProject -> {
                KeyValue keyValue = new KeyValue(apiDocProject.getId().toString(),apiDocProject.getProjectName());
                keyValues.add(keyValue);
            });
        }*/
        model.addAttribute("apiDocProject", JSON.toJSON(apiDocProject));
        model.addAttribute("projectId", projectId);
        return "/apiDocModule/index";
    }

    /**
     * 添加ApiDocModule页面
     */
    @RequestMapping("/apiDocModule_add")
    public String apiDocModule_add() {
        return "/apiDocModule/add";
    }

    /**
     * ajax删除ApiDocModule
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long apiDocModuleId) {
        BaseResult result = new BaseResult();
        try {
            apiDocModuleService.deleteByPrimaryKey(apiDocModuleId);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新ApiDocModule页面
     */
    @RequestMapping("/edit/{apiDocModuleId}")
    public Object apiDocModuleEdit(@PathVariable Long apiDocModuleId, Model model) {
        ApiDocModule apiDocModule = apiDocModuleService.selectByPrimaryKey(apiDocModuleId);
        model.addAttribute("apiDocModule", apiDocModule);
        return "/apiDocModule/edit";
    }

    /**
     * ajax更新ApiDocModule
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ApiDocModule apiDocModule) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (apiDocModule.getId() == null) {
                apiDocModule.setCreateTime(new Date());
                apiDocModule.setStatus((byte) 1);
                apiDocModule.setUserId(fqUserCache.getId());
                apiDocModuleService.insert(apiDocModule);
            } else {
                apiDocModuleService.updateByPrimaryKeySelective(apiDocModule);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询ApiDocModule首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size,Long projectId) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            ApiDocModuleExample example = new ApiDocModuleExample();
            example.setOrderByClause("create_time desc");
            example.createCriteria().andProjectIdEqualTo(projectId);
            List<ApiDocModule> list = apiDocModuleService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}