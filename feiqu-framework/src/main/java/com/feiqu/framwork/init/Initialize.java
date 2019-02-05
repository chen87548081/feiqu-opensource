package com.feiqu.framwork.init;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.setting.dialect.Props;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.GirlCategoryEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.base.BaseInterface;
import com.feiqu.system.mapper.FqAreaMapper;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.response.*;
import com.feiqu.system.pojo.simple.BeautySim;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.*;
import com.feiqu.system.service.impl.FqBackgroundImgServiceImpl;
import com.feiqu.system.service.impl.FqNoticeServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeesuite.cache.redis.JedisProviderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;

import java.util.*;

@Component
public class Initialize implements BaseInterface {

	private static Logger _log = LoggerFactory.getLogger(Initialize.class);

	public void init() {
		try {
			CommonConstant.DOMAIN_URL = Global.getConfig("feiqu.domainUrl");
			CommonConstant.MAX_UPLOAD_SIZE = Integer.valueOf(Global.getConfig("feiqu.maxUploadSize"));
			CommonConstant.FILE_UPLOAD_TEMP_PATH = Global.getConfig("feiqu.uploadPath");

			Props props = new Props("application.properties");
			CommonConstant.ALIYUN_OSS_BUCKET_NAME = props.getStr("aliyun.filesystem.bucketName");
			CommonConstant.ALIYUN_OSS_ENDPOINT = props.getStr("aliyun.filesystem.endpoint");

			PageHelper.startPage(1, 5 , false);
			ThoughtExample thoughtExample = new ThoughtExample();
			thoughtExample.setOrderByClause("create_time desc ");
			thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
			ThoughtService thoughtService = SpringUtils.getBean("thoughtServiceImpl");
			List<ThoughtWithUser> newThoughts = thoughtService.getThoughtWithUser(thoughtExample);
			if(CollectionUtil.isNotEmpty(newThoughts)){
                newThoughts.forEach(t->{
                    if(StringUtils.isNotEmpty(t.getPicList())){
                        t.setPictures(Arrays.asList(t.getPicList().split(",")));
                    }
                });
            }
			CommonConstant.NEW_THOUGHT_LIST = newThoughts;

			Date now = new Date();
			Date sevenDateBefore = DateUtil.offsetDay(now,-7);
			Date oneMonthDateBefore = DateUtil.offsetDay(now,-31);
			JedisCommands commands = JedisProviderFactory.getJedisCommands(null);
			PageHelper.startPage(1, 5 , false);
			thoughtExample.clear();
			thoughtExample.setOrderByClause("comment_count desc ");
			thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andCreateTimeGreaterThan(sevenDateBefore);
			List<SimThoughtDTO> simThoughtDTOS = Lists.newArrayList();
			List<Thought> sevenDaysThoughts = thoughtService.selectByExample(thoughtExample);
			if(CollectionUtil.isNotEmpty(sevenDaysThoughts)){
                sevenDaysThoughts.forEach(t->{
                    SimThoughtDTO simThoughtDTO = new SimThoughtDTO();
                    simThoughtDTO.setId(t.getId());
                    simThoughtDTO.setContent(t.getThoughtContent());
                    if(StringUtils.isNotEmpty(t.getPicList())){
                        simThoughtDTO.setPic(t.getPicList().split(",")[0]);
                    }else {
                        simThoughtDTO.setPic("");
                    }
                    simThoughtDTOS.add(simThoughtDTO);
                });
            }
//			String generalFormat = DateUtil.formatDate(now);
			commands.set(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST, JSON.toJSONString(simThoughtDTOS));
			commands.expire(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST,2*24*60*60);

			PageHelper.startPage(1, 5 , false);
			thoughtExample.clear();
			thoughtExample.setOrderByClause("comment_count desc ");
			thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
			CommonConstant.HOT_THOUGHT_LIST = thoughtService.getThoughtWithUser(thoughtExample);

			FqNoticeExample example = new FqNoticeExample();
			example.setOrderByClause("fq_order asc");
			example.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
			FqNoticeService fqNoticeService = SpringUtils.getBean(FqNoticeServiceImpl.class);
			List<FqNotice> list = fqNoticeService.selectByExample(example);
			if(CollectionUtil.isNotEmpty(list)){
                CommonConstant.FQ_NOTICE_LIST = list;
            }
			ArticleExample articleExample = new ArticleExample();
			articleExample.setOrderByClause("browse_count desc");
			articleExample.createCriteria().andCreateTimeGreaterThan(oneMonthDateBefore);
			PageHelper.startPage(0,10,false);
			ArticleService articleService = (ArticleService) SpringUtils.getBean("articleServiceImpl");
			CommonConstant.HOT_ARTICLE_LIST = articleService.selectByExample(articleExample);

			PageHelper.startPage(0,5,false);
			SuperBeautyService superBeautyService = (SuperBeautyService) SpringUtils.getBean("superBeautyServiceImpl");
			SuperBeautyExample beautyExample = new SuperBeautyExample();
			beautyExample.setOrderByClause("like_count desc");
			List<BeautyUserResponse> beauties = superBeautyService.selectDetailByExample(beautyExample);
			if(CollectionUtil.isNotEmpty(beauties)){
                CommonConstant.HOT_BEAUTY_LIST = beauties;
            }

			FqAreaMapper areaMapper = SpringUtils.getBean(FqAreaMapper.class);
			CommonConstant.AREA_LIST = areaMapper.selectByExample(new FqAreaExample());

			FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
			FqUserExample userExample = new FqUserExample();
			userExample.setOrderByClause("id desc");
//		userExample.createCriteria().andCreateTimeBetween(DateUtil.beginOfDay(DateUtil.yesterday()),DateUtil.endOfDay(DateUtil.yesterday()));
			PageHelper.startPage(0,10,false);
			List<FqUser> newUsers = fqUserService.selectByExample(userExample);//新用户10位
			List<FqUserSim> sims = Lists.newArrayList();
			for(FqUser fqUser : newUsers){
                FqUserSim sim = new FqUserSim(fqUser);
                sims.add(sim);
            }
			CommonConstant.NEW_SIMPLE_USERS= sims;

			userExample.clear();
			CommonConstant.FQ_USER_TOTAL_COUNT = fqUserService.countByExample(userExample);

			FqFriendLinkService fqFriendLinkService = SpringUtils.getBean("fqFriendLinkServiceImpl");
			List<FqFriendLink> fqFriendLinks = fqFriendLinkService.selectByExample(new FqFriendLinkExample());
			CommonConstant.FRIEND_LINK_LIST = fqFriendLinks;


/*CommonConstant.aliossFsProvider = new AliyunossProvider(props.getStr("fs.group3.endpoint"),
				props.getStr("fs.group3.groupName"), props.getStr("fs.group3.accessKey"), props.getStr("fs.group3.secretKey"));
			CommonConstant.ALIOSS_URL_PREFIX = props.getStr("fs.group3.urlprefix");

			CommonConstant.ALIYUN_OSS_CALLBACK = props.getStr("fs.group3.callback");
			CommonConstant.ALIYUN_OSS_ENDPOINT = props.getStr("fs.group3.endpoint");*/


			FqBackgroundImgService fqBackgroundImgService = SpringUtils.getBean(FqBackgroundImgServiceImpl.class);
			FqBackgroundImgExample backgroundImgExample = new FqBackgroundImgExample();
			backgroundImgExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
			FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(backgroundImgExample);

			CommonConstant.bgImgUrl = fqBackgroundImg == null?"https://img.t.sinajs.cn/t6/skin/skinvip805/images/body_bg.jpg?id=1410943047113":fqBackgroundImg.getImgUrl();

			for(GirlCategoryEnum categoryEnum : GirlCategoryEnum.values()){
                CommonConstant.CATEGORIES.add(new KeyValue(categoryEnum.getCode(),categoryEnum.getDesc()));
            }

			String banners = commands.get(CommonConstant.BEAUTY_BANNERS_REDIS);
			if(StringUtils.isNotEmpty(banners)){
				CommonConstant.BEAUTY_BANNERS = JSON.parseArray(banners, BeautySim.class);
			}

			int month = DateUtil.thisMonth()+1;
			Set<String> userIds =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,4);
			if(CollectionUtils.isNotEmpty(userIds)){
				List<Integer> userIdList = Lists.newArrayList();
				for(String userId : userIds){
					userIdList.add(Integer.valueOf(userId));
				}
				FqUserExample fqUserExample = new FqUserExample();
				example.createCriteria().andIdIn(userIdList);
				List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
				Map<Integer,FqUser> userMap = Maps.newHashMap();
				fqUsers.forEach(fqUser -> {
					userMap.put(fqUser.getId(),fqUser);
				});
				List<UserActiveNumResponse> responses = Lists.newArrayList();
				for(int i = 0;i<userIdList.size();i++){
					double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,userIdList.get(i).toString());
					if(userMap.get(userIdList.get(i)) == null){
						continue;
					}
					UserActiveNumResponse userActiveNumResponse = new UserActiveNumResponse(userMap.get(userIdList.get(i)),score,i+1);
					responses.add(userActiveNumResponse);
				}
				CommonConstant.FQ_ACTIVE_USER_LIST = responses;
			}

		} catch (Exception e) {
			_log.error("初始化报错",e);
		} finally {
			JedisProviderFactory.getJedisProvider(null).release();
		}

		_log.info(">>>>>飞趣社区 系统初始化完成！<<<<<<");
	}

}
