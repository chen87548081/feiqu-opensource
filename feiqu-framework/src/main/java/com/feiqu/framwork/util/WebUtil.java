package com.feiqu.framwork.util;


import com.feiqu.common.utils.DESUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cwd
 * @create 2017-09-11:59
 **/
@Service(value = "webUtil")
public class WebUtil {

    @Autowired
    private FqUserService userService;

    public FqUserCache currentUser(HttpServletRequest request, HttpServletResponse response) {
        FqUser user = null;
        String cookieKey = CommonConstant.USER_ID_COOKIE;
        // 获取cookie信息
        String userCookie = getCookie(request, cookieKey);
        // 1.cookie为空，直接清除
        if (StringUtils.isEmpty(userCookie)) {
            CookieUtil.removeCookie(response, cookieKey);
            return null;
        }
        // 2.解密cookie
        String cookieInfo = null;
        // cookie 私钥
        String secret = CommonConstant.USER_COOKIE_SECRET;
        try {
            cookieInfo = new DESUtils(secret).decryptString(userCookie);
        } catch (RuntimeException e) {
            // ignore
        }
        // 3.异常或解密问题，直接清除cookie信息
        if (StringUtils.isEmpty(cookieInfo)) {
            CookieUtil.removeCookie(response, cookieKey);
            return null;
        }
        String[] userInfo = cookieInfo.split("~");
        // 4.规则不匹配
        if (userInfo.length < 3) {
            CookieUtil.removeCookie(response, cookieKey);
            return null;
        }
        String userId   = userInfo[0];
        String oldTime  = userInfo[1];
        String maxAge   = userInfo[2];
        // 5.判定时间区间，超时的cookie清理掉
        if (!"-1".equals(maxAge)) {
            long now  = System.currentTimeMillis();
            long time = Long.parseLong(oldTime) + (Long.parseLong(maxAge) * 1000);
            if (time <= now) {
                CookieUtil.removeCookie(response, cookieKey);
                return null;
            }
        }
        if(userId == null || "null".equals(userId)){
            CookieUtil.removeCookie(response, cookieKey);
            return null;
        }
        FqUserCache fqUserCache = CacheManager.getUserCacheByUid(Integer.parseInt(userId));
        return fqUserCache;
    }

    /**
     * 读取cookie
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if(null != cookies){
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }



    /**
     * 用户登陆状态维持
     *
     * cookie设计为: des(私钥).encode(userId~time~maxAge~ip)
     *
     * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为-1（只在当前会话）<br>
     *                   记住密码默认为30天
     * @return void
     */
    public static void loginUser(HttpServletRequest request, HttpServletResponse response, FqUser user, boolean... remember) {

        request.setAttribute("user", user);
        // 获取用户的id、nickName
        String uid     = user.getId()+"";
        // 当前毫秒数
        long   now      = System.currentTimeMillis();
        // 超时时间
        int    maxAge   = -1;
        if (remember.length > 0 && remember[0]) {
            maxAge      = 60 * 60 * 24 * 30; // 30天
        }
        // 用户id地址
        String ip		= getIP(request);
        // 构造cookie
        StringBuilder cookieBuilder = new StringBuilder()
                .append(uid).append("~")
                .append(now).append("~")
                .append(maxAge).append("~")
                .append(ip);

        // cookie 私钥
        String secret = CommonConstant.USER_COOKIE_SECRET;
        // 加密cookie
        String userCookie = new DESUtils(secret).encryptString(cookieBuilder.toString());
        String cookieKey = CommonConstant.USER_ID_COOKIE;
        // 设置用户的cookie、 -1 维持成session的状态
        CookieUtil.setCookie(response, cookieKey, userCookie, maxAge);
    }

    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

}
