package com.feiqu.framwork.web.base;

import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.util.SpringContextUtil;
import com.feiqu.framwork.util.StringEscapeEditor;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.system.pojo.cache.FqUserCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web层通用数据处理
 * 
 * @author ruoyi
 */
public class BaseController
{
    private final static Logger _log = LoggerFactory.getLogger(BaseController.class);

    protected static final String USER_LOGIN_REDIRECT_URL = "redirect:/u/login";
    protected static final String GENERAL_ERROR_URL = "/error.html";
    protected static final String GENERAL_CUSTOM_ERROR_URL = "/error/generalCustomError.html";
    protected static final String GENERAL_NOT_FOUNF_404_URL = "/404.html";
    protected static final String GENERAL_TOPIC_DELETED_URL = "/topic-deleted.html";
    /**
     * 统一异常处理
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler({Exception.class})
    public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        _log.error("统一异常记录：", exception);
        /*request.setAttribute("ex", exception);
        if (null != request.getHeader("X-Requested-With") && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            request.setAttribute("requestHeader", "ajax");
        }*/
        return "/error.html";
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**

         * 自动转换日期类型的字段格式

         */
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**

         * 防止XSS攻击 改使用filter 又改回来了

         */
//        binder.setDisallowedFields("articleContent");
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    protected FqUserCache getCurrentUser(HttpServletRequest request, HttpServletResponse response){
        WebUtil webUtil = SpringContextUtil.getBean(WebUtil.class);
        return webUtil.currentUser(request,response);
    }

    protected FqUserCache getCurrentUser(){
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = servletRequestAttributes
                .getRequest();
        HttpServletResponse response = servletRequestAttributes
                .getResponse();
        WebUtil webUtil = SpringUtils.getBean(WebUtil.class);
        return webUtil.currentUser(request,response);
    }

}
