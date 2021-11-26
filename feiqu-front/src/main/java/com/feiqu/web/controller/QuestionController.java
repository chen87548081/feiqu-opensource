package com.feiqu.web.controller;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.system.model.Question;
import com.feiqu.system.model.QuestionExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.QuestionService;
import com.feiqu.framwork.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author cwd
 * @create 2017-10-15:31
 **/
@Controller
public class QuestionController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WebUtil webUtil;

    @RequestMapping("question")
    public String question(Model model, HttpServletRequest request, HttpServletResponse response){
        FqUserCache user = webUtil.currentUser(request,response);
        if(user == null){
            return "redirect:/u/login?redirectSuccessUrl=question";
        }
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("createtime desc");
        List list = questionService.selectWithUserByExample(example);
        model.addAttribute("list",list);
        return "/question/index";
    }

    @ResponseBody
    @RequestMapping(value = "question",method = RequestMethod.POST)
    public Object goquestion(@RequestBody Question question){
        BaseResult result = new BaseResult();
        try {
            if(StringUtils.isBlank(question.getQueContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            question.setCreatetime(new Date());
            questionService.insert(question);
        } catch (Exception e) {
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

}
