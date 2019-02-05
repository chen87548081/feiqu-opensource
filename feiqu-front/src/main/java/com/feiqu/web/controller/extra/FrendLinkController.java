package com.feiqu.web.controller.extra;

import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqFriendLinkService;
import com.feiqu.framwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/2.
 */
@Controller
@RequestMapping("friendLink")
public class FrendLinkController {
    @Autowired
    private FqFriendLinkService fqFriendLinkService;
    @Autowired
    private WebUtil webUtil;

    @ResponseBody
    @GetMapping("add")
    public Object addLink(FqFriendLink fqFriendLink, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        FqUserCache fqUser = webUtil.currentUser(request,response);
        if(fqUser == null){
            result.setResult(ResultEnum.FAIL);
            return result;
        }
        if(!fqUser.getRole().equals(UserRoleEnum.ADMIN_USER_ROLE.getValue())){
            result.setResult(ResultEnum.FAIL);
            return result;
        }
        fqFriendLink.setCreateTime(new Date());
        fqFriendLinkService.insert(fqFriendLink);
        return result;
    }
}
