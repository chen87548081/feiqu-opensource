package com.feiqu.framwork.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqUserService;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import redis.clients.jedis.JedisCommands;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by chenweidong on 2018/1/16.
 */
public class CommonUtils {

    private static Log logger = LogFactory.get();

    private static DbSearcher searcher = null;
    //匹配@ 的所有的昵称
    private static Pattern aitePattern = Pattern.compile("@([\\w\\u4e00-\\u9fa5]+\\s{1})");

    static {
        try {
            String dbPath = Global.getConfig("feiqu.ip2regionDbPath");
            logger.info("ip2region db数据初始化路径：{}",dbPath);
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, dbPath);
        } catch (DbMakerConfigException | FileNotFoundException e) {
            logger.error(e);
        }
    }

    public static String getRegionByIp(String ip){
        try {
            DataBlock dataBlock = searcher.memorySearch(ip);
            String region = dataBlock.getRegion();
            logger.info("ip:{},region:{}",ip,region);
            String[] regions = StringUtils.split(region,"|");
            if("0".equals(regions[3])){
                if(!"0".equals(regions[2])){
                    return regions[0]+regions[2];
                }
                return regions[0];
            }
            if(!regions[2].equals(regions[3])){
                return regions[2]+regions[3];
            }
            return regions[3];
        } catch (Exception e) {
            logger.error(e);
            return "未知";
        }
    }

    public static String getFullRegionByIp(String ip){
        try {
            DataBlock dataBlock = searcher.memorySearch(ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            logger.error(e);
            return "未知";
        }
    }

    public static List<String> findAiteNicknames(String content){
        return ReUtil.findAll(aitePattern,content,1);
    }

    //增加活跃度
    public static void addActiveNum(Integer userId,double score){
        int month = DateUtil.thisMonth()+1;
        String key = CommonConstant.FQ_ACTIVE_USER_SORT+month;
        try {
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Double scoreStore = commands.zscore(key,userId.toString());
            if(scoreStore == null){
                scoreStore = score;
            }else {
                scoreStore += score;
            }
            commands.zadd(key,scoreStore,userId.toString());
            commands.expire(key,180*24*60*60);
        } catch (Exception e) {
            logger.error(e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();
        }
    }

    public static void  addOrDelUserQudouNum(FqUserCache user, int num) {
        FqUser toUpdateUser = new FqUser();
        toUpdateUser.setId(user.getId());
        toUpdateUser.setQudouNum(user.getQudouNum() == null? 1:user.getQudouNum()+num);
        FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
        fqUserService.updateByPrimaryKeySelective(toUpdateUser);
        user.setQudouNum(toUpdateUser.getQudouNum());
        CacheManager.refreshUserCacheByUser(user);
    }
    public static void sendMsg(Integer type, Integer receiveUserId, Date time, String content){
        if(MsgEnum.OFFICIAL_MSG.getValue().equals(type)){
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(time == null?new Date():time);
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(receiveUserId);
            message.setType(type);
            message.setContent(content);
            CMessageService messageService = SpringUtils.getBean(CMessageService.class);
            messageService.insert(message);
        }

    }
}
