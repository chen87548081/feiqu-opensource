package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.UserTimeLine;
import com.feiqu.system.model.UserTimeLineExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.UserTimeLineService;
import com.feiqu.framwork.util.WebUtil;
import com.google.common.collect.Maps;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author cwd
 * @create 2017-10-15:05
 **/
@Controller
@RequestMapping("timeline")
public class TimeLineController extends BaseController {

    @Autowired
    private UserTimeLineService timeLineService;
    @Autowired
    private WebUtil webUtil;

    @PostMapping("postTimeline")
    @ResponseBody
    public Object postTimeline(UserTimeLine timeLine){
        BaseResult result = new BaseResult();
        if(timeLine.getUserId() == null){
            result.setResult(ResultEnum.USER_NOT_LOGIN);
            return result;
        }
        if(StringUtils.isBlank(timeLine.getContent())){
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }
        UserTimeLineExample example = new UserTimeLineExample();
        example.setOrderByClause("create_time desc");
        example.createCriteria().andUserIdEqualTo(timeLine.getUserId());
        UserTimeLine line = timeLineService.selectFirstByExample(example);
        if(line != null && DateUtils.isSameDay(line.getCreateTime(),new Date())){
            result.setResult(ResultEnum.TIME_LINE_SAME_DAY);
            return result;
        }
        timeLine.setCreateTime(new Date());
        timeLine.setDelFlag(YesNoEnum.NO.getValue());
        timeLineService.insert(timeLine);
        return result;

    }

    @GetMapping("list")
    public String timeline(HttpServletRequest request, HttpServletResponse response) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        UserTimeLineExample example = new UserTimeLineExample();
        example.setOrderByClause("create_time desc");
        example.createCriteria().andUserIdEqualTo(user.getId()).andCreateTimeGreaterThan(DateUtil.beginOfYear(new Date()));
        List<UserTimeLine> lines = timeLineService.selectByExample(example);
        Map<String, List<UserTimeLine>> map = Maps.newTreeMap(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj2.compareTo(obj1);
            }
        });
        for (UserTimeLine line : lines) {
            String yearMonth = DateUtil.format(line.getCreateTime(), "yyyy-MM");
            if (map.get(yearMonth) == null) {
                List<UserTimeLine> linesNew = new ArrayList<UserTimeLine>();
                linesNew.add(line);
                map.put(yearMonth, linesNew);
            } else {
                map.get(yearMonth).add(line);
            }
        }
        request.setAttribute("map", map);
        return "/user/timeline";
    }
}
