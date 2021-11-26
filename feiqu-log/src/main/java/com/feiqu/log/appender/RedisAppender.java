package com.feiqu.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.feiqu.common.config.Global;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.log.redis.RedisClient;
import com.feiqu.log.util.LogMessageUtil;
import com.feiqu.system.model.collectData.RunLogMessage;

import java.util.concurrent.ThreadPoolExecutor;


public class RedisAppender extends AppenderBase<ILoggingEvent> {
    private RedisClient redisClient;
    private String appName  = Global.getConfig("appName");
    private String redisHost = Global.getConfig("spring.redis.host");
    private String redisPort  = Global.getConfig("spring.redis.port");
    private String redisPass  = Global.getConfig("spring.redis.password");

    private static ThreadPoolExecutor threadPoolExecutor
            = ThreadUtil.newExecutor(4,8);

    @Override
    protected void append(ILoggingEvent event) {
        if (redisClient == null) {
            redisClient = RedisClient.getInstance(this.redisHost, Integer.parseInt(this.redisPort), redisPass);
        }
        threadPoolExecutor.execute(()->{
            RunLogMessage logMessage = LogMessageUtil.getLogMessage(appName, event);
            final String redisKey = BizConstant.LOG_KEY;
            redisClient.pushMessage(redisKey, JSONUtil.toJsonStr(logMessage));
        });

    }
}
