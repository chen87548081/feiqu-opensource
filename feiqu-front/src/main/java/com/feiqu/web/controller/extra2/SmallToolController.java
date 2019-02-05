package com.feiqu.web.controller.extra2;

import com.feiqu.framwork.web.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cwd
 * @version SmallToolController, v 0.1 2018/8/20 cwd 1049766
 */
@Controller
@RequestMapping("smallTool")
public class SmallToolController extends BaseController {

    @GetMapping("index")
    public String index(){
        return "/smallTool/index.html";
    }

}
