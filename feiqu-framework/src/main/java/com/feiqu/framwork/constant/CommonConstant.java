package com.feiqu.framwork.constant;

import cn.hutool.core.date.DateUtil;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.FqArea;
import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.model.FqNotice;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.pojo.response.UserActiveNumResponse;
import com.feiqu.system.pojo.simple.BeautySim;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

/**
 * @author cwd
 * @create 2017-09-17:09
 **/
@Configuration
public class CommonConstant {

    public static String THOUGHT_TOP_LIST = "thought_top_list";
    public static String USER_ID_COOKIE = "cwd33";
    public static String USER_COOKIE_SECRET = "cwd11";
    public static String FORGET_PASSWORD_SECRET = "cwd22";
//    public static String DOMAIN_URL = "http://www.flyfun.site/";

    public static String DOMAIN_URL = "";
    public static Integer MAX_UPLOAD_SIZE = null;
    public static String FILE_UPLOAD_TEMP_PATH = null;
    public static String BAIDU_MAP_API_URL = null;
    public static String BAIDU_MAP_API_AK = null;
    public static String FILE_NAME_PREFIX = "fq_";
    public static String ICON_UPLOAD_PATH= "";
    public static Integer INIT_QUDOU_NUM= 20;
    public static Integer SIGN_DAYS_QUDOU_NUM_5 = 1;
    public static Integer SIGN_DAYS_QUDOU_NUM_15 = 5;
    public static Integer SIGN_DAYS_QUDOU_NUM_30 = 10;
    public static Integer SIGN_DAYS_QUDOU_NUM_30_MORE = 20;
    public static Integer DEAULT_PAGE_SIZE= 10;

    public static List<FqNotice> FQ_NOTICE_LIST= null;
    public static List<Article> HOT_ARTICLE_LIST= null;
    public static List<FqArea> AREA_LIST= null;
    public static List<BeautyUserResponse> HOT_BEAUTY_LIST = null;
    public static List<ThoughtWithUser> HOT_THOUGHT_LIST = null;
    public static List<ThoughtWithUser> NEW_THOUGHT_LIST = null;


    public static List<FqFriendLink> FRIEND_LINK_LIST = null;

    public static String ALIOSS_URL_PREFIX = null;
    public static String ALIYUN_OSS_BUCKET_NAME = null;

    public final static int TIMEOUT = 10000;

    public final static List<String> picExtList = Lists.newArrayList("jpg","png","gif","bmp","jpeg");

    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    //昨天新加入的新用户
    public static List<FqUserSim> NEW_SIMPLE_USERS = null;

    public static String bgImgUrl = null;
    //飞趣社区总人数
    public static int FQ_USER_TOTAL_COUNT = 0;

    public static int ALIYUN_OSS_EXPIRE = 300;
    public static long ALIYUN_OSS_MAX_SIZE = 20;//15M
    public static String ALIYUN_OSS_CALLBACK = null;
    public static String ALIYUN_OSS_ENDPOINT = null;
    public static String SYSTEM_ERROR_CODE = "4000";
    public static String FQ_ACTIVE_USER_SORT = "fq_active_user_sort_";
    public static List<KeyValue> CATEGORIES = Lists.newArrayList();
    public static String QINIU_PIC_STYLE_NAME = "-pic.water.mark";
    public static String FQ_IP_DATA_THIS_DAY_FORMAT = "fq_ip_data_"+ DateUtil.formatDate(new Date());
    public static String FQ_BLACK_LIST_REDIS_KEY = "fq_black_list_key";
    public static String FQ_FOLLOW_PREFIX = "fq_follow_prefix_";
    public static String SEVEN_DAYS_HOT_THOUGHT_LIST = "fq_7_days_hot_thoughts_"+DateUtil.formatDate(new Date());//7天最热的
    public static String GENERAL_CUSTOM_ERROR_CODE = "errorMsg";//


    public static List<BeautySim> BEAUTY_BANNERS = Lists.newArrayList();
    public static String BEAUTY_BANNERS_REDIS = "fq_beauty_banner";

    //网址点击事件统计
    public static String FQ_USER_WEBSITE_CLICK_COUNT = "fq_user_website_click_count";
    public static String FQ_WEBSITE_ALL = "fq_website_all";
    public static List<UserActiveNumResponse> FQ_ACTIVE_USER_LIST = Lists.newArrayList();

}
