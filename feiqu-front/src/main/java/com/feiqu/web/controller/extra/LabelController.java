package com.feiqu.web.controller.extra;

import com.feiqu.system.service.FqLabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * LabelController
 *
 * @author chenweidong
 * @date 2017/11/20
 */
@Controller
@RequestMapping("label")
public class LabelController {

    private static Logger logger = LoggerFactory.getLogger(LabelController.class);
    @Autowired
    private FqLabelService fqLabelService;

    @GetMapping("/article/{labelId}")
    private String labelArticle(@PathVariable Integer labelId){

        return "/label/articles.html";
    }



}
