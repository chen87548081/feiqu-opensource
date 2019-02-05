package com.feiqu.web.controller.extra;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.FqVisitRecordExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FqVisitRecordResponse;
import com.feiqu.system.service.FqVisitRecordService;
import com.feiqu.framwork.util.WebUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */
@Controller
@RequestMapping("visit")
public class VisitController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(VisitController.class);
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private FqVisitRecordService visitRecordService;
    @ResponseBody
    @GetMapping("/records/{page}")
    public Object visitRecords(@PathVariable Integer page, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache fqUser = webUtil.currentUser(request,response);
        if(fqUser == null){
            return result;
        }
        FqVisitRecordExample example = new FqVisitRecordExample();
        example.createCriteria().andVisitedUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        example.setOrderByClause("visit_time desc");
        PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE);
        List<FqVisitRecordResponse> list = visitRecordService.selectVisitsByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        result.setData(pageInfo);
        return result;
    }
}
