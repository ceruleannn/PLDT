package com.hyhello.priceless.controller;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import com.hyhello.priceless.dto.Response;
import com.hyhello.priceless.service.AccessLogService;
import com.hyhello.priceless.service.AccessLogWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/accessLog")
public class AccessLogController {

    @Autowired
    public AccessLogService accessLogService;

    @PostMapping("")
    @ResponseBody
    public Response logAccess(AccessLog accessLog){

        accessLogService.saveAccessLog(accessLog);

        return new Response(200, "访问记录成功: " + accessLog.getUrl() + " " + accessLog.getTitle());
    }




}
