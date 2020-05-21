package com.hyhello.priceless.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * sb 异常捕获 返回code .500
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "static/index.html";
    }

    @RequestMapping("/")
    public String homepage(){
        return "static/homepage.html";
    }
}
