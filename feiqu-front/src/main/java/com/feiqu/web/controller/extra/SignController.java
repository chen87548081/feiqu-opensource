package com.feiqu.web.controller.extra;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ActiveNumEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqSign;
import com.feiqu.system.model.FqSignExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.SignRes;
import com.feiqu.system.pojo.response.SignUserResponse;
import com.feiqu.system.service.FqSignService;
import com.feiqu.system.service.FqUserService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SignController
 *
 * @author chenweidong
 * @date 2017/11/17
 */
@Controller
@RequestMapping("sign")
public class SignController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(SignController.class);
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqSignService signService;
    @Autowired
    private FqUserService userService;
    @ResponseBody
    @PostMapping(value = "in")
    public Object signin(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqSignExample example = new FqSignExample();
            example.createCriteria().andUserIdEqualTo(user.getId());
            FqSign sign = signService.selectFirstByExample(example);
            Date now = new Date();
            if(sign == null){
                sign = new FqSign();
                sign.setDays(1);
                sign.setSignTime(new Date());
                sign.setUserId(user.getId());
                sign.setSignDays(DateUtil.date().toString("dd"));
                signService.insert(sign);
            }else {
                if(sign.getSignTime().after(DateUtil.beginOfDay(now))){
                    result.setResult(ResultEnum.SIGN_ALREADY_TODAY);
                    return result;
                }
                if(sign.getSignTime().after(DateUtil.beginOfDay(DateUtil.yesterday()))){
                    sign.setDays(sign.getDays()+1);
                }else {
                    sign.setDays(1);
                }
                //将signdays更新1,2,3,5,.....
                if(DateUtil.date().dayOfMonth() == 1){//每月第一天清空为1
                    sign.setSignDays("1");
                } else if(sign.getSignTime().before(DateUtil.beginOfMonth(now))){
                    sign.setSignDays(DateUtil.date().toString("dd"));
                } else {
                    sign.setSignDays(StringUtils.isBlank(sign.getSignDays())?
                            DateUtil.date().toString("dd") : sign.getSignDays() + "," + DateUtil.date().toString("dd"));
                }
                sign.setSignTime(now);
                signService.updateByPrimaryKey(sign);
            }
            SignRes signStatus = new SignRes(sign,now);
            signStatus.setSigned(true);
            if(signStatus.getDays() < 5){
                signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_5);
            }else if(signStatus.getDays() < 15){
                signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_15);
            }else if(signStatus.getDays() < 30){
                signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_30);
            }else {
                signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_30_MORE);
            }
            CommonUtils.addOrDelUserQudouNum(user,signStatus.getExperience());
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.SIGN_IN.getValue());
            result.setData(signStatus);
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "status")
    public Object status(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setData(null);
                return result;
            }
            FqSignExample example = new FqSignExample();
            Date now = new Date();
            example.createCriteria().andUserIdEqualTo(user.getId()).andSignTimeGreaterThan(DateUtil.beginOfDay(now));
            FqSign sign = signService.selectFirstByExample(example);
            if(sign == null){
                result.setData(null);
                return result;
            }else {
                SignRes signStatus = new SignRes(sign,now);
                signStatus.setSigned(true);
                if(signStatus.getDays() < 5){
                    signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_5);
                }else if(signStatus.getDays() < 15){
                    signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_15);
                }else if(signStatus.getDays() < 30){
                    signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_30);
                }else {
                    signStatus.setExperience(CommonConstant.SIGN_DAYS_QUDOU_NUM_30_MORE);
                }
                result.setData(signStatus);
            }
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "top")
    public Object top(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            PageHelper.startPage(0, 20);
            FqSignExample example = new FqSignExample();
            example.setOrderByClause("sign_time desc");
            List<SignUserResponse> signs = signService.selectWithUserByExample(example);//最新签到
            List<List<SignUserResponse>> lists = new ArrayList<List<SignUserResponse>>();
            lists.add(signs);
            example.clear();
            example.setOrderByClause("sign_time asc");
            example.createCriteria().andSignTimeGreaterThan(DateUtil.beginOfDay(new Date()));
            PageHelper.startPage(0, 20);
            List<SignUserResponse> signs2 = signService.selectWithUserByExample(example);//今日最快
            lists.add(signs2);
            example.clear();
            example.setOrderByClause("days desc");
            PageHelper.startPage(0, 20);
            List<SignUserResponse> signs3 = signService.selectWithUserByExample(example);//总签到榜
            lists.add(signs3);
            result.setData(lists);
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }




}
