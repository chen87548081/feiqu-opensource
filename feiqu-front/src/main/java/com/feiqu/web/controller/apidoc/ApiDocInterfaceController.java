package com.feiqu.web.controller.apidoc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.condition.ApiDocInterfaceCondition;
import com.feiqu.system.pojo.condition.DebugCondition;
import com.feiqu.system.pojo.response.HttpResponseDetail;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.service.ApiDocInterfaceService;
import com.feiqu.system.service.ApiDocModuleService;
import com.feiqu.system.service.ApiDocProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ApiDocInterfacecontroller
 * Created by cwd on 2019/1/13.
 */
@Controller
@RequestMapping("/apiDocInterface")
public class ApiDocInterfaceController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ApiDocInterfaceController.class);

    @Resource
    private ApiDocInterfaceService apiDocInterfaceService;
    @Resource
    private ApiDocModuleService apiDocModuleService;
    @Resource
    private ApiDocProjectService apiDocProjectService;

    /**
     * 跳转到ApiDocInterface首页
     */
    @RequestMapping("")
    public String index(ApiDocInterfaceCondition condition,Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        model.addAttribute("condition",JSON.toJSON(condition));
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
        return "/apiDocInterface/index.html";
    }


    /**
     * 添加ApiDocInterface页面
     */
    @RequestMapping("/apiDocInterfaceAdd")
    public String apiDocInterface_add(Long projectId,Model model) {
        model.addAttribute("projectId",projectId);
        ApiDocModuleExample example = new ApiDocModuleExample();
        example.createCriteria().andProjectIdEqualTo(projectId);
        List<ApiDocModule> modules = apiDocModuleService.selectByExample(example);
        List<KeyValue> keyValues = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(modules)){
            modules.forEach(module -> {
                KeyValue keyValue = new KeyValue(module.getId().toString(),module.getModuleName());
                keyValues.add(keyValue);
            });
        }
        model.addAttribute("modules", keyValues);
        return "/apiDocInterface/add.html";
    }

    /**
     * ajax删除ApiDocInterface
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            apiDocInterfaceService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新ApiDocInterface页面
     */
    @RequestMapping("/edit/{apiDocInterfaceId}")
    public Object apiDocInterfaceEdit(@PathVariable Long apiDocInterfaceId, Model model) {
        ApiDocInterface apiDocInterface = apiDocInterfaceService.selectByPrimaryKey(apiDocInterfaceId);
        apiDocInterface.setTrueexam(HtmlUtils.htmlUnescape(apiDocInterface.getTrueexam()));
        apiDocInterface.setFalseexam(HtmlUtils.htmlUnescape(apiDocInterface.getFalseexam()));
        model.addAttribute("apiDocInterface", JSON.toJSON(apiDocInterface));
        ApiDocModuleExample example = new ApiDocModuleExample();
        example.createCriteria().andProjectIdEqualTo(apiDocInterface.getProjectid());
        List<ApiDocModule> modules = apiDocModuleService.selectByExample(example);
        List<KeyValue> keyValues = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(modules)){
            modules.forEach(module -> {
                KeyValue keyValue = new KeyValue(module.getId().toString(),module.getModuleName());
                keyValues.add(keyValue);
            });
        }
        model.addAttribute("modules", keyValues);
        model.addAttribute("apiDocInterfaceId", apiDocInterfaceId);
        return "/apiDocInterface/edit.html";
    }

    /**
     * 更新ApiDocInterface页面
     */
    @RequestMapping("/debug/{apiDocInterfaceId}")
    public Object debug(@PathVariable Long apiDocInterfaceId, Model model) {
        ApiDocInterface apiDocInterface = apiDocInterfaceService.selectByPrimaryKey(apiDocInterfaceId);
        apiDocInterface.setTrueexam(HtmlUtils.htmlUnescape(apiDocInterface.getTrueexam()));
        apiDocInterface.setFalseexam(HtmlUtils.htmlUnescape(apiDocInterface.getFalseexam()));
        model.addAttribute("apiDocInterface", JSON.toJSON(apiDocInterface));
        ApiDocModuleExample example = new ApiDocModuleExample();
        example.createCriteria().andProjectIdEqualTo(apiDocInterface.getProjectid());
        List<ApiDocModule> modules = apiDocModuleService.selectByExample(example);
        List<KeyValue> keyValues = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(modules)){
            modules.forEach(module -> {
                KeyValue keyValue = new KeyValue(module.getId().toString(),module.getModuleName());
                keyValues.add(keyValue);
            });
        }
        model.addAttribute("modules", keyValues);
        model.addAttribute("apiDocInterfaceId", apiDocInterfaceId);
        return "/apiDocInterface/debug.html";
    }

    /**
     * 更新ApiDocInterface页面
     */
    @PostMapping("/doDebug")
    @ResponseBody
    public Object doDebug(DebugCondition debugCondition) {
        BaseResult result = new BaseResult();
        try {
            logger.info("debug入参：{}",JSON.toJSONString(debugCondition));
            debugCondition.setParam(HtmlUtils.htmlUnescape(debugCondition.getParam()));
            ApiDocInterface apiDocInterface = apiDocInterfaceService.selectByPrimaryKey(debugCondition.getId());
            Assert.notNull(apiDocInterface,"接口不能为空！");
            HttpRequest httpRequest = new HttpRequest(debugCondition.getFullurl());
            httpRequest.header("Content-Type",apiDocInterface.getContenttype());
            JSONObject jsonObject = new JSONObject(debugCondition.getParam());
            String interfaceMethod = apiDocInterface.getMethod();
            HttpResponse httpResponse = null;
            if(Method.POST.name().equals(interfaceMethod)){
                httpResponse = httpRequest.method(Method.POST).body(jsonObject).execute();
            }else if(Method.GET.name().equals(interfaceMethod)){
                httpResponse = httpRequest.method(Method.GET).body(jsonObject).execute();
            }

            String reponseStr = httpResponse.toString();
            String resultStr = httpResponse.body();
            logger.info("debug反参：状态码:{},内容编码:{},返回数据：{}",httpResponse.getStatus(),httpResponse.contentEncoding(),resultStr);

            String header = reponseStr.substring(0,reponseStr.indexOf("Response Body"));
            HttpResponseDetail responseDetail = new HttpResponseDetail(header,httpResponse.body());
            result.setData(responseDetail);
        }catch (Exception e){
            logger.error("debug报错",e);
            result.setCode("1");
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * ajax更新ApiDocInterface
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ApiDocInterface apiDocInterface) {
        BaseResult result = new BaseResult();
        try {
            logger.info("入参：{}",JSON.toJSONString(apiDocInterface));
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (apiDocInterface.getId() == null) {
                Assert.notNull(apiDocInterface.getProjectid(),"项目不能为空！");
                Assert.notNull(apiDocInterface.getModuleid(),"模块不能为空！");
                ApiDocModule module = apiDocModuleService.selectByPrimaryKey(apiDocInterface.getModuleid());
                apiDocInterface.setUpdatetime(new Date());
                apiDocInterface.setUpdateby(fqUserCache.getNickname());
                apiDocInterface.setFullurl(module.getUrl() + apiDocInterface.getUrl());
                apiDocInterface.setIstemplate(false);
                apiDocInterfaceService.insert(apiDocInterface);
            } else {
                ApiDocModule module = apiDocModuleService.selectByPrimaryKey(apiDocInterface.getModuleid());
                apiDocInterface.setFullurl(module.getUrl() + apiDocInterface.getUrl());
                apiDocInterface.setUpdateby(fqUserCache.getNickname());
                apiDocInterfaceService.updateByPrimaryKeySelective(apiDocInterface);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询ApiDocInterface首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size, ApiDocInterfaceCondition condition) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            ApiDocProjectExample projectExample = new ApiDocProjectExample();
            projectExample.createCriteria().andUserIdEqualTo(fqUserCache.getId()).andStatusEqualTo((byte) 1);
            List<ApiDocProject> apiDocProjects = apiDocProjectService.selectByExample(projectExample);
            if(CollectionUtil.isEmpty(apiDocProjects)){
                return result;
            }
            List<Long> prjectIds = apiDocProjects.stream().map(ApiDocProject::getId).collect(Collectors.toList());

            PageHelper.startPage(index, size);
            ApiDocInterfaceExample example = new ApiDocInterfaceExample();
            example.setOrderByClause("sequence desc");
            ApiDocInterfaceExample.Criteria criteria = example.createCriteria();
            criteria.andProjectidIn(prjectIds);
            if(condition != null){
                if(condition.getProjectId() != null){
                    criteria.andProjectidEqualTo(condition.getProjectId());
                }
            }
            List<ApiDocInterface> list = apiDocInterfaceService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

}