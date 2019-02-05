package com.feiqu.web.controller.extra2;

import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.spider.TopicInfoPipeline;
import com.feiqu.framwork.support.spider.V2exDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author cwd
 * @version SpiderController, v 0.1 2018/11/29 cwd 1049766
 */
@Controller
@RequestMapping("spider")
public class SpiderController {
    @Resource
    private TopicInfoPipeline topicInfoPipeline;



    @RequestMapping("v2exSpider")
    @ResponseBody
    public Object v2exSpider(){
        OOSpider ooSpider = OOSpider.create(Site.me()
                        .setUserAgent(CommonConstant.userAgentArray[new Random().nextInt(CommonConstant.userAgentArray.length)])
                        .addHeader("Referer","https://www.v2ex.com/").setSleepTime(5000).setDomain("v2ex.com"),
                topicInfoPipeline, V2exDTO.class);
        ooSpider.addUrl("https://www.v2ex.com/?tab=jobs")
                .run();
        return true;
    }

   /* @RequestMapping("zhipinSpider")
    @ResponseBody
    public Object zhipinSpider(){
        File file = FileUtil.file("D:\\webmagic\\www.zhipin.com");
        File[] files = file.listFiles();
        for(File fileS : files){
            List<String> list =  FileUtil.readLines(fileS, Charset.defaultCharset());
            String s = list.get(0);
            JSONObject jsonObject = new JSONObject(s);
            JSONArray hrefs = jsonObject.getJSONArray("corporationHref");
            for(Object j : hrefs){
                String companyUrl = (String) j;
                System.out.println(companyUrl);
            }
        }

        return true;
    }*/

}
