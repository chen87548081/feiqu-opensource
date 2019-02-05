package com.feiqu.framwork.init;

import com.feiqu.common.utils.SpringUtils;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/27.
 */
@Component
public class FeiquInitTrigger implements CommandLineRunner {
    private static Logger _log = LoggerFactory.getLogger(FeiquInitTrigger.class);

/*@Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(null == contextRefreshedEvent.getApplicationContext().getParent()) {
            _log.debug(">>>>> spring初始化完毕 <<<<<");
            // spring初始化完毕后，通过反射调用所有使用BaseService注解的initMapper方法
            Map<String, Object> baseServices = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(BaseService.class);
            for(Object service : baseServices.values()) {
                _log.debug(">>>>> {}.initMapper()", service.getClass().getName());
                try {
                    Method initMapper = service.getClass().getMethod("initMapper");
                    initMapper.invoke(service);
                } catch (Exception e) {
                    _log.error("初始化BaseService的initMapper方法异常", e);
                    e.printStackTrace();
                }
            }

            // 系统入口初始化
            Map<String, BaseInterface> baseInterfaceBeans = contextRefreshedEvent.getApplicationContext().getBeansOfType(BaseInterface.class);
            for(Object service : baseInterfaceBeans.values()) {
                _log.debug(">>>>> {}.init()", service.getClass().getName());
                try {
                    Method init = service.getClass().getMethod("init");
                    init.invoke(service);
                } catch (Exception e) {
                    _log.error("初始化BaseInterface的init方法异常", e);
                    e.printStackTrace();
                }
            }
        }
    }*/


    @Override
    public void run(String... strings) throws Exception {
        _log.info(">>>>> spring初始化完毕 <<<<<");
        // spring初始化完毕后，通过反射调用所有使用BaseService注解的initMapper方法
        Map<String, Object> baseServices = SpringUtils.getBeansWithAnnotation(BaseService.class);
        for(Object service : baseServices.values()) {
            _log.info(">>>>> {}.initMapper()", service.getClass().getName());
            try {
                Method initMapper = service.getClass().getMethod("initMapper");
                initMapper.invoke(service);
            } catch (Exception e) {
                _log.error("初始化BaseService的initMapper方法异常", e);
                e.printStackTrace();
            }
        }

        // 系统入口初始化
        Map<String, BaseInterface> baseInterfaceBeans = (Map<String, BaseInterface>) SpringUtils.getBeansOfType(BaseInterface.class);
        for(Object service : baseInterfaceBeans.values()) {
            _log.debug(">>>>> {}.init()", service.getClass().getName());
            try {
                Method init = service.getClass().getMethod("init");
                init.invoke(service);
            } catch (Exception e) {
                _log.error("初始化BaseInterface的init方法异常", e);
                e.printStackTrace();
            }
        }

    }
}
