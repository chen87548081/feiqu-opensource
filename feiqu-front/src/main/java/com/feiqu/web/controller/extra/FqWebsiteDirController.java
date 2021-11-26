package com.feiqu.web.controller.extra;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqWebsiteDirService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * FqWebsiteDircontroller
 * Created by cwd on 2018/1/23.
 */
@Controller
@RequestMapping("/websiteDir")
public class FqWebsiteDirController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqWebsiteDirController.class);

    @Resource
    private FqWebsiteDirService fqWebsiteDirService;

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
        return "/websiteDir/manage";
    }
    @GetMapping("/manage/list")
    @ResponseBody
    public Object manageList(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit, FqWebsiteDir websiteDir){
        BaseResult result = new BaseResult();
        try {
            if(limit >20 ){
                limit = 20;
            }
            PageHelper.startPage(page,limit);
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            FqWebsiteDirExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(websiteDir.getName())){
                criteria.andNameLike("%"+websiteDir.getName()+"%");
            }
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<FqWebsiteDir> fqWebsiteDirs = fqWebsiteDirService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(fqWebsiteDirs);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("网址分页出错",e);
            result.setCode("1");
            result.setMessage("网址分页出错");
        }
        return result;
    }

    /**
     * 跳转到FqWebsiteDir首页
     */
    @RequestMapping("")
    public String index(Model model, @RequestParam(defaultValue = "1") Integer page, String type, String word) {
//        PageHelper.startPage(page,20);//暂时不分页 没有多少网址暂时
        try {
            /*redisTemplate.opsForValue().set("fq-3q","ss");
            String s = redisTemplate.opsForValue().get("fq-3q");
            logger.info(s);*/
            List<FqWebsiteDir> dirs;
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            FqUserCache user = getCurrentUser();
            Integer uid = user == null?0:user.getId();
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            FqWebsiteDirExample.Criteria criteria = example.createCriteria();
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            if(StringUtils.isNotEmpty(type)){
                criteria.andTypeEqualTo(type);
            }
            if(StringUtils.isNotEmpty(word)){
                criteria.andNameLike("%"+word+"%");
            }
            if(example.getOredCriteria().get(0).getAllCriteria().size() == 1){
                if(commands.exists(CommonConstant.FQ_WEBSITE_ALL)){
                    String allWebsites = commands.get(CommonConstant.FQ_WEBSITE_ALL);
                    dirs = JSON.parseArray(allWebsites,FqWebsiteDir.class);
                    if(CollectionUtils.isEmpty(dirs)){
                        dirs = fqWebsiteDirService.selectByExample(example);
                        commands.set(CommonConstant.FQ_WEBSITE_ALL,JSON.toJSONString(dirs));
                    }
                }else {
                    dirs = fqWebsiteDirService.selectByExample(example);
                    commands.set(CommonConstant.FQ_WEBSITE_ALL,JSON.toJSONString(dirs));
                }
            }else {
                dirs = fqWebsiteDirService.selectByExample(example);
            }
            Map<String,List<FqWebsiteDir>> map = dirs.stream().collect(Collectors.groupingBy(FqWebsiteDir::getType));

            List<FqWebsiteDir> personalHotWebs = Lists.newArrayList();
            PageHelper.startPage(0,8,false);
            example.clear();
            example.setOrderByClause("click_count desc");
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<FqWebsiteDir> commonHotWebs = fqWebsiteDirService.selectByExample(example);
            if(uid > 0){
                String key = CommonConstant.FQ_USER_WEBSITE_CLICK_COUNT +":"+uid;
                Set<Tuple> tuples = commands.zrevrangeWithScores(key,0,9);
                if(CollectionUtil.isNotEmpty(tuples)){
                    List<String> eles = tuples.stream().map(Tuple::getElement).collect(Collectors.toList());
                    Map<Integer,FqWebsiteDir> websiteMap = Maps.newHashMap();
                    List<Integer> siteIds = Lists.newArrayList();
                    eles.forEach(e->siteIds.add(Integer.valueOf(e)));
                    example.clear();
                    example.createCriteria().andIdIn(siteIds).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                    personalHotWebs = fqWebsiteDirService.selectByExample(example);
                    personalHotWebs.forEach(e->{
                        websiteMap.put(e.getId(), e);
                    });
                    personalHotWebs.clear();
                    for (Tuple e : tuples) {
                        Integer id = Integer.valueOf(e.getElement());
                        FqWebsiteDir fqWebsiteDir = websiteMap.get(id);
                        fqWebsiteDir.setClickCount((int) e.getScore());
                        personalHotWebs.add(fqWebsiteDir);
                    }
                }
            }

            model.addAttribute("commonHotWebs",commonHotWebs);
            model.addAttribute("personalHotWebs",personalHotWebs);

            model.addAttribute("dirs",dirs);
            List<String> types = fqWebsiteDirService.selectTypes();
            model.addAttribute("types",types);
            model.addAttribute("count",dirs.size());
            model.addAttribute("page",page);
            model.addAttribute("map",map);
            model.addAttribute("word",word);
        } catch (Exception e) {
            logger.error("网址页面报错",e);
            model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE,"网址页面报错");
            return GENERAL_CUSTOM_ERROR_URL;
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        return "/websiteDir/index";
    }

    @ResponseBody
    @PostMapping("/record")
    public BaseResult record(FqWebsiteDir fqWebsiteDir) {
        BaseResult baseResult = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            Integer uid = user == null?0:user.getId();
            if(StringUtils.isNotEmpty(fqWebsiteDir.getUrl())){
                FqWebsiteDirExample example = new FqWebsiteDirExample();
                example.createCriteria().andUrlEqualTo(fqWebsiteDir.getUrl()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                FqWebsiteDir fqWebsiteDirDB = fqWebsiteDirService.selectFirstByExample(example);
                if(fqWebsiteDirDB != null){
                    fqWebsiteDirDB.setClickCount(fqWebsiteDirDB.getClickCount() == null?1:fqWebsiteDirDB.getClickCount()+1);
                    fqWebsiteDirService.updateByPrimaryKey(fqWebsiteDirDB);
                    if(uid > 0){
                        //记录某个人的点击网站偏好
                        JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
                        String key = CommonConstant.FQ_USER_WEBSITE_CLICK_COUNT +":"+uid;
                        Double scoreStore = commands.zscore(key,fqWebsiteDirDB.getId().toString());
                        if(scoreStore == null){
                            scoreStore = 1d;
                        }else {
                            scoreStore += 1;
                        }
                        commands.zadd(key,scoreStore,fqWebsiteDirDB.getId().toString());
                    }
                }else {
                    logger.info("数据库未查到对应的url，网址为：{}",fqWebsiteDir.getUrl());
                }
            }
        } catch (Exception e) {
            logger.error("记录点击次数报错",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
        return baseResult;
    }

    /**
     * ajax添加FqWebsiteDir
     */
    @ResponseBody
    @PostMapping("/add")
    public Object add(FqWebsiteDir fqWebsiteDir, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        if(StringUtils.isBlank(fqWebsiteDir.getName()) || StringUtils.isBlank(fqWebsiteDir.getType())){
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }

        if(!ReUtil.isMatch(PatternPool.URL,fqWebsiteDir.getUrl())){
            result.setResult(ResultEnum.WEBSITE_URL_ERROR);
            return result;
        }
        FqUserCache fqUserCache = getCurrentUser();
        try {
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            example.createCriteria().andUrlEqualTo(fqWebsiteDir.getUrl()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqWebsiteDir fqWebsiteDirDB = fqWebsiteDirService.selectFirstByExample(example);
            if(fqWebsiteDirDB != null){
                result.setResult(ResultEnum.WEBSITE_URL_EXISTS);
                return result;
            }

            fqWebsiteDir.setDelFlag(YesNoEnum.NO.getValue());
            fqWebsiteDir.setUserId(fqUserCache == null?0:fqUserCache.getId());
            fqWebsiteDir.setCreateTime(new Date());
            fqWebsiteDir.setClickCount(0);
            fqWebsiteDirService.insert(fqWebsiteDir);
            CacheManager.refreshWebsiteCache();
        } catch (Exception e) {
            logger.error("发生系统错误",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * ajax删除FqWebsiteDir
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("删除网址：用户id：{}，图片id：{}",fqUserCache.getId(),id);
            FqWebsiteDir fqWebsiteDir = fqWebsiteDirService.selectByPrimaryKey(id);
            Assert.notNull(fqWebsiteDir,"网址不能为空");
            fqWebsiteDir.setDelFlag(YesNoEnum.YES.getValue());
            fqWebsiteDirService.updateByPrimaryKey(fqWebsiteDir);
            CacheManager.refreshWebsiteCache();
        } catch (Exception e) {
            logger.error("删除网址报错",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * 更新FqWebsiteDir页面
     */
    @RequestMapping("/edit/{fqWebsiteDirId}")
    public Object fqWebsiteDirEdit(@PathVariable Integer fqWebsiteDirId, Model model) {
        FqWebsiteDir fqWebsiteDir = fqWebsiteDirService.selectByPrimaryKey(fqWebsiteDirId);
        model.addAttribute("fqWebsiteDir", fqWebsiteDir);
        return "/system/FqWebsiteDir/edit";
    }

    /**
     * ajax更新FqWebsiteDir
     */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqWebsiteDir fqWebsiteDir) {
        BaseResult result = new BaseResult();
        fqWebsiteDirService.updateByPrimaryKeySelective(fqWebsiteDir);
        CacheManager.refreshWebsiteCache();
        return result;
    }


    /**
     * 查询FqWebsiteDir首页
     */
    /*@RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size,FqWebsiteDir websiteDir) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqWebsiteDirExample example = new FqWebsiteDirExample();
        FqWebsiteDirExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(websiteDir.getName())){
            criteria.andNameLike("%"+websiteDir.getName()+"%");
        }
        example.setOrderByClause("create_time desc");
        List<FqWebsiteDir> list = fqWebsiteDirService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }*/

    /**
     * 更新FqWebsiteDir页面
     */
    @RequestMapping("/techWiki")
    public Object techWiki() {
        return "/websiteDir/techWiki.html";
    }

    /*@RequestMapping("/collect")
    @ResponseBody
    public Object collect() {
        BaseResult result = new BaseResult();
        File file = new File("e://websites.txt");
        List<String> strings = FileUtil.readLines(file, Charset.forName("utf-8"));
        Date now = new Date();
        for(String s : strings){
            String[] strings1 = StringUtils.split(s,",");
            if(strings1.length != 4){
                logger.info(s);
                continue;
            }
            FqWebsiteDir fqWebsiteDir = new FqWebsiteDir();
            fqWebsiteDir.setClickCount(0);
            fqWebsiteDir.setCreateTime(now);
            fqWebsiteDir.setDelFlag(YesNoEnum.NO.getValue());
            fqWebsiteDir.setName(strings1[3]);
            fqWebsiteDir.setUrl(strings1[1]);
            fqWebsiteDir.setType(strings1[0]);
            fqWebsiteDir.setIcon(strings1[2]);
            fqWebsiteDir.setUserId(22);
            fqWebsiteDirService.insert(fqWebsiteDir);
        }
        return result;
    }*/
}