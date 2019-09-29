package com.hyhello.priceless.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class AccessRecordController {

    @RequestMapping("/log")
    public String logAccess(){

        return "访问记录成功";
    }

}
