package com.feiqu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/11/6.
 */
@Controller
@RequestMapping("game")
public class GameController {

    @GetMapping("meditation")
    public String meditation(){
        return "/game/meditation.html";
    }
    @GetMapping("study")
    public String study(){
        return "/game/study.html";
    }
}
