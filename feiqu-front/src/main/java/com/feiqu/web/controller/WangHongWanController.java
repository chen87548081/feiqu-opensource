package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.model.WangHongWan;
import com.feiqu.system.model.WangHongWanExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.WangHongWanDTO;
import com.feiqu.system.service.WangHongWanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * WangHongWancontroller
 * Created by cwd on 2018/12/22.
 */
@Controller
@RequestMapping("/wangHong")
public class WangHongWanController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WangHongWanController.class);

    @Autowired
    private WangHongWanService wangHongWanService;

    /**
     * 跳转到WangHongWan首页
     */
    @RequestMapping("")
    public String index(@RequestParam(defaultValue = "1") Integer p, Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        PageHelper.startPage(p, CommonConstant.DEAULT_PAGE_SIZE);
        WangHongWanExample example = new WangHongWanExample();
        example.setOrderByClause("ID desc");
        List<WangHongWan> wangHongWans = wangHongWanService.selectByExample(example);
        PageInfo page = new PageInfo(wangHongWans);
        model.addAttribute("pageIndex",p);
        model.addAttribute("pageSize",CommonConstant.DEAULT_PAGE_SIZE);
        model.addAttribute("count",page.getTotal());
        List<WangHongWanDTO> wangHongWanDTOS = Lists.newArrayList();
        wangHongWans.forEach(wangHongWan -> {
            WangHongWanDTO wangHongWanDTO = new WangHongWanDTO();
            wangHongWanDTO.setArea(wangHongWan.getArea());
            wangHongWanDTO.setAuthor(wangHongWan.getAuthor());
            wangHongWanDTO.setContent(wangHongWan.getContent());
            wangHongWanDTO.setId(wangHongWan.getId());
            List<String> imgs = Arrays.asList(wangHongWan.getPicList().split(","));
            imgs = imgs.stream().map(e->e+"?x-oss-process=style/img_thum3").collect(Collectors.toList());
            wangHongWanDTO.setImgs(imgs);
            wangHongWanDTOS.add(wangHongWanDTO);
        });
        model.addAttribute("wangHongWans",wangHongWanDTOS);
        return "/wh/index.html";
    }

    /**
     * 添加WangHongWan页面
     */
    @RequestMapping("/wangHongWan_add")
    public String wangHongWan_add() {
        return "/WangHongWan/add.html";
    }

   /* @ResponseBody
    @RequestMapping("/generate")
    public Object generate() {
        BaseResult result = new BaseResult();
        File file = new File("D:\\webmagic\\wanghongwan.com");
        File[] files =  file.listFiles();
        for(File file1 : files){
            List<String> list = FileUtil.readLines(file1, "UTF-8");
            list.forEach(s -> {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray imgs = jsonObject.getJSONArray("imgs");
                JSONArray author = jsonObject.getJSONArray("author");
                String area = jsonObject.getStr("area");
                String content = jsonObject.getStr("content");
                WangHongWan wangHongWan = new WangHongWan();
                wangHongWan.setArea(area);
                wangHongWan.setAuthor(author == null?"": EmojiUtils.toAliases(author.get(0).toString()));
                wangHongWan.setContent(StringUtils.isEmpty(content)?"":EmojiUtils.toAliases(content));
                StringJoiner stringJoiner = new StringJoiner(",");
                if(imgs != null &&!imgs.isEmpty()){
                    imgs.forEach(i->{
                        stringJoiner.add((String) i);
                    });
                }
                wangHongWan.setPicList(stringJoiner.toString());
                wangHongWanService.insert(wangHongWan);
            });
        }

        return result;
    }*/

    /**
     * ajax添加WangHongWan
     */
    @ResponseBody
    @RequestMapping("/add")
    public Object add(WangHongWan wangHongWan) {
        BaseResult result = new BaseResult();
        wangHongWanService.insert(wangHongWan);
        return result;
    }

    /**
     * ajax删除WangHongWan
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        wangHongWanService.deleteByPrimaryKey(id);
        return result;
    }

    /**
     * 更新WangHongWan页面
     */
    @RequestMapping("/{wangHongWanId}")
    public Object wangHongWanEdit(@PathVariable Long wangHongWanId, Model model) {
        WangHongWan wangHongWan = wangHongWanService.selectByPrimaryKey(wangHongWanId);
        WangHongWanDTO wangHongWanDTO = new WangHongWanDTO();
        wangHongWanDTO.setArea(wangHongWan.getArea());
        wangHongWanDTO.setAuthor(wangHongWan.getAuthor());
        wangHongWanDTO.setContent(wangHongWan.getContent());
        wangHongWanDTO.setId(wangHongWan.getId());
        List<String> imgs = Arrays.asList(wangHongWan.getPicList().split(","));
        wangHongWanDTO.setImgs(imgs);
        model.addAttribute("fqTopic", wangHongWanDTO);
        return "/wh/detail.html";
    }

    /**
     * ajax更新WangHongWan
     */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(WangHongWan wangHongWan) {
        BaseResult result = new BaseResult();
        wangHongWanService.updateByPrimaryKey(wangHongWan);
        return result;
    }


    /**
     * 查询WangHongWan首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        WangHongWanExample example = new WangHongWanExample();
        example.setOrderByClause("create_time desc");
        List<WangHongWan> list = wangHongWanService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }
}