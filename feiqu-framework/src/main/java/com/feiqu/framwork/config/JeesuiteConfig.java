package com.feiqu.framwork.config;

import com.feiqu.common.config.Global;
import com.feiqu.common.utils.AESUtil;
import com.jeesuite.cache.redis.JedisProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class JeesuiteConfig {
    @Bean
    public JedisProviderFactoryBean initJedisProvider(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig){
        JedisProviderFactoryBean jedisProviderFactoryBean = new JedisProviderFactoryBean();
        jedisProviderFactoryBean.setJedisPoolConfig(jedisPoolConfig);
        jedisProviderFactoryBean.setServers(Global.getConfig("feiqu-redis.servers"));
        jedisProviderFactoryBean.setPassword(AESUtil.aesDecode(Global.getConfig("feiqu-redis.password")));
        jedisProviderFactoryBean.setMode(Global.getConfig("feiqu-redis.mode"));

        return jedisProviderFactoryBean;
    }

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig initJedisPool(){

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(Global.getPropertiesConfig("jeesuite.cache.maxPoolSize")));
        config.setMaxIdle(Integer.parseInt(Global.getPropertiesConfig("jeesuite.cache.maxPoolIdle")));
        config.setMinIdle(Integer.parseInt(Global.getPropertiesConfig("jeesuite.cache.minPoolIdle")));
        return config;
    }
}
