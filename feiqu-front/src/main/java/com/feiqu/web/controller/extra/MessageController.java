package com.feiqu.web.controller.extra;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.OrderEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.CMessageExample;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.Dialog;
import com.feiqu.system.pojo.response.MessageUserDetail;
import com.feiqu.system.service.CMessageService;
import com.feiqu.system.service.FqUserService;
import com.feiqu.framwork.util.WebUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * MessageController
 *
 * @author chenweidong
 * @date 2017/11/21
 */
@RequestMapping("/message")
@Controller
public class MessageController extends BaseController
{
    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private WebUtil webUtil;
    @Autowired
    private CMessageService messageService;
    @Autowired
    private FqUserService fqUserService;

    @PostMapping("read")
    @ResponseBody
    public Object read(HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            CMessageExample example = new CMessageExample();
            example.createCriteria().andReceivedUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            CMessage message =new CMessage();
            message.setIsRead(YesNoEnum.YES.getValue());
            messageService.updateByExampleSelective(message,example);
        } catch (Exception e) {
            logger.error("消息已读失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("/delDialog/{uId}")
    @ResponseBody
    public Object delDialog(@PathVariable Integer uId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        logger.info("删除对话 uid:"+uId);

        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                return ResultEnum.USER_NOT_FOUND;
            }
            logger.error(fqUser.getId()+"删除对话："+uId);
            CMessageExample example = new CMessageExample();
            example.createCriteria().andReceivedUserIdEqualTo(fqUser.getId()).andPostUserIdEqualTo(uId);
            CMessage message =new CMessage();
            message.setDelFlag(YesNoEnum.YES.getValue());
            messageService.updateByExampleSelective(message,example);
        } catch (Exception e) {
            logger.error("消息删除失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @GetMapping("dialogs")
    public String dialog(HttpServletRequest request, HttpServletResponse response) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return "redirect:/u/login?redirectSuccessUrl="+CommonConstant.DOMAIN_URL+request.getRequestURI();
        }
        logger.info("对话 uid:"+user.getId());
        CMessageExample example = new CMessageExample();
        example.createCriteria().andTypeEqualTo(MsgEnum.OFFICIAL_MSG.getValue())
                .andDelFlagEqualTo(YesNoEnum.NO.getValue())
                .andReceivedUserIdEqualTo(user.getId());
        example.setOrderByClause("create_time desc");
        //拿到官方消息第一条
        CMessage message = messageService.selectFirstByExample(example);
        request.setAttribute("sysMessage", message);
        //只拿到好友的消息
        List<Dialog> dialogs = messageService.selectDialogsByUserId(user.getId());
        request.setAttribute("dialogs", dialogs);

        return "/user/dialogs";
    }

    /*
    查看对话
     */
    @GetMapping("/dialog/{userId}")
    public String msgs(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(defaultValue = "0") Integer pageIndex,
                       @PathVariable Integer userId, @RequestParam(defaultValue = "desc") String order) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return "redirect:/u/login?redirectSuccessUrl="+CommonConstant.DOMAIN_URL+request.getRequestURI();
        }
        if(!OrderEnum.ASC.getCode().equals(order) && !OrderEnum.DESC.getCode().equals(order)){
            return "redirect:/message/dialogs";
        }
        int myUserId = user.getId(),friendUserId = userId;

        FqUserExample userExample = new FqUserExample();
        userExample.createCriteria().andIdEqualTo(userId);
        int userCount = fqUserService.countByExample(userExample);
        //userId -1 代表系统消息
        if(userId != -1 && userCount <= 0){
            return "redirect:/message/dialogs";
        }
        PageHelper.startPage(pageIndex, CommonConstant.DEAULT_PAGE_SIZE);

        List<MessageUserDetail> messages = messageService.selectDialogDetail(myUserId,friendUserId, order);

//        List<MessageUserDetail> messages = messageService.selectMyMsgsByMessage(messageExample);
        PageInfo page = new PageInfo(messages);
        request.setAttribute("postUserId", userId);
        request.setAttribute("count", page.getTotal());
        request.setAttribute("messages", messages);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("pageSize", CommonConstant.DEAULT_PAGE_SIZE);
        return "/user/msgs";
    }
}
