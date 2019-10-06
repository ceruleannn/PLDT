package com.hyhello.priceless.controller;

import com.hyhello.priceless.dataaccess.entity.AccessLogEntity;
import com.hyhello.priceless.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/access")
public class AccessLogController {

    @Autowired
    public AccessLogService accessLogService;

    @PostMapping("/log")
    public String logAccess(String url, String title){

        accessLogService.saveAccessLog(url,title);

        return "访问记录成功: " + url + " " + title;
    }

}
