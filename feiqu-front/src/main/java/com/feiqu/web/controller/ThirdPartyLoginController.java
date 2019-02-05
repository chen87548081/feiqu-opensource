package com.feiqu.web.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.login.ThirdPartyLoginHelper;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.FqThirdParty;
import com.feiqu.system.model.FqThirdPartyExample;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.ThirdPartyUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqThirdPartyService;
import com.feiqu.system.service.FqUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/8.
 */
@Controller
public class ThirdPartyLoginController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ThirdPartyLoginController.class);

    @Autowired
    private FqUserService fqUserService;
    @Autowired
    private FqThirdPartyService fqThirdPartyService;
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private CMessageService messageService;

    @RequestMapping("/sns")
    public void thirdLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam("t") String type) {
        String url = getRedirectUrl(request, type);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/callback/qq")
    public String qqCallback(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
        String host = request.getHeader("host");
        try {
            String code = request.getParameter("code");
            FqUserCache fqUser = webUtil.currentUser(request,response);
            boolean login = fqUser != null;
            FqUser fqUserEntity = fqUser == null? null:fqUser.transferFqUser();
            if (StringUtils.isNotBlank(code)) {// 如果不为空
                // 获取token和openid
                Map<String, String> map = ThirdPartyLoginHelper.getQQTokenAndOpenid(code, host);
                String openId = map.get("openId");
                logger.info("qq获取到token{}和openId:{}",map.get("access_token"),openId);
                if (StringUtils.isNotBlank(openId)) {// 如果openID存在
                    // 获取第三方用户信息存放到session中
                    ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getQQUserinfo(map.get("access_token"), openId);
                    thirdUser.setProvider("QQ");
                    thirdPartyLogin(request, thirdUser,response,fqUserEntity);
                    // 跳转到登录成功界面
//                    modelMap.put("retUrl", Resources.Global.getPropertiesConfig("third_login_success"));
                    if(login){
                        return "redirect:/u/set#bind";
                    }else {
                        // 跳转到登录成功界面
                        return "redirect:/";
                    }
                } else {// 如果未获取到OpenID
                    return "redirect:/u/register";
                }
            } else {// 如果没有返回令牌，则直接返回到登录页面
                return "redirect:/u/register";
            }
        } catch (Exception e) {
            logger.error("qq login error",e);
        }
        return "redirect:/u/register";
    }

    @RequestMapping("/callback/sina")
    public String sinaCallback(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
        String host = request.getHeader("host");
//        String host = "www.flyfun.site";
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            FqUser fqUserEntity = fqUser == null? null:fqUser.transferFqUser();
            boolean login = fqUser != null;
            String code = request.getParameter("code");
            if (StringUtils.isNotBlank(code)) {// 如果不为空
                // 获取token和uid
                JSONObject json = ThirdPartyLoginHelper.getSinaTokenAndUid(code, host);
                logger.info("获取token和uid:{}",json.toString());
                String uid = json.getString("uid");
                if (StringUtils.isNotBlank(uid)) {// 如果uid存在
                    // 获取第三方用户信息存放到session中
                    ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getSinaUserinfo(json.getString("access_token"),
                            uid);
                    thirdUser.setProvider("SINA");
                    thirdPartyLogin(request, thirdUser,response,fqUserEntity);
                    if(login){
                        return "redirect:/u/set#user-tab=4";
                    }else {
                        // 跳转到登录成功界面
                        return "redirect:/";
                    }
//                    modelMap.put("retUrl", Resources.Global.getPropertiesConfig("third_login_success"));
                } else {// 如果未获取到OpenID
                    // 跳转到登录成功界面
//                    modelMap.put("retUrl", "-1");
                    return "redirect:/u/register";
                }
            } else {// 如果没有返回令牌，则直接返回到登录页面
                // 跳转到登录成功界面
//                modelMap.put("retUrl", "-1");
                return "redirect:/u/register";
            }
        } catch (Exception e) {
            // 跳转到登录失败界面
//            modelMap.put("retUrl", "-1");
            logger.error("新浪登陆报错",e);
            return "redirect:/u/register";
        }
    }


    @RequestMapping("/callback/wx")
    public String wxCallback(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
        String host = request.getHeader("host");
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            FqUser fqUserEntity = fqUser == null? null:fqUser.transferFqUser();
            boolean login = fqUser != null;
            String code = request.getParameter("code");
            if (StringUtils.isNotBlank(code)) {// 如果不为空
                Map<String, String> map = ThirdPartyLoginHelper.getWxTokenAndOpenid(code, host);
                String openId = map.get("openId");
                logger.info("获取openId:{}",openId);
                if (StringUtils.isNotBlank(openId)) {// 如果uid存在
                    // 获取第三方用户信息存放到session中
                    ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getWxUserinfo(map.get("access_token"), openId);
                    thirdUser.setProvider("WX");
                    thirdPartyLogin(request, thirdUser,response,fqUserEntity);
                    if(login){
                        return "redirect:/u/set#user-tab=4";
                    }else {
                        // 跳转到登录成功界面
                        return "redirect:/";
                    }
//                    modelMap.put("retUrl", Resources.Global.getPropertiesConfig("third_login_success"));
                } else {// 如果未获取到OpenID
                    // 跳转到登录成功界面
//                    modelMap.put("retUrl", "-1");
                    return "redirect:/u/register";
                }
            } else {// 如果没有返回令牌，则直接返回到登录页面
                // 跳转到登录成功界面
//                modelMap.put("retUrl", "-1");
                return "redirect:/u/register";
            }
        } catch (Exception e) {
            // 跳转到登录失败界面
//            modelMap.put("retUrl", "-1");
            logger.error("微信登陆报错",e);
            return "redirect:/u/register";
        }
    }

    private void thirdPartyLogin(HttpServletRequest request, ThirdPartyUser thirdUser, HttpServletResponse response, FqUser fqUser) {
        String ip = WebUtil.getIP(request);
        if(fqUser == null){
            FqThirdPartyExample example = new FqThirdPartyExample();
            example.createCriteria().andOpenidEqualTo(thirdUser.getOpenid()).andProviderEqualTo(thirdUser.getProvider());
            FqThirdParty thirdParty = fqThirdPartyService.selectFirstByExample(example);
            if(thirdParty != null){
                fqUser = fqUserService.selectByPrimaryKey(thirdParty.getUserId());
                if(fqUser != null){
                    /*fqUser.setCreateIp(WebUtil.getIP(request));
                fqUser.setProvider(thirdUser.getProvider());
                    fqUserService.updateByPrimaryKey(fqUser);*/
                    WebUtil.loginUser(request,response,fqUser,true);
                }
            }else {
                fqUser = new FqUser(thirdUser);
                fqUser.setCreateIp(ip);
                if(StringUtils.isEmpty(fqUser.getCity())){
                    fqUser.setCity(CommonUtils.getRegionByIp(ip));
                }
                fqUser.setQudouNum(CommonConstant.INIT_QUDOU_NUM);
                int userId = fqUserService.insertThirdPartyUser(fqUser,thirdUser);
                WebUtil.loginUser(request,response,fqUser,true);
                Date now = new Date();
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(fqUser.getId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：欢迎你来到飞趣社区，希望你在这体验愉快！另外，你可以加入官方qq交流群：632118669,一起讨论。 "+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
        }else {//绑定
            FqThirdPartyExample example = new FqThirdPartyExample();
            example.createCriteria().andOpenidEqualTo(thirdUser.getOpenid()).andProviderEqualTo(thirdUser.getProvider());
            FqThirdParty thirdParty = fqThirdPartyService.selectFirstByExample(example);
            if(thirdParty == null){
                thirdUser.setUserId(fqUser.getId());
                thirdParty = new FqThirdParty(thirdUser);
                fqThirdPartyService.insert(thirdParty);
            }else {//此第三方已经绑定
            }
        }


    }

    private String getRedirectUrl(HttpServletRequest request, String type) {
        String url = "";
        String host = request.getHeader("host");
       //todo
         url = Global.getPropertiesConfig("authorizeURL_" + type);
        if ("wx".equals(type)) {
            url = url + "?appid=" + Global.getPropertiesConfig("app_id_" + type) + "&redirect_uri=http://" + host
                    + Global.getPropertiesConfig("redirect_url_" + type) + "&response_type=code&scope="
                    + Global.getPropertiesConfig("scope_" + type) + "&state=fhmj";
        } else {
            url = url + "?client_id=" + Global.getPropertiesConfig("app_id_" + type) + "&response_type=code&scope="
                    + Global.getPropertiesConfig("scope_" + type) + "&redirect_uri=http://" + host
                    + Global.getPropertiesConfig("redirect_url_" + type);
        }
        return url;
    }
}
