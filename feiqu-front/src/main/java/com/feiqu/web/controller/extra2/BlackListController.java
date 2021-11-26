package com.feiqu.web.controller.extra2;

import cn.hutool.core.lang.Validator;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.pojo.response.IpVisitDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

/**
 * @author cwd
 * @version BlackListController, v 0.1 2018/12/26 cwd 1049766
 */
@RequestMapping("blackList")
@Controller
public class BlackListController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(BlackListController.class);

    @GetMapping("denyService")
    public String denyService(){
        return "/blackList/denyService";
    }

    @GetMapping("manage")
    public String manage(Model model, @RequestParam(required = false) String queryDate){
        try {
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Set<String> ips =commands.smembers(CommonConstant.FQ_BLACK_LIST_REDIS_KEY);
            model.addAttribute("ips",ips);
            Set<Tuple> tuples = Sets.newHashSet();
            String key = "";
            if(StringUtils.isNotEmpty(queryDate)){
                key = "fq_ip_data_"+queryDate;
                tuples = commands.zrevrangeWithScores(key,0,100);
            }else {
                tuples = commands.zrevrangeWithScores(CommonConstant.FQ_IP_DATA_THIS_DAY_FORMAT,0,100);
            }
            List<IpVisitDTO> keyValueList = Lists.newArrayList();
            tuples.forEach(tuple -> keyValueList.add(new IpVisitDTO(tuple.getElement(),tuple.getScore()+"", CommonUtils.getFullRegionByIp(tuple.getElement()))));
            model.addAttribute("visitIps",keyValueList);
            model.addAttribute("beauties",CommonConstant.BEAUTY_BANNERS);
        } catch (Exception e) {
            logger.error("",e);
            model.addAttribute("errorMsg","出错了");
            return GENERAL_CUSTOM_ERROR_URL;
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();;
        }
        return "/blackList/manage";
    }

    @PostMapping("/manage/remove")
    @ResponseBody
    public BaseResult manageList(String ip){
        BaseResult result = new BaseResult();
        try {
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            Long id  =commands.srem(CommonConstant.FQ_BLACK_LIST_REDIS_KEY,ip);
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();;
        }
        return result;
    }

    @PostMapping("/manage/add")
    @ResponseBody
    public BaseResult add(String ip){
        BaseResult result = new BaseResult();
        try {
            if(!Validator.isIpv4(ip)){
                result.setResult(ResultEnum.FAIL);
                result.setMessage("IP格式不正确！");
                return result;
            }
            JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
            commands.sadd(CommonConstant.FQ_BLACK_LIST_REDIS_KEY,ip);
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            JedisProviderFactory.getJedisProvider(null).release();;
        }
        return result;
    }
}
