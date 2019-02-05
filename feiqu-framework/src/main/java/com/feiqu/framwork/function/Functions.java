package com.feiqu.framwork.function;

import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.system.model.FqBackgroundImg;
import com.feiqu.system.model.FqBackgroundImgExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqBackgroundImgService;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author cwd
 * @create 2017-10-14:59
 **/
@Service(value = "beetl_functions")
public class Functions {

    @Autowired
    WebUtil webUtil;


    /**
     * 继续encode URL (url,传参tomcat会自动解码)
     * 要作为参数传递的话，需要再次encode
     * @return String
     */
    public String encodeUrl(String url) {
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return url;
    }

    /**
     * 模版中拿取cookie中的用户信息
     *
     * @param request
     * @param response
     * @return
     */
    public FqUserCache currentUser(HttpServletRequest request, HttpServletResponse response) {
        return webUtil.currentUser(request, response);
    }

    public String currentBgImgUrl(int uid){
        if(uid <= 0){return "";}
        JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
        try {
            String key = CacheManager.getUserBackImgKey(uid);
            String picUrl = commands.get(key);
            if(StringUtils.isEmpty(picUrl)){
                FqBackgroundImgService fqBackgroundImgService = SpringUtils.getBean("fqBackgroundImgServiceImpl");
                FqBackgroundImgExample example = new FqBackgroundImgExample();
                example.createCriteria().andUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(example);
                if(fqBackgroundImg != null){
                    picUrl = fqBackgroundImg.getImgUrl();
                }else {
                    picUrl = "null";
                }
                commands.set(key,picUrl);
                commands.expire(key,60*60*24);
                return picUrl;
            }else {
                return picUrl;
            }
        } finally{
            JedisProviderFactory.getJedisProvider(null).release();
        }
//        return CommonConstant.bgImgUrl;
    }
}
