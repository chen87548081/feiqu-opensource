package com.feiqu.framwork.config;

import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.WebUtil;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
    
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {

                //如果不是ajax 代表是点击页面 则统计点击数量
                if (null == request.getHeader("X-Requested-With") || !request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
                    try {
                        String ip = WebUtil.getIP(request);
                        JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
                        boolean isBlackIp = commands.sismember(CommonConstant.FQ_BLACK_LIST_REDIS_KEY,ip);
                        if(isBlackIp){
                            logger.info("该ip:{} 存在于黑名单，禁止其访问！",ip);
                            response.sendRedirect(request.getContextPath()+"/blackList/denyService");
                            return false;
                        }

                        String clickkey = CommonConstant.FQ_IP_DATA_THIS_DAY_FORMAT;
                        Double score = commands.zscore(clickkey,ip);
                        if(score == null){
                            commands.zadd(clickkey,1,ip);
                        }else {
                            commands.zadd(clickkey,score+1,ip);
                        }
                        commands.expire(clickkey,30*24*60*60);//存放一个月
                    } finally{
                        JedisProviderFactory.getJedisProvider(null).release();
                    }
                }
                return true;
            }
        }).addPathPatterns("/**").excludePathPatterns("/blackList/denyService");
    }
    
	/**
	 * 跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/*registry.addMapping("/api*//**")
		.allowedOrigins("http://ibeetl.com","http://www.ibeetl.com")
		.allowedMethods("*");*/
		
	}
}
